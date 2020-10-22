package com.hengda.zwf.commonsettings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 修正《十代机室内版》圆形图片显示不圆问题
 *
 * @author 祝文飞（Tailyou）
 * @time 2017/2/16 18:52
 */
public class HImageView extends AppCompatImageView {

    private Paint paint;
    private float PERCENT = 1f;

    public HImageView(Context context) {
        this(context, null);
    }

    public HImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            int left = (int) (getWidth() * (1 - PERCENT) / 2);
            int right = (int) (getWidth() * (1 + PERCENT) / 2);
            drawable.setBounds(left, 0, right, getHeight());
            drawable.draw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

}
