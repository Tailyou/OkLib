package com.hengda.zwf.commonhttp

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/2/15 14:06
 * 描述：Retrofit基类
 */
abstract class BaseRetrofit {

    private var okHttpClient = initOkHttp()

    abstract fun initOkHttp(): OkHttpClient

    fun <T> getApiService(baseUrl: String, clz: Class<T>): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(clz)
    }

}
