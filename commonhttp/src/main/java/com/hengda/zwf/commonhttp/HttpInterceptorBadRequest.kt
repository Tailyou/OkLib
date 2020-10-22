package com.hengda.zwf.commonhttp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/11/11 16:45
 * 描述：BadRequest拦截器，处理错误请求（不规范返回）
 */
class HttpInterceptorBadRequest(private var reqSucceedCode: String = "1") : Interceptor {

    private val gson by lazy { Gson() }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder()
                .apply {
                    amendResponseBody(response)
                }
                .apply {
                    amendResponseCode(response)
                }
                .build()
    }

    //修正状态码
    private fun Response.Builder.amendResponseCode(response: Response) {
        val code = if (response.code() in 400 until 500) 200 else response.code()
        this.code(code)
    }

    //修正响应体
    private fun Response.Builder.amendResponseBody(response: Response) {
        val originBody = response.body()
        var json = originBody?.string()
        var res: HttpResponse<Any>? = null
        try {
            res = gson.fromJson(json, object : TypeToken<HttpResponse<Any>>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            res = gson.fromJson(json, object : TypeToken<HttpResponse<List<Any>>>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (res?.status != reqSucceedCode && !res?.status?.startsWith("200")!!) {
            res.data = null
            json = gson.toJson(res)
        }
        this.body(ResponseBody.create(originBody?.contentType(), json))
    }

}
