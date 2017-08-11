package com.goockr.nakedeyeguard.Tools;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by JJT-ssd on 2017/2/23.
 */

public class LoadingAnimation {

    public static void showAnimation(View view)
    {
        TranslateAnimation translateAnimation=new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-0.6f,Animation.RELATIVE_TO_PARENT,0,
                Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,0);
        translateAnimation.setDuration(800);//动画时间
        translateAnimation.setRepeatCount(0);//从复次数
        translateAnimation.setRepeatMode(Animation.REVERSE);//动画执行模式
        translateAnimation.setFillAfter(true);//动画结束后停留在当前位置
        view.startAnimation(translateAnimation);
    }
    public static void rotateDictAnimation(View view,int currentDistance)
    {
        RotateAnimation rotateAnimation=new RotateAnimation(0,currentDistance, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(500);//动画时间
        rotateAnimation.setInterpolator(new LinearInterpolator());//不停顿
        rotateAnimation.setFillAfter(false);//动画结束后停留在当前位置
        view.startAnimation(rotateAnimation);
    }
    public static void rotateDictAnimation(View view,int currentDistance,int timems)
    {
        RotateAnimation rotateAnimation=new RotateAnimation(0,currentDistance, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(timems);//动画时间
        rotateAnimation.setInterpolator(new LinearInterpolator());//不停顿
        rotateAnimation.setFillAfter(false);//动画结束后停留在当前位置
        view.startAnimation(rotateAnimation);
    }
    public static void rotateAnimation(View view)
    {
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(500);//动画时间
        rotateAnimation.setInterpolator(new LinearInterpolator());//不停顿
        rotateAnimation.setRepeatCount(1000);//从复次数
        //rotateAnimation.setRepeatMode(Animation.REVERSE);//动画执行模式
        rotateAnimation.setFillAfter(false);//动画结束后停留在当前位置
        view.startAnimation(rotateAnimation);
    }
}
