package com.mic.zl.micangpartner.util;

import android.support.v4.view.ViewPager;
import android.view.View;

/*
 * *viewpaper页面缩放转变
 * *PageTransformer为ViewPaper切换时候的一个动画
 * */
public class ScalePageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;//做小的缩放值

    @Override
    public void transformPage(View view, float position) {
        if (position < -1){//左侧
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        }
        else if (position <= 1) {//滑动是效果
            float scaleFactor =  MIN_SCALE+(1-Math.abs(position))*(1-MIN_SCALE);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
        else {//右侧
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        }
    }
}