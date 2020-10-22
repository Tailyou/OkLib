package com.hengda.zwf.commonsettings;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hengda.zwf.commonadapter.LCommonAdapter;
import com.hengda.zwf.commonadapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Tailyou
 * 时间：2016/1/14 16:45
 * 邮箱：tailyou@163.com
 * 描述：导览机通用设置辅助类
 */
public class SettingHelper {

    private static volatile SettingHelper instance = null;
    private static SettingConfig settingConfig;
    private LCommonAdapter<SubItem> adapter = null;

    private SettingHelper(Context context) {
        settingConfig = settingConfig.getInstance(context);
    }

    public static SettingHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingHelper.class) {
                if (instance == null) {
                    instance = new SettingHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 根据类型返回ListView，在Dialog中显示
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:10
     */
    public ListView buildSettingSubItemListView(Context context, final int setType) {
        ListView listView = new ListView(context);
        listView.setAdapter(adapter = new LCommonAdapter<SubItem>(context, R.layout.item_setting_sub_item, initSubItemData(setType)) {
            @Override
            public void convert(ViewHolder holder, SubItem subItem) {
                holder.setText(R.id.txtName, subItem.getName());
                switch (setType) {
                    case SettingItem.AUTO_FLAG:
                        holder.setImageResource(R.id.ivSelectStatus, subItem.getCode() == settingConfig.getAutoFlag() ? R.mipmap.selected : R.mipmap.unselected);
                        break;
                    case SettingItem.AUTO_MODE:
                        holder.setImageResource(R.id.ivSelectStatus, subItem.getCode() == settingConfig.getAutoMode() ? R.mipmap.selected : R.mipmap.unselected);
                        break;
                    case SettingItem.RECEIVE_MODE:
                        holder.setImageResource(R.id.ivSelectStatus, subItem.getCode() == settingConfig.getReceiveMode() ? R.mipmap.selected : R.mipmap.unselected);
                        break;
                    case SettingItem.ALARM_MODE:
                        holder.setImageResource(R.id.ivSelectStatus, subItem.getCode() == settingConfig.getSTCMode() ? R.mipmap.selected : R.mipmap.unselected);
                        break;
                    case SettingItem.POWER_MODE:
                        holder.setImageResource(R.id.ivSelectStatus, subItem.getCode() == settingConfig.getPowerMode() ? R.mipmap.selected : R.mipmap.unselected);
                        break;
                    case SettingItem.ENERGY_MODE:
                        holder.setImageResource(R.id.ivSelectStatus, subItem.getCode() == settingConfig.getEnergyMode() ? R.mipmap.selected : R.mipmap.unselected);
                        break;
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubItem subItem = (SubItem) parent.getItemAtPosition(position);
                switch (setType) {
                    case SettingItem.AUTO_FLAG:
                        settingConfig.setAutoFlag(subItem.getCode());
                        break;
                    case SettingItem.AUTO_MODE:
                        settingConfig.setAutoMode(subItem.getCode());
                        break;
                    case SettingItem.RECEIVE_MODE:
                        settingConfig.setReceiveMode(subItem.getCode());
                        break;
                    case SettingItem.ALARM_MODE:
                        settingConfig.setSTCMode(subItem.getCode());
                        break;
                    case SettingItem.POWER_MODE:
                        settingConfig.setPowerMode(subItem.getCode());
                        settingConfig.setPowerPermi(0);
                        break;
                    case SettingItem.ENERGY_MODE:
                        settingConfig.setEnergyMode(subItem.getCode());
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
        return listView;
    }

    /**
     * 根据类型，初始化设置数据
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:10
     */
    private List<SubItem> initSubItemData(int settingType) {
        List<SubItem> datas = new ArrayList<>();
        switch (settingType) {
            case SettingItem.AUTO_FLAG:
                datas.add(new SubItem("关闭", 0));
                datas.add(new SubItem("开启", 1));
                break;
            case SettingItem.AUTO_MODE:
                datas.add(new SubItem("隔一讲解", 0));
                datas.add(new SubItem("连续讲解", 1));
                break;
            case SettingItem.RECEIVE_MODE:
                datas.add(new SubItem("蓝牙感应", 0));
                datas.add(new SubItem("RFID感应", 1));
                break;
            case SettingItem.ALARM_MODE:
                datas.add(new SubItem("间接报警", 0));
                datas.add(new SubItem("直接报警", 1));
                break;
            case SettingItem.POWER_MODE:
                datas.add(new SubItem("禁止关机", 0));
                datas.add(new SubItem("允许关机", 1));
                break;
            case SettingItem.ENERGY_MODE:
                datas.add(new SubItem("关闭", 0));
                datas.add(new SubItem("开启", 1));
                break;
        }
        return datas;
    }

    /**
     * 销毁Adapter
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:14
     */
    public void destroyAdapter() {
        if (adapter != null) {
            adapter = null;
        }
    }

    /**
     * 设置项-子项
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:14
     */
    static class SubItem {
        private String name;
        private int code;

        public SubItem(String name, int code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }


        public int getCode() {
            return code;
        }
    }

}
