package com.goockr.nakedeyeguard.Music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class NoisyAudioStreamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MusicService.class);
        serviceIntent.setAction(Constant.ACTION_MEDIA_PLAY_PAUSE);
        context.startService(serviceIntent);
    }
}
