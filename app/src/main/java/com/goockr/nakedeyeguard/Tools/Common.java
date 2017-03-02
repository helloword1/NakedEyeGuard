package com.goockr.nakedeyeguard.Tools;

import android.os.Handler;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class Common {

    public static void scheduleDismiss(final KProgressHUD hud) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }
}
