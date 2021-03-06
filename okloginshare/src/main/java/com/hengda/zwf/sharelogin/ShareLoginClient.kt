package com.hengda.zwf.sharelogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.hengda.zwf.sharelogin.content.ShareContent
import com.hengda.zwf.sharelogin.handler.QQHandlerActivity
import com.hengda.zwf.sharelogin.handler.WBHandlerActivity
import com.hengda.zwf.sharelogin.handler.WXHandlerActivity
import com.hengda.zwf.sharelogin.type.LoginPlatform
import com.hengda.zwf.sharelogin.type.LoginPlatform.*
import com.hengda.zwf.sharelogin.type.SharePlatform
import com.hengda.zwf.sharelogin.type.SharePlatform.*
import com.sina.weibo.sdk.utils.LogUtil
import java.security.InvalidParameterException
import java.util.*

object ShareLoginClient {

    const val SHARE_CONTENT = "SHARE_CONTENT"
    const val SHARE_PLATFORM = "SHARE_PLATFORM"
    const val ACTION_LOGIN = "ACTION_LOGIN"
    const val ACTION_SHARE = "ACTION_SHARE"

    var sLoginListener: ILoginListener? = null
    var sShareListener: IShareListener? = null

    fun init(slc: ShareLoginConfig) {
        if (slc.isDebug) {
            LogUtil.enableLog()
        } else {
            LogUtil.disableLog()
        }
    }

    fun isQQInstalled(context: Context): Boolean {
        return isInstalled(context, "com.tencent.mobileqq")
    }

    fun isWBInstalled(context: Context): Boolean {
        return isInstalled(context, "com.sina.weibo")
    }

    fun isWXInstalled(context: Context): Boolean {
        return isInstalled(context, "com.tencent.mm")
    }

    /**
     * 根据包名判断是否安装
     * @time 2017/6/6 11:09
     */
    private fun isInstalled(context: Context, pkgName: String): Boolean {
        val pm = context.applicationContext.packageManager ?: return false
        val packages = pm.getInstalledPackages(0)
        return packages.map { it.packageName.toLowerCase(Locale.ENGLISH) }.contains(pkgName)
    }

    /**
     * 第三方登录
     * @time 2017/6/6 13:52
     */
    fun login(activity: Activity, @LoginPlatform type: String, loginListener: ILoginListener?) {
        ShareLoginClient.sLoginListener = loginListener
        when (type) {
            QQ -> toLogin(activity, QQHandlerActivity::class.java)
            WB -> toLogin(activity, WBHandlerActivity::class.java)
            WX -> toLogin(activity, WXHandlerActivity::class.java)
        }
    }

    /**
     * 跳转->第三方登录
     * @time 2017/6/7 9:50
     */
    private fun toLogin(activity: Activity, cls: Class<out Activity>) {
        val intent = Intent(activity, cls)
        intent.action = ACTION_LOGIN
        activity.startActivity(intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * 第三方分享
     * @time 2017/6/7 9:51
     */
    fun share(activity: Activity, @SharePlatform sharePlatform: String, shareContent: ShareContent, shareListener: IShareListener?) {
        ShareLoginClient.sShareListener = shareListener
        val cls = when (sharePlatform) {
            WEIBO_TIME_LINE -> WBHandlerActivity::class.java
            QQ_FRIEND, QQ_ZONE -> QQHandlerActivity::class.java
            WEIXIN_FRIEND, WEIXIN_FRIEND_ZONE, WEIXIN_FAVORITE -> WXHandlerActivity::class.java
            else -> {
                throw InvalidParameterException()
            }
        }
        toShare(activity, sharePlatform, shareContent, cls)
    }

    /**
     * 跳转->第三方分享
     * @time 2017/6/7 9:57
     */
    private fun toShare(activity: Activity, sharePlatform: String, shareContent: ShareContent, cls: Class<out Activity>) {
        val intent = Intent(activity, cls)
        intent.action = ACTION_SHARE
        intent.putExtra(SHARE_PLATFORM, sharePlatform)
        intent.putExtra(SHARE_CONTENT, shareContent)
        activity.startActivity(intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}
