package com.hengda.zwf.commonhttp

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/11/11 16:45
 * 描述：Header拦截器，包装通用请求头
 */
class HttpInterceptorHeader : Interceptor {

    var parameters: Map<String, String>? = null

    constructor(parameters: Map<String, String>) {
        this.parameters = parameters
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (parameters != null && parameters!!.isNotEmpty()) {
            for ((key, value) in parameters!!) {
                builder.header(key, value)
            }
        }
        return chain.proceed(builder.build())
    }

}
