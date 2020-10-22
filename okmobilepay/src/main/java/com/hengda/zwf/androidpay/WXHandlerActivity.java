package com.hengda.zwf.androidpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXHandlerActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Wxpay.getInstance() != null) {
            Wxpay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (Wxpay.getInstance() != null) {
            Wxpay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (Wxpay.getInstance() != null) {
                if (baseResp.errStr != null) {
                    Log.e("wxpay", "error=" + baseResp.errStr);
                }
                Wxpay.getInstance().onResp(baseResp.errCode);
                finish();
            }
        }
    }

}
