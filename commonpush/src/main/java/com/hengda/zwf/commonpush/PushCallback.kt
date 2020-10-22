package com.hengda.zwf.commonpush

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2018/5/22 17:43
 * 描述：
 */
abstract class PushCallback {
    abstract fun bindDevice2ClientId(clientId: String)

    abstract fun onConnectionChange(isConnected: Boolean)

    abstract fun onReceiveMsg(type: String, content: String)

    open fun onLogReceived(type: Int, log: String?) {}
}