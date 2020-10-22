package com.hengda.zwf.sharelogin.uinfo

import android.os.Handler
import android.os.Looper
import com.hengda.zwf.sharelogin.ILoginListener
import com.hengda.zwf.sharelogin.ShareLoginConfig
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2018/6/22 8:40
 * 描述：
 */
object AuthUserInfoHttp {

    private var mainExecutor = object : Handler(Looper.getMainLooper()) {}
    private var threadPool = Executors.newCachedThreadPool()

    //获取QQ用户信息
    fun reqQQUserInfo(accessToken: String, openid: String, appId: String, callback: AuthUserInfoClient.UserInfoCallback?) {
        threadPool.execute {
            try {
                val url = URL("https://graph.qq.com/user/get_simple_userinfo?" +
                        "access_token=$accessToken&" +
                        "openid=$openid&" +
                        "oauth_consumer_key=$appId&" +
                        "format=json")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connect()
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                var line: String? = null
                val stringBuffer = StringBuffer()
                while ((bufferedReader.readLine().also { line = it }) != null) {
                    line = String(line!!.toByteArray(charset("UTF-8")))
                    stringBuffer.append(line)
                }
                var jsonObject = JSONObject(stringBuffer.toString())
                var nickname = jsonObject.getString("nickname")
                var sex = jsonObject.getString("gender")
                var headImgUrl = jsonObject.getString("figureurl_qq_1")
                mainExecutor.post { callback?.onSuccess(AuthUserInfoBean(openid, nickname, sex, headImgUrl)) }
                bufferedReader.close()
                httpURLConnection.disconnect()
            } catch (e: Throwable) {
                e.printStackTrace()
                mainExecutor.post { callback?.onError(e.message!!) }
            }
        }
    }

    //获取微信用户信息
    fun reqWXUserInfo(accessToken: String, openid: String, callback: AuthUserInfoClient.UserInfoCallback?) {
        threadPool.execute {
            try {
                val url = URL("https://api.weixin.qq.com/sns/userinfo?" +
                        "access_token=$accessToken&" +
                        "openid=$openid")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connect()
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                var line: String? = null
                val stringBuffer = StringBuffer()
                while ((bufferedReader.readLine().also { line = it }) != null) {
                    line = String(line!!.toByteArray(charset("UTF-8")))
                    stringBuffer.append(line)
                }
                var jsonObject = JSONObject(stringBuffer.toString())
                var userId = jsonObject.getString("unionid")
                var nickname = jsonObject.getString("nickname")
                var sex = jsonObject.getString("sex")
                var headImgUrl = jsonObject.getString("headimgurl")
                mainExecutor.post { callback?.onSuccess(AuthUserInfoBean(userId, nickname, sex, headImgUrl)) }
                bufferedReader.close()
                httpURLConnection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                mainExecutor.post { callback?.onError(e.message!!) }
            }
        }
    }

    //微信-根据登录成功后的code获取token
    fun reqAccessTokenByCode(code: String, listener: ILoginListener?) {
        threadPool.execute {
            try {
                val url = URL("https://api.weixin.qq.com/sns/oauth2/access_token?" +
                        "appid=${ShareLoginConfig.weiXinAppId}&" +
                        "secret=${ShareLoginConfig.weiXinSecret}&" +
                        "grant_type=authorization_code&" +
                        "code=$code")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connect()
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                var line: String? = null
                val stringBuffer = StringBuffer()
                while ((bufferedReader.readLine().also { line = it }) != null) {
                    line = String(line!!.toByteArray(charset("UTF-8")))
                    stringBuffer.append(line)
                }
                var jsonObject = JSONObject(stringBuffer.toString())
                var token = jsonObject.getString("access_token")
                var openid = jsonObject.getString("openid")
                var expiresIn = jsonObject.getLong("expires_in")
                mainExecutor.post { listener?.onSuccess(token, openid, expiresIn) }
                bufferedReader.close()
                httpURLConnection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                mainExecutor.post { listener?.onError(e.message!!) }
            }
        }
    }

    //获取微博用户信息
    fun reqWBUserInfo(accessToken: String, uid: String, callback: AuthUserInfoClient.UserInfoCallback?) {
        threadPool.execute {
            try {
                val url = URL("https://api.weibo.com/2/users/show.json?" +
                        "access_token=$accessToken&" +
                        "uid=$uid")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connect()
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                var line: String? = null
                val stringBuffer = StringBuffer()
                while ((bufferedReader.readLine().also { line = it }) != null) {
                    line = String(line!!.toByteArray(charset("UTF-8")))
                    stringBuffer.append(line)
                }
                var jsonObject = JSONObject(stringBuffer.toString())
                var userId = jsonObject.getString("id")
                var nickname = jsonObject.getString("screen_name")
                var sex = jsonObject.getString("gender")
                var headImgUrl = jsonObject.getString("avatar_large")
                mainExecutor.post { callback?.onSuccess(AuthUserInfoBean(userId, nickname, sex, headImgUrl)) }
                bufferedReader.close()
                httpURLConnection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                mainExecutor.post { callback?.onError(e.message!!) }
            }
        }
    }

}
