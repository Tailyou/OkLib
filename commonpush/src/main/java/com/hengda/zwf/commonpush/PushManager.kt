package com.hengda.zwf.commonpush

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.charset.Charset
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2018/5/22 16:56
 * 描述：
 */
class PushManager(private var pushConfig: PushConfig) {

    companion object {
        const val TAG = "PushManager"
    }

    private var heartThread = Executors.newScheduledThreadPool(1)
    private var mainExecutor = object : Handler(Looper.getMainLooper()) {}
    private var threadPool = Executors.newCachedThreadPool()

    private var socket: Socket? = null
    private var isReader: InputStreamReader? = null
    private var osWriter: OutputStreamWriter? = null
    private var isConnected = false

    init {
        if (pushConfig.ip.isBlank() || pushConfig.port == 0) {
            throw IllegalArgumentException("Illegal parameters")
        }
        connect()
        heartbeat()
    }

    /**
     * 发起连接
     */
    private fun connect() {
        threadPool.execute {
            try {
                socket = Socket(pushConfig.ip, pushConfig.port)
                isReader = InputStreamReader(socket?.getInputStream(), Charset.forName("UTF-8"))
                osWriter = OutputStreamWriter(socket?.getOutputStream())
                printLog("connect status: ${socket?.isConnected}")
                mainExecutor.post { pushCallback?.onConnectionChange(true) }
                isConnected = true
                receive()
            } catch (e: Exception) {
                printLog("connect status: failed")
                mainExecutor.post { pushCallback?.onConnectionChange(false) }
                isConnected = false
                try {
                    Thread.sleep(500)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                connect()
            }
        }
    }

    /**
     * 心跳
     */
    private fun heartbeat() {
        heartThread.scheduleAtFixedRate({
            try {
                if (isConnected) {
                    socket?.sendUrgentData(0xFF)
                }
            } catch (e: Exception) {
                Log.i(TAG, "connect info: disconnected，connect again")
                mainExecutor.post { pushCallback?.onConnectionChange(false) }
                isConnected = false
                connect()
            }
        }, 0, 3000, TimeUnit.MILLISECONDS)
    }

    /**
     * 接收
     */
    private fun receive() {
        threadPool.execute {
            while (isConnected && socket != null && isReader != null) {
                try {
                    val chars = CharArray(8192)
                    val length = isReader?.read(chars)
                    if (length != null && length > 0) {
                        val content = String(chars, 0, length)
                        if (content.isNotBlank()) {
                            printLog("receive data: $content")
                            pushCallback?.onLogReceived(2, content)
                            format(content).forEach { dealResponse(it) }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    pushCallback?.onLogReceived(2, e.message)
                }
            }
        }
    }

    /**
     * 发送
     */
    private fun send(content: String) {
        threadPool.execute {
            if (isConnected && socket != null && osWriter != null) {
                try {
                    printLog("send data: $content")
                    pushCallback?.onLogReceived(1, content)
                    osWriter?.write("$content\n")
                    osWriter?.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                    pushCallback?.onLogReceived(1, e.message)
                }
            } else {
                mainExecutor.post { pushCallback?.onConnectionChange(false) }
                isConnected = false
                connect()
            }
        }
    }

    /**
     * 关闭
     */
    fun close() {
        isConnected = false
        try {
            socket?.close()
            isReader?.close()
            osWriter?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        threadPool.shutdownNow()
        heartThread.shutdownNow()
    }

    /**
     * 格式化服务器返回
     */
    private fun format(content: String): MutableList<PushResponse> {
        var result = mutableListOf<PushResponse>()
        try {
            var tempContent = content.replace("}{", "},{")
            tempContent = "[$tempContent]"
            val type = object : TypeToken<MutableList<PushResponse>>() {}.type
            result = Gson().fromJson(tempContent, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 处理
     */
    private fun dealResponse(r: PushResponse) {
        when (r.type) {
            "bind" -> {
                mainExecutor.post { pushCallback?.bindDevice2ClientId(r.client_id) }
            }
            "heart" -> {
                send("heart_response")
            }
            "sent_msg" -> {
                mainExecutor.post { pushCallback?.onReceiveMsg(r.send_type, r.send_content) }
            }
        }
    }

    /**
     * 打印日志
     */
    private fun printLog(content: String) {
        if (pushConfig.debug) {
            Log.i(TAG, content)
        }
    }

    private var pushCallback: PushCallback? = null

    fun setPushCallback(callback: PushCallback) {
        this.pushCallback = callback
    }

}