package com.hengda.zwf.sharelogin.handler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hengda.zwf.sharelogin.ILoginListener
import com.hengda.zwf.sharelogin.IShareListener
import com.hengda.zwf.sharelogin.ShareLoginClient
import com.hengda.zwf.sharelogin.ShareLoginConfig
import com.hengda.zwf.sharelogin.content.ShareContent
import com.hengda.zwf.sharelogin.type.SharePlatform
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

class QQHandlerActivity : Activity() {

    private lateinit var mUIListener: IUiListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null && !intent.action.isNullOrBlank()) {
            when (intent.action) {
                ShareLoginClient.ACTION_LOGIN -> doLogin(ShareLoginClient.sLoginListener)
                ShareLoginClient.ACTION_SHARE -> {
                    val shareContent = intent.extras.get(ShareLoginClient.SHARE_CONTENT) as ShareContent
                    val sharePlatform = intent.extras.getString(ShareLoginClient.SHARE_PLATFORM)
                    doShare(sharePlatform, shareContent, ShareLoginClient.sShareListener)
                }
            }
        }
    }

    /**
     * 登录
     * @param loginListener
     * @time 2017/6/6 13:46
     */
    private fun doLogin(loginListener: ILoginListener?) {
        mUIListener = object : IUiListener {
            override fun onComplete(p0: Any?) {
                try {
                    val jsonObject = p0 as JSONObject
                    val token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
                    val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
                    val expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
                    loginListener?.onSuccess(token, openId, java.lang.Long.valueOf(expires)!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancel() {
                loginListener?.onCancel()
            }

            override fun onError(p0: UiError?) {
                loginListener?.onError(p0?.errorMessage)
            }
        }
        val tencent = Tencent.createInstance(ShareLoginConfig.qqAppId, this.applicationContext)
        if (!tencent.isSessionValid) {
            tencent.login(this, ShareLoginConfig.qqScope, mUIListener)
        } else {
            tencent.logout(this)
        }
    }

    /**
     * 分享
     * @time 2017/6/6 14:57
     */
    private fun doShare(sharePlatform: String, shareContent: ShareContent, shareListener: IShareListener?) {
        mUIListener = object : IUiListener {
            override fun onComplete(p0: Any?) {
                shareListener?.onSuccess()
            }

            override fun onCancel() {
                shareListener?.onCancel()
            }

            override fun onError(p0: UiError?) {
                shareListener?.onError(p0?.errorMessage)
            }
        }
        val tencent = Tencent.createInstance(ShareLoginConfig.qqAppId, applicationContext)
        if (sharePlatform == SharePlatform.QQ_FRIEND) {
            tencent.shareToQQ(this, setupQQWebBundle(shareContent), mUIListener)
        } else {
            tencent.shareToQzone(this, setupQzoneWebBundle(shareContent), mUIListener)
        }
    }

    /**
     * QQ好友-网页（图文）
     * @time 2017/6/6 15:30
     */
    private fun setupQQWebBundle(shareContent: ShareContent): Bundle {
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.title)
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.text)
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.url)
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareContent.largeImgPath)
        return params
    }

    /**
     * QQ空间-网页（图文）
     * @time 2017/6/7 12:12
     */
    private fun setupQzoneWebBundle(shareContent: ShareContent): Bundle {
        val params = Bundle()
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.title)
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent.text)
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareContent.url)
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arrayListOf(shareContent.largeImgPath))
        return params
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mUIListener != null) {
            Tencent.handleResultData(data, mUIListener)
        }
        finish()
    }

}
