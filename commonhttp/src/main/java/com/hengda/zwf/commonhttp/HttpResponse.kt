package com.hengda.zwf.commonhttp

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/2/15 14:06
 * 描述：服务器响应数据格式
 */
class HttpResponse<T>(var status: String, var msg: String, var data: T?)