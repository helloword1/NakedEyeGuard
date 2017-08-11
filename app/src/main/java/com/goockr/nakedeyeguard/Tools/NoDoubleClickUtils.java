package com.goockr.nakedeyeguard.Tools;

/**
 * Created by CMQ on 2017/6/9.
 */

public class NoDoubleClickUtils {

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 250) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
