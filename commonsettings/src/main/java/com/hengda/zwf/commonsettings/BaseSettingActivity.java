package com.hengda.zwf.commonsettings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.zwf.commonadapter.LCommonAdapter;
import com.hengda.zwf.commonadapter.ViewHolder;
import com.hengda.zwf.commondialog.util.DialogClickListener;
import com.hengda.zwf.commonutil.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseSettingActivity extends AppCompatActivity {

    ImageView ivBack;
    TextView tvTitle;
    ListView listView;

    Context mContext;
    SettingHelper mSettingHelper;
    SettingConfig mSettingConfig;
    List<SettingItem> mSettingItems = new ArrayList<>();
    LCommonAdapter<SettingItem> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        mSettingHelper = SettingHelper.getInstance(mContext);
        mSettingConfig = SettingConfig.getInstance(mContext);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        listView = (ListView) findViewById(R.id.listView);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("设置");
        initSettingData();
        initSettingUI();
    }

    /**
     * 初始化设置数据
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 8:37
     */
    private void initSettingData() {
        String[] settingNames = getResources().getStringArray(R.array.settings);
        Integer[] settingIcons = {R.mipmap.ic_setting_zdjj,
                R.mipmap.ic_setting_zdms,
                R.mipmap.ic_setting_gyms,
                R.mipmap.ic_setting_bjms,
                R.mipmap.ic_setting_gzgj,
                R.mipmap.ic_setting_jnms,
                R.mipmap.ic_setting_glymm,
                R.mipmap.ic_setting_fwqsz,
                R.mipmap.ic_setting_sbbh,
                R.mipmap.ic_setting_gcms,
                R.mipmap.ic_setting_wlsz,
                R.mipmap.ic_setting_cspt,
                R.mipmap.ic_setting_bbxx,
                R.mipmap.ic_setting_jcgx,
                R.mipmap.ic_setting_sczy,
                R.mipmap.ic_setting_zygx};
        for (int i = 0; i < settingIcons.length; i++) {
            mSettingItems.add(new SettingItem(settingNames[i], settingIcons[i], i));
        }
    }

    /**
     * 初始化Settings ListView
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 12:14
     */
    private void initSettingUI() {
        listView.setAdapter(mAdapter = new LCommonAdapter<SettingItem>(mContext, R.layout.item_setting_item, mSettingItems) {
            @Override
            public void convert(ViewHolder holder, SettingItem settingItem) {
                String append = settingItem.getType() == 12 ? "-ver " + AppUtil.getVersionName(mContext) : "";
                holder.setText(R.id.txtName, settingItem.getName() + append);
                holder.setImageResource(R.id.imgIcon, settingItem.getResId());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingItem settingItem = (SettingItem) parent.getItemAtPosition(position);
                switch (settingItem.getType()) {
                    case 0://自动讲解：开、关
                    case 1://自动模式：隔一、连续
                    case 2://感应模式：蓝牙、RFID
                    case 3://报警模式：直接、间接
                    case 4://公众关机：禁止、允许
                    case 5://节能模式：开、关
                        showSelectDialog(settingItem);
                        break;
                    case 6://管理员密码
                    case 7://服务器地址
                    case 8://设备编号
                        showEditDialog(settingItem);
                        break;
                    case 9://工程模式
                        openEngineeringMode();
                        break;
                    case 10://网络设置
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                        break;
                    case 11://测试平台
                        openTestPlatform();
                        break;
                    case 13://检测更新
                        checkUpdate();
                        break;
                    case 14://清除资源
                        deleteRes();
                        break;
                    case 15://下载资源
                        loadRes();
                        break;
                }
            }
        });
    }

    /**
     * 显示选择类设置弹窗
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:52
     */
    private void showSelectDialog(SettingItem settingItem) {
        ListView customView = mSettingHelper.buildSettingSubItemListView(mContext, settingItem.getType());
        HDialogCenter.showDialog(mContext, customView, new DialogClickListener() {
            @Override
            public void p() {
                mSettingHelper.destroyAdapter();
                HDialogCenter.hideDialog();
            }
        }, new String[]{settingItem.getName(), "确定"});
    }

    /**
     * 显示输入类设置弹窗
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:53
     */
    private void showEditDialog(final SettingItem settingItem) {
        View root = View.inflate(mContext, R.layout.dialog_custom_view_edt, null);
        final EditText edtInput = (EditText) root.findViewById(R.id.editText);
        switch (settingItem.getType()) {
            case 6:
                edtInput.setText(mSettingConfig.getPassword());
                break;
            case 7:
                edtInput.setText(mSettingConfig.getDefaultIpPort());
                break;
            case 8:
                edtInput.setText(mSettingConfig.getDeviceNo());
                break;
        }
        Selection.setSelection(edtInput.getText(), edtInput.getText().length());
        HDialogCenter.showDialog(mContext, root, new DialogClickListener() {
                    @Override
                    public void p() {
                        String input = edtInput.getText().toString();
                        switch (settingItem.getType()) {
                            case 6:
                                confirmPassword(input);
                                break;
                            case 7:
                                confirmServerIP(input);
                                break;
                            case 8:
                                confirmDeviceNo(input);
                                break;
                        }
                    }
                },
                new String[]{settingItem.getName(), "确定"});
    }

    protected abstract void openEngineeringMode();

    /**
     * 启动测试平台
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:22
     */
    private void openTestPlatform() {
        if (AppUtil.checkInstallByPkgName(mContext, SettingConstant.TEST_APP_PACKAGE_NAME)) {
            Intent intent = new Intent();
            intent.setClassName(SettingConstant.TEST_APP_PACKAGE_NAME, SettingConstant.TEST_APP_LAUNCHER_ACT);
            startActivity(intent);
        } else {
            showShort(mContext, "请先安装测试程序！");
        }
    }

    protected abstract void checkUpdate();

    protected abstract void deleteRes();

    protected abstract void loadRes();

    private void confirmPassword(String password) {
        if (isPassword(password)) {
            mSettingConfig.setPassword(password);
            HDialogCenter.hideDialog();
        } else {
            showShort(mContext, "格式错误，请输入1-8位数字！");
        }
    }

    private void confirmServerIP(String ipPort) {
        if (TextUtils.isEmpty(ipPort)) {
            showShort(mContext, "请输入服务器地址");
        } else {
            HDialogCenter.hideDialog();
            if (!TextUtils.equals(mSettingConfig.getDefaultIpPort(), ipPort)) {
                mSettingConfig.setDefaultIpPort(ipPort);
                modifyIpPortSucceed(ipPort);
            }
        }
    }

    private void confirmDeviceNo(String deviceNo) {
        if (TextUtils.isEmpty(deviceNo)) {
            showShort(mContext, "请输入设备号");
        } else {
            if (!TextUtils.equals(mSettingConfig.getDeviceNo(), deviceNo)) {
                mSettingConfig.setDeviceNo(deviceNo);
                modifyDeviceNoSucceed(deviceNo);
            }
            HDialogCenter.hideDialog();
        }
    }

    /**
     * Toast提示
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:08
     */
    private void showShort(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 验证密码格式
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/2/17 11:53
     */
    private boolean isPassword(String str) {
        Pattern pattern = Pattern.compile("^\\d{1,8}$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    protected abstract void modifyIpPortSucceed(String ipPort);

    protected abstract void modifyDeviceNoSucceed(String deviceNo);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        listView = null;
    }

}
