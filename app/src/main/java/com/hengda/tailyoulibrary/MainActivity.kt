package com.hengda.tailyoulibrary

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hengda.zwf.sharelogin.ILoginListener
import com.hengda.zwf.sharelogin.IShareListener
import com.hengda.zwf.sharelogin.ShareLoginClient
import com.hengda.zwf.sharelogin.content.ShareContent
import com.hengda.zwf.sharelogin.type.LoginPlatform
import com.hengda.zwf.sharelogin.type.SharePlatform
import com.hengda.zwf.sharelogin.uinfo.AuthUserInfoBean
import com.hengda.zwf.sharelogin.uinfo.AuthUserInfoClient
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private lateinit var mBitmap: Bitmap
    private lateinit var mThumbBmpBytes: ByteArray
    private lateinit var mShareContent: ShareContent
    lateinit var mActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActivity = this@MainActivity
        object : Thread() {
            override fun run() {
                super.run()
                mBitmap = (ContextCompat.getDrawable(mActivity, R.drawable.large) as BitmapDrawable).bitmap
                mThumbBmpBytes = BitmapUtil.bitmap2ByteArray(mBitmap)!!
                mShareContent = ShareContent("这是标题，好像不够长",
                        "我感觉这是个神奇的问题，昨天项目还一切OK",
                        "https://segmentfault.com/q/1010000009688458",
                        "http://oksdjdocc.bkt.clouddn.com/17-7-18/76493090.jpg",
                        mThumbBmpBytes)
            }
        }.start()
    }

    fun doShareLogin(view: View) {
        when (view.id) {
            R.id.btnQQLogin -> if (ShareLoginClient.isQQInstalled(this)) doLogin(LoginPlatform.QQ)
            R.id.btnQQShareZone -> if (ShareLoginClient.isQQInstalled(this)) doShare(SharePlatform.QQ_ZONE)
            R.id.btnQQShareFriend -> if (ShareLoginClient.isQQInstalled(this)) doShare(SharePlatform.QQ_FRIEND)
            R.id.btnWBLogin -> if (ShareLoginClient.isWBInstalled(this)) doLogin(LoginPlatform.WB)
            R.id.btnWBShare -> if (ShareLoginClient.isWBInstalled(this)) doShare(SharePlatform.WEIBO_TIME_LINE)
            R.id.btnWXLogin -> if (ShareLoginClient.isWXInstalled(this)) doLogin(LoginPlatform.WX)
            R.id.btnWXShareFriend -> if (ShareLoginClient.isWXInstalled(this)) doShare(SharePlatform.WEIXIN_FRIEND)
            R.id.btnWXShareFavorite -> if (ShareLoginClient.isWXInstalled(this)) doShare(SharePlatform.WEIXIN_FAVORITE)
            R.id.btnWXShareFriendZone -> if (ShareLoginClient.isWXInstalled(this)) doShare(SharePlatform.WEIXIN_FRIEND_ZONE)
        }
    }

    //分享
    private fun doShare(platform: String) {
        ShareLoginClient.share(mActivity, platform, mShareContent, object : IShareListener {
            override fun onSuccess() {
                toast("share success")
            }

            override fun onCancel() {
                toast("share cancel")
            }

            override fun onError(msg: String?) {
                toast("share error")
            }
        })
    }

    //登录
    private fun doLogin(platform: String) {
        ShareLoginClient.login(mActivity, platform, object : ILoginListener {
            override fun onSuccess(accessToken: String, uId: String, expiresIn: Long) {
                loadUserInfo(platform, accessToken, uId)
                toast("$accessToken,$uId")
            }

            override fun onError(errorMsg: String?) {
                toast("login error:$errorMsg")
            }

            override fun onCancel() {
                toast("login cancel")
            }
        })
    }

    //获取三方账号信息
    private fun loadUserInfo(platform: String, accessToken: String, uId: String) {
        AuthUserInfoClient.getUserInfo(platform, accessToken, uId, object : AuthUserInfoClient.UserInfoCallback {
            override fun onSuccess(authUserInfoBean: AuthUserInfoBean) {
                toast(authUserInfoBean.toString())
            }

            override fun onError(msg: String) {
                toast(msg)
            }
        })
    }

}
