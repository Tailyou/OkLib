package com.hengda.zwf.commonhttp

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/11/11 16:45
 * 描述：Parameter拦截器，包装通用请求参数
 */
class HttpInterceptorParameter : Interceptor {

    var parameters: Map<String, String>? = null

    constructor(parameters: Map<String, String>) {
        this.parameters = parameters
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.url().newBuilder()
        if (parameters != null && parameters!!.isNotEmpty()) {
            for ((key, value) in parameters!!) {
                builder.addQueryParameter(key, value)
            }
        }
        val url = builder.build()
        val newRequest = originalRequest.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }

}
