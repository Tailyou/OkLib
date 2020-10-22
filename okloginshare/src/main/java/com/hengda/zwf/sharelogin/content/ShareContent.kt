package com.hengda.zwf.sharelogin.content

import java.io.Serializable

/**
 * 分享类型-网页
 * @time 2017/6/6 16:29
 */
class ShareContent(val title: String,
                   val text: String,
                   val url: String,
                   val largeImgPath: String,
                   val thumbImgBytes: ByteArray) : Serializable