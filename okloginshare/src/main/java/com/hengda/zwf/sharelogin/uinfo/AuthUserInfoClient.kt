package com.hengda.zwf.sharelogin.uinfo

import com.hengda.zwf.sharelogin.ShareLoginConfig
import com.hengda.zwf.sharelogin.type.LoginPlatform

object AuthUserInfoClient {

    fun getUserInfo(@LoginPlatform type: String, accessToken: String, uid: String, callback: UserInfoCallback?) {
        when (type) {
            LoginPlatform.QQ -> getQQUserInfo(accessToken, uid, callback)
            LoginPlatform.WX -> getWXUserInfo(accessToken, uid, callback)
            LoginPlatform.WB -> getWBUserInfo(accessToken, uid, callback)
        }
    }

    private fun getQQUserInfo(accessToken: String, uid: String, callback: UserInfoCallback?) {
        AuthUserInfoHttp.reqQQUserInfo(accessToken, uid, ShareLoginConfig.qqAppId, callback)
    }

    private fun getWXUserInfo(accessToken: String, uid: String, callback: UserInfoCallback?) {
        AuthUserInfoHttp.reqWXUserInfo(accessToken, uid, callback)
    }

    private fun getWBUserInfo(accessToken: String, uid: String, callback: UserInfoCallback?) {
        AuthUserInfoHttp.reqWBUserInfo(accessToken, uid, callback)
    }

    interface UserInfoCallback {
        fun onSuccess(authUserInfoBean: AuthUserInfoBean)

        fun onError(msg: String)
    }

}
