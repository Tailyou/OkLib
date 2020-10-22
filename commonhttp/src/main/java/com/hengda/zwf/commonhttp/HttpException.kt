package com.hengda.zwf.commonhttp

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/2/15 14:06
 * 描述：自定义网络请求异常
 */
class HttpException(var errorCode: String, var errorMsg: String) : RuntimeException(errorMsg)
