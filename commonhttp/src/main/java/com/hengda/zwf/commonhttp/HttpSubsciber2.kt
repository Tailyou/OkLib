package com.hengda.zwf.commonhttp

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/12/14 16:01
 * 描述：通用Subscriber
 */
class HttpSubsciber2<T>(callback: HttpCallback<T>) : Observer<T> {

    private var mCallback: HttpCallback<T>? = null

    init {
        mCallback = callback
    }

    override fun onSubscribe(d: Disposable) {
        //ignore
    }

    override fun onNext(value: T) {
        mCallback?.onSuccess(value)
    }

    override fun onError(it: Throwable) {
        when (it) {
            is CompositeException -> {
                it.exceptions.forEach { onError(it) }
            }
            is HttpException -> {
                mCallback?.onError(it.errorCode, it.errorMsg)
            }
            else -> {
                mCallback?.onError(it.message)
            }
        }
    }

    override fun onComplete() {
        //ignore
    }

}