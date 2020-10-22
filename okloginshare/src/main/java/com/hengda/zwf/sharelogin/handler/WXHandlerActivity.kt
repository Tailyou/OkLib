package com.hengda.zwf.sharelogin.handler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hengda.zwf.sharelogin.ILoginListener
import com.hengda.zwf.sharelogin.IShareListener
import com.hengda.zwf.sharelogin.ShareLoginClient
import com.hengda.zwf.sharelogin.ShareLoginConfig
import com.hengda.zwf.sharelogin.content.ShareContent
import com.hengda.zwf.sharelogin.type.SharePlatform
import com.hengda.zwf.sharelogin.uinfo.AuthUserInfoHttp
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXHandlerActivity : Activity(), IWXAPIEventHandler {

    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, ShareLoginConfig.weiXinAppId, true)
        api.registerApp(ShareLoginConfig.weiXinAppId)
        when (intent.action) {
            ShareLoginClient.ACTION_LOGIN -> doLogin(this)
            ShareLoginClient.ACTION_SHARE -> {
                val shareContent = intent.extras.get(ShareLoginClient.SHARE_CONTENT) as ShareContent
                val sharePlatform = intent.extras.getString(ShareLoginClient.SHARE_PLATFORM)
                doShare(this, shareContent, sharePlatform)
            }
        }
        api.handleIntent(intent, this)
        finish()
    }

    /**
     * 登录
     * @time 2017/6/6 13:46
     */
    private fun doLogin(context: Context) {
        val appId = ShareLoginConfig.weiXinAppId
        val api = WXAPIFactory.createWXAPI(context.applicationContext, appId, true)
        api.registerApp(appId)
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        api.sendReq(req)
    }

    /**
     * 分享
     * @time 2017/6/6 14:57
     */
    private fun doShare(context: Context, shareContent: ShareContent, @SharePlatform shareType: String) {
        val appId = ShareLoginConfig.weiXinAppId
        val api = WXAPIFactory.createWXAPI(context.applicationContext, appId, true)
        api.registerApp(appId)
        api.sendReq(setupShareRequest(shareContent, shareType))
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        api.handleIntent(getIntent(), this)
        finish()
    }

    override fun onReq(baseReq: BaseReq) {
        finish()
    }

    override fun onResp(baseResp: BaseResp) {
        if (baseResp is SendAuth.Resp && baseResp.type == 1) {
            parseLoginResp(baseResp, ShareLoginClient.sLoginListener)
        } else {
            parseShareResp(baseResp, ShareLoginClient.sShareListener)
        }
        finish()
    }


    /**
     * 组装分享请求
     * @time 2017/6/7 15:23
     */
    private fun setupShareRequest(shareContent: ShareContent, @SharePlatform shareType: String): SendMessageToWX.Req {
        val req = SendMessageToWX.Req()
        req.transaction = System.currentTimeMillis().toString()
        req.message = setupMessage(shareContent)
        when (shareType) {
            SharePlatform.WEIXIN_FRIEND -> req.scene = SendMessageToWX.Req.WXSceneSession
            SharePlatform.WEIXIN_FRIEND_ZONE -> req.scene = SendMessageToWX.Req.WXSceneTimeline
            SharePlatform.WEIXIN_FAVORITE -> req.scene = SendMessageToWX.Req.WXSceneFavorite
        }
        return req
    }

    /**
     * 组装分享内容
     * @time 2017/6/7 15:23
     */
    private fun setupMessage(shareContent: ShareContent): WXMediaMessage {
        val webPage = WXWebpageObject()
        webPage.webpageUrl = shareContent.url
        val msg = WXMediaMessage(webPage)
        msg.title = shareContent.title
        msg.description = shareContent.text
        msg.thumbData = shareContent.thumbImgBytes
        if (!msg.mediaObject.checkArgs()) {
            throw IllegalArgumentException("分享信息的参数类型不正确")
        }
        return msg
    }

    /**
     * 解析登录响应
     * @time 2017/6/7 14:51
     */
    private fun parseLoginResp(resp: SendAuth.Resp, listener: ILoginListener?) {
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> AuthUserInfoHttp.reqAccessTokenByCode(resp.code, listener)
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                listener?.onCancel()
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                listener?.onError("用户拒绝授权")
            }
            else -> listener?.onError("未知错误")
        }
    }

    /**
     * 解析分享响应
     * @time 2017/6/7 14:55
     */
    private fun parseShareResp(resp: BaseResp, listener: IShareListener?) {
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> listener?.onSuccess()
            BaseResp.ErrCode.ERR_USER_CANCEL -> listener?.onCancel()
            BaseResp.ErrCode.ERR_AUTH_DENIED -> listener?.onError("用户拒绝授权")
            BaseResp.ErrCode.ERR_SENT_FAILED -> listener?.onError("发送失败")
            BaseResp.ErrCode.ERR_COMM -> listener?.onError("一般错误")
            else -> listener?.onError("未知错误")
        }
    }

}
