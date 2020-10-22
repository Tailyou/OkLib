package com.hengda.tailyoulibrary

import android.support.multidex.MultiDexApplication
import com.hengda.zwf.sharelogin.ShareLoginClient
import com.hengda.zwf.sharelogin.ShareLoginConfig

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initShareLoginClient()
    }

    /**
     * 初始化 ShareLoginClient
     * @time 2017/6/6 13:40
     */
    private fun initShareLoginClient() {
        val slc = ShareLoginConfig.Builder()
                .debug(true)
                .appName(getString(R.string.app_name))
                .qq("1107152377", "all")
                .weiXin("wxf5cee76aadce9dd7", "412995e8265988c0356bea70d90d7a0c")
                .weiBo("90405246", "http://sns.whalecloud.com/sina2/callback", "all")
                .build()
        ShareLoginClient.init(slc)
    }

}
