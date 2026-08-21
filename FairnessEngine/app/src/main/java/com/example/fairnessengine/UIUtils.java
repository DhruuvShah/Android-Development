package com.example.fairnessengine;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class UIUtils {
    
    public static boolean areAnimationsEnabled(Context context) {
        float animatorDurationScale = Settings.Global.getFloat(
                context.getContentResolver(), 
                Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f);
        return animatorDurationScale != 0.0f;
    }

    public static void addPressScaleAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (areAnimationsEnabled(v.getContext())) {
                        animateScale(v, 0.92f);
                    } else {
                        v.setScaleX(0.92f);
                        v.setScaleY(0.92f);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (areAnimationsEnabled(v.getContext())) {
                        animateScale(v, 1.0f);
                    } else {
                        v.setScaleX(1.0f);
                        v.setScaleY(1.0f);
                    }
                    break;
            }
            return false; // let click listener work
        });
    }

    private static void animateScale(View view, float scale) {
        SpringAnimation animX = new SpringAnimation(view, DynamicAnimation.SCALE_X, scale);
        SpringAnimation animY = new SpringAnimation(view, DynamicAnimation.SCALE_Y, scale);
        
        animX.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
        animX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        
        animY.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
        animY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        
        animX.start();
        animY.start();
    }
    
    public static void animateRevealEntrance(View view) {
        if (!areAnimationsEnabled(view.getContext())) {
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
            return;
        }
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);
        SpringAnimation animX = new SpringAnimation(view, DynamicAnimation.SCALE_X, 1.0f);
        SpringAnimation animY = new SpringAnimation(view, DynamicAnimation.SCALE_Y, 1.0f);
        
        animX.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        animX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        
        animY.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        animY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        
        animX.start();
        animY.start();
    }
}
