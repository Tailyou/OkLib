package com.hengda.zwf.commonhttp

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/12/15 9:36
 * 描述：结合CommonSubscriber使用
 */
open class HttpCallback<in T> {

    open fun onSuccess(t: T) {}

    open fun onError(vararg msg: String?) {}

}