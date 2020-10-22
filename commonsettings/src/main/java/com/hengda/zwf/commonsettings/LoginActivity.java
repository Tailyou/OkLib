package com.hengda.zwf.commonsettings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录界面（获取关机权限，获取设置权限）
 *
 * @author 祝文飞（Tailyou）
 * @time 2016/11/30 11:32
 */
public class LoginActivity extends AppCompatActivity {

    public final static String ACTION_ADMIN = "ACTION_ADMIN";
    public final static String ACTION_POWER = "ACTION_POWER";

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvInput;
    private HImageView ivNumZero, ivNumOne, ivNumTwo, ivNumThree,
            ivNumFour, ivNumFive, ivNumSix, ivNumSeven,
            ivNumEight, ivNumNine, ivOk, ivDelete;

    private Context mContext;
    private SettingConfig settingConfig;
    private String pwd = "";
    private String fromAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvInput = (TextView) findViewById(R.id.tvInput);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivOk = (HImageView) findViewById(R.id.ivOk);
        ivDelete = (HImageView) findViewById(R.id.ivClear);
        ivNumZero = (HImageView) findViewById(R.id.ivNumZero);
        ivNumOne = (HImageView) findViewById(R.id.ivNumOne);
        ivNumTwo = (HImageView) findViewById(R.id.ivNumTwo);
        ivNumThree = (HImageView) findViewById(R.id.ivNumThree);
        ivNumFour = (HImageView) findViewById(R.id.ivNumFour);
        ivNumFive = (HImageView) findViewById(R.id.ivNumFive);
        ivNumSix = (HImageView) findViewById(R.id.ivNumSix);
        ivNumSeven = (HImageView) findViewById(R.id.ivNumSeven);
        ivNumEight = (HImageView) findViewById(R.id.ivNumEight);
        ivNumNine = (HImageView) findViewById(R.id.ivNumNine);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOk();
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDelete();
            }
        });
        setClickListenerForNum(ivNumZero, 0);
        setClickListenerForNum(ivNumOne, 1);
        setClickListenerForNum(ivNumTwo, 2);
        setClickListenerForNum(ivNumThree, 3);
        setClickListenerForNum(ivNumFour, 4);
        setClickListenerForNum(ivNumFive, 5);
        setClickListenerForNum(ivNumSix, 6);
        setClickListenerForNum(ivNumSeven, 7);
        setClickListenerForNum(ivNumEight, 8);
        setClickListenerForNum(ivNumNine, 9);

        settingConfig = SettingConfig.getInstance(mContext);
        fromAction = getIntent().getAction();
        if (TextUtils.equals(fromAction, ACTION_ADMIN)) {
            tvTitle.setText("获取管理员权限");
        } else if (TextUtils.equals(fromAction, ACTION_POWER)) {
            tvTitle.setText("获取关机权限");
        }
    }

    /**
     * 点击OK
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:04
     */
    private void clickOk() {
        if (TextUtils.isEmpty(pwd)) {
            showShort(mContext, "请输入密码");
        } else {
            switch (fromAction) {
                case ACTION_ADMIN:
                    acquireSettingPermission();
                    break;
                case ACTION_POWER:
                    acquirePowerPermission();
                    break;
            }
        }
    }

    /**
     * 点击删除
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:04
     */
    private void clickDelete() {
        if (pwd.length() > 1) {
            pwd = pwd.substring(0, pwd.length() - 1);
            tvInput.setText(pwd);
        } else {
            clearInput();
        }
    }

    /**
     * 给数字设置监听
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:08
     */
    private void setClickListenerForNum(HImageView view, final int num) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNum(num);
            }
        });
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
     * 获取设置权限
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:08
     */
    private void acquireSettingPermission() {
        if (TextUtils.equals(pwd, settingConfig.getPassword()) || TextUtils.equals(pwd, SettingConstant.LOGIN_PWD)) {
            setResult(RESULT_OK);
            finish();
        } else {
            showShort(mContext, "密码错误");
            clearInput();
        }
    }

    /**
     * 获取关机权限
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:08
     */
    private void acquirePowerPermission() {
        if (TextUtils.equals(pwd, settingConfig.getPassword()) || TextUtils.equals(pwd, SettingConstant.LOGIN_PWD)) {
            settingConfig.setPowerPermi(1);
            showShort(mContext, "获取关机权限成功");
            setResult(RESULT_OK);
            finish();
        } else {
            showShort(mContext, "密码错误");
            clearInput();
        }
    }

    /**
     * 清空输入
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:08
     */
    private void clearInput() {
        pwd = "";
        tvInput.setText("请输入密码");
    }

    /**
     * 点击数字
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/6/27 11:03
     */
    private void clickNum(int num) {
        if (pwd.length() < 8) {
            pwd += num;
            tvInput.setText(pwd);
        } else {
            clearInput();
        }
    }

}
