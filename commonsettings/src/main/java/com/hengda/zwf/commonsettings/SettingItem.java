package com.hengda.zwf.commonsettings;

/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2017/2/17 11:44
 * 描述：导览机-设置项
 */
public class SettingItem {

    public static final int AUTO_FLAG = 0;//自动讲解：0关闭，1开启
    public static final int AUTO_MODE = 1;//自动模式：0隔一，1连续
    public static final int RECEIVE_MODE = 2;//感应模式：0蓝牙，1RFID
    public static final int ALARM_MODE = 3;//报警方式：0直接报警，1间接报警
    public static final int POWER_MODE = 4;//关机权限：0禁止，1允许
    public static final int ENERGY_MODE = 5;//节能模式：0关闭，1开启

    private String name;
    private int resId;
    private int type;

    public SettingItem(String name, int resId, int type) {
        this.name = name;
        this.resId = resId;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
