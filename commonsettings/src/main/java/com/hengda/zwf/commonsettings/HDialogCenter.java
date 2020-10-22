package com.hengda.zwf.commonsettings;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hengda.zwf.commondialog.HDialogBuilder;
import com.hengda.zwf.commondialog.util.DialogClickListener;


/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/26 19:03
 * 邮箱：tailyou@163.com
 * 描述：Dialog中心
 */
public class HDialogCenter {

    private static HDialogBuilder hDialogBuilder;

    /**
     * 显示Dialog-CustomView
     *
     * @param context
     * @param view
     * @param dialogClickListener
     * @param txt
     */
    public static void showDialog(Context context, View view, final DialogClickListener dialogClickListener, String... txt) {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(context);
        hDialogBuilder
                .withTitle(txt[0])
                .dlgColor(ContextCompat.getColor(context, R.color.setting_accent))
                .setCustomView(view)
                .pBtnText(txt[1])
                .pBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogClickListener.p();
                    }
                })
                .cancelable(false);
        if (txt.length == 3) {
            hDialogBuilder
                    .nBtnText(txt[2])
                    .nBtnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogClickListener.n();
                        }
                    });
        }
        hDialogBuilder.show();
    }

    /**
     * 隐藏Dialog
     */
    public static void hideDialog() {
        if (hDialogBuilder != null && hDialogBuilder.isShowing()) {
            hDialogBuilder.dismiss();
            hDialogBuilder = null;
        }
    }

}
