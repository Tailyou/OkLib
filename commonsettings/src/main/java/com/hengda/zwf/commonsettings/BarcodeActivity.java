package com.hengda.zwf.commonsettings;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 条形码
 *
 * @author 祝文飞（Tailyou）
 * @time 2016/11/30 11:32
 */
public class BarcodeActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvDeviceNo;
    private ImageView ivBack;
    private ImageView ivBarcode;
    private String deviceNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        deviceNo = SettingConfig.getInstance(this).getDeviceNo();

        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBarcode = (ImageView) findViewById(R.id.ivBarcode);
        tvDeviceNo = (TextView) findViewById(R.id.tvDeviceNo);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvDeviceNo.setText(deviceNo);
        tvTitle.setText("条形码");
        showBarcode();
    }

    public void showBarcode() {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(deviceNo, BarcodeFormat.CODE_128, 500, 200);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            ivBarcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
