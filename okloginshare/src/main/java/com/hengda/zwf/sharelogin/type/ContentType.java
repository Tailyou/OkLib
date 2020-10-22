package com.hengda.zwf.sharelogin.type;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 分享内容类型（1：文本，2：图片，3：网页）
 * @time 2017/6/6 18:32
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ContentType.TXT, ContentType.PIC, ContentType.WEB})
public @interface ContentType {
    int TXT = 1, PIC = 2, WEB = 3;
}