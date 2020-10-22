package com.hengda.zwf.commondialog.anim;

import android.view.View;
import android.view.animation.BounceInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;

import static com.nineoldandroids.animation.ObjectAnimator.ofFloat;

public class BounceScaleIn extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        ObjectAnimator scaleX = ofFloat(view, "scaleX", 0.0f, 1.0f).setDuration(mDuration);
        ObjectAnimator scaleY = ofFloat(view, "scaleY", 0.0f, 1.0f).setDuration(mDuration);
        scaleX.setInterpolator(new BounceInterpolator());
        scaleY.setInterpolator(new BounceInterpolator());
        getAnimatorSet().playTogether(scaleX, scaleY);
    }

}
