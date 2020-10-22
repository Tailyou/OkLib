package com.hengda.zwf.commonsettings;

import android.content.Context;
import android.text.TextUtils;

import com.hengda.zwf.commonutil.FileUtil;
import com.hengda.zwf.commonutil.SDCardUtil;
import com.hengda.zwf.commonutil.SharedPrefUtil;

/**
 * 作者：Tailyou
 * 时间：2016/1/11 10:05
 * 邮箱：tailyou@163.com
 * 描述：恒达App配置文件
 */
public class SettingConfig {

    public static final String SHARED_PREF_SETTINGS = "SHARED_PREF_SETTINGS";
    public static final String AUTO_FLAG = "AUTO_FLAG";//自动讲解：0关闭，1开启
    public static final String AUTO_MODE = "AUTO_MODE";//讲解方式：0隔一，1连续
    public static final String STC_MODE = "ALARM_MODE";//报警方式：0直接报警，1间接报警
    public static final String RECEIVE_MODE = "RECEIVE_MODE";//收号方式：0蓝牙，1RFID，2混合
    public static final String ENERGY_MODE = "ENERGY_MODE";//节能模式：0关闭，1开启
    public static final String POWER_MODE = "POWER_MODE";//关机权限：0禁止，1允许
    public static final String POWER_PERMI = "POWER_PERMI";//禁止关机下是否获得关机权限：0未获得，1已获得
    public static final String IP_PORT = "IP_PORT";//服务器IP和端口
    public static final String PASSWORD = "PASSWORD";//管理员密码

    private static volatile SettingConfig instance = null;
    private static SharedPrefUtil settingConfig;

    private SettingConfig(Context context) {
        settingConfig = new SharedPrefUtil(context, SHARED_PREF_SETTINGS);
    }

    public static SettingConfig getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingConfig.class) {
                if (instance == null) {
                    instance = new SettingConfig(context);
                }
            }
        }
        return SettingConfig.instance;
    }

    public String getDeviceNo() {
        StringBuilder deviceNo = FileUtil.readStringFromFile(SDCardUtil.getSDCardPath() + "/DeviceNo.txt", "UTF-8");
        return TextUtils.isEmpty(deviceNo) ? SettingConstant.DEFAULT_DEVICE_NO : deviceNo.toString();
    }

    public void setDeviceNo(String deviceNo) {
        FileUtil.writeStringToFile(SDCardUtil.getSDCardPath() + "/DeviceNo.txt", deviceNo, false);
    }

    public String getPassword() {
        return settingConfig.getPrefString(PASSWORD, SettingConstant.DEFAULT_PWD);
    }

    public void setPassword(String password) {
        settingConfig.setPrefString(PASSWORD, password);
    }

    public int getAutoFlag() {
        return settingConfig.getPrefInt(AUTO_FLAG, 1);
    }

    public void setAutoFlag(int autoFlag) {
        settingConfig.setPrefInt(AUTO_FLAG, autoFlag);
    }

    public int getAutoMode() {
        return settingConfig.getPrefInt(AUTO_MODE, 0);
    }

    public void setAutoMode(int autoMode) {
        settingConfig.setPrefInt(AUTO_MODE, autoMode);
    }

    public int getSTCMode() {
        return settingConfig.getPrefInt(STC_MODE, 1);
    }

    public void setSTCMode(int flag) {
        settingConfig.setPrefInt(STC_MODE, flag);
    }

    public int getReceiveMode() {
        return settingConfig.getPrefInt(RECEIVE_MODE, 0);
    }

    public void setReceiveMode(int receiveNoMode) {
        settingConfig.setPrefInt(RECEIVE_MODE, receiveNoMode);
    }

    public int getEnergyMode() {
        return settingConfig.getPrefInt(ENERGY_MODE, 1);
    }

    public void setEnergyMode(int flag) {
        settingConfig.setPrefInt(ENERGY_MODE, flag);
    }

    public int getPowerMode() {
        return settingConfig.getPrefInt(POWER_MODE, 0);
    }

    public void setPowerMode(int flag) {
        settingConfig.setPrefInt(POWER_MODE, flag);
    }

    public int getPowerPermi() {
        return settingConfig.getPrefInt(POWER_PERMI, 0);
    }

    public void setPowerPermi(int flag) {
        settingConfig.setPrefInt(POWER_PERMI, flag);
    }

    public String getDefaultIpPort() {
        return settingConfig.getPrefString(IP_PORT);
    }

    public void setDefaultIpPort(String ipPort) {
        settingConfig.setPrefString(IP_PORT, ipPort);
    }

}
