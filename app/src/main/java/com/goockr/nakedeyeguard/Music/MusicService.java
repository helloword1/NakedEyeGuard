package com.goockr.nakedeyeguard.Music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;


import com.goockr.music.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */

public class MusicService extends Service implements AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "MusicService";
    private static final int NOTIFICATION_ID = 0x111;

    //    private int currentPosition;
    private MediaPlayer mediaPlayer;
    private boolean isPause = false;
    public static List<MusicInfoBean> musicInfoList = new ArrayList<>();

    private NotificationManager mNotificationManager;
    private int currentMode;//默认播放模式
    private AudioManager mAudioManager;

    public static final int MODE_ONE_LOOP = 0;
    public static final int MODE_ALL_LOOP = 1;
    public static final int MODE_RANDOM = 2;
    public static final int MODE_SEQUENCE = 3;

    public static final String[] MODE_DESC = {"单曲循环", "列表循环", "随机播放", "顺序播放"};

    List<OnPlayerListener> listeners = new ArrayList<>();

    private IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();

    private long quitTimerRemain;
    private Notification notification;
    private Handler handler = new Handler();

    private Runnable changeProgress = new Runnable() {
        @Override
        public void run() {
            if (handler != null && listeners.size() > 0 && isPlaying()) {
                int progress = mediaPlayer.getCurrentPosition();
                for (OnPlayerListener listener : listeners) {
                    listener.onProgressChange(progress);
                }
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        currentMode = MODE_ONE_LOOP;

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initMediaPlayer();

    }

    public void addPlayerListener(OnPlayerListener listener) {
        listeners.add(listener);
    }

    public void removePlayerListener(OnPlayerListener listener) {
        for (OnPlayerListener listener1 : listeners) {
            if (listener1.toString().equals(listener.toString())) {
                listeners.remove(listener);
                return;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Constant.ACTION_MEDIA_PLAY_PAUSE:
                    pause();
                    break;
                case Constant.ACTION_MEDIA_PREVIOUS:
                    //playPrevious();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void initMediaPlayer() {

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("onCompletion","onCompletion");
//                    if(!isPlaying()){
//                        return;
//                    }

                    mediaPlayer.start();

//                    switch (currentMode) {
//                        case MODE_ONE_LOOP:
//                            mediaPlayer.start();
//                            break;
//                    }
                }
            });
        } else {
            Log.e(TAG, "initMediaPlayer: ");
        }
    }

    public void play(int currentMusic) {
        Log.e("playPause","play");
        if(musicInfoList.size() == 0){
            return;
        }


        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(musicInfoList.get(currentMusic).getUrl());
            mediaPlayer.prepare();
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listeners.size() > 0) {
            for (OnPlayerListener listener : listeners) {
                listener.onMusicChange(musicInfoList.get(currentMusic),getDurationTime());
            }
        }
    }

    private void start() {
        mediaPlayer.start();
        handler.post(changeProgress);
        isPause = false;

        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        Log.e("playPause3", "playPauseregister");
        registerReceiver(mNoisyReceiver, mNoisyFilter);
    }

    public void playPause() {
        if (isPlaying()) {
            Log.e("playPause1", "playPause");
            pause();
        } else if (isPause()) {
            Log.e("playPause2", "playPause");
            resume();
        } else {
            Log.e("playPause3", "playPause");
            play(0);
        }
    }

    private int pause() {

        if (!isPlaying()) {
            return -1;
        }
        mediaPlayer.pause();
        isPause = true;
        handler.removeCallbacks(changeProgress);

        mAudioManager.abandonAudioFocus(this);
        unregisterReceiver(mNoisyReceiver);
        if (listeners.size() > 0) {
            for (OnPlayerListener listener : listeners) {
                listener.onPlayPause();
            }
        }
        return 0;
    }

    public int resume() {
        if (isPlaying()) {
            return -1;
        }
        start();
        if (listeners.size() > 0) {
            for (OnPlayerListener listener : listeners) {
                listener.onPlayResume();
            }
        }
        return 0;
    }

    public void stopPlay() {
        stop();
    }

   // public void toNext() {
//        playNext();
//    }

//    public void toPrevious() {
//        playPrevious();
//    }

//    private void playNext() {
//        Log.e("playNext","playNext");
//        switch (currentMode) {
//            case MODE_ONE_LOOP:
//                play(currentMusic);
//                break;
//            case MODE_ALL_LOOP:
//                if (currentMusic + 1 == musicInfoList.size()) {
//                    play(0);
//                } else {
//                    play(currentMusic + 1);
//                }
//                break;
//            case MODE_SEQUENCE:
//                if (currentMusic + 1 == musicInfoList.size()) {
//                    Toast.makeText(this, "没有更多音乐", Toast.LENGTH_SHORT).show();
//                } else {
//                    play(currentMusic + 1);
//                }
//                break;
//            case MODE_RANDOM:
//                play(getRandomPosition());
//                break;
//        }
//    }

//    private void playPrevious() {
//        Log.e("playPrevious","playPrevious");
//        switch (currentMode) {
//            case MODE_ONE_LOOP:
//                play(currentMusic);
//                break;
//            case MODE_ALL_LOOP:
//                if (currentMusic - 1 < 0) {
//                    play(musicInfoList.size() - 1);
//                } else {
//                    play(currentMusic - 1);
//                }
//                break;
//            case MODE_SEQUENCE:
//                if (currentMusic - 1 < 0) {
//                    Toast.makeText(this, "没有上一首歌曲", Toast.LENGTH_SHORT).show();
//                } else {
//                    play(currentMusic - 1);
//                }
//                break;
//            case MODE_RANDOM:
//                play(getRandomPosition());
//                break;
//        }
//    }

    private int getRandomPosition() {
        int random = (int) (Math.random() * (musicInfoList.size() - 1));
        return random;
    }

//    public void saveMode(int mode) {
//        BaseApp.mApp.getKv().put(Constant.SHARE_MODE, mode).commit();
//        currentMode = mode;
//        Toast.makeText(MusicService.this, MODE_DESC[currentMode], Toast.LENGTH_LONG).show();
//    }

//    public int getCurrentMode() {
//        return BaseApp.mApp.getKv().getInt(Constant.SHARE_MODE, MODE_ALL_LOOP);
//    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public boolean isPause() {
        return mediaPlayer != null && isPause;
    }

    public void stop() {
        pause();
        stopQuitTimer();
    }

    public void changeProgress(int progress) {

        mediaPlayer.seekTo(progress * 1000);


    }



//    public int getCurrentMusic() {
//        if(currentMusic > musicInfoList.size()){
//            currentMusic = 0;
//        }
//        return currentMusic;
//    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (isPlaying()) {
                    pause();
                }
                break;
        }
    }



    public class MusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    public void setTime(final long milli) {
        stopQuitTimer();
        if (milli > 0) {
            quitTimerRemain = milli + DateUtils.SECOND_IN_MILLIS;
            handler.post(mQuitRunnable);
        } else {
            quitTimerRemain = 0;
            for (OnPlayerListener listener : listeners) {
                listener.onTimer(milli);
                Log.e("setTime", milli + "");
            }
        }
    }

    private void stopQuitTimer() {
        handler.removeCallbacks(mQuitRunnable);
    }

    private Runnable mQuitRunnable = new Runnable() {
        @Override
        public void run() {
            quitTimerRemain -= DateUtils.SECOND_IN_MILLIS;
            if (quitTimerRemain > 0) {
                for (OnPlayerListener listener : listeners) {
                    listener.onTimer(quitTimerRemain);
                    Log.e("setTime", quitTimerRemain + "");
                }
                handler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            } else {
                stop();
            }
        }
    };

    public int getDurationTime(){
        if(mediaPlayer != null){
            return mediaPlayer.getDuration();
        }
        return 0;
    }
}
