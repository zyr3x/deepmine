package com.deepmine.by;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by zyr3x on 01.10.13.
 */
public class RadioService extends Service {
    public static MediaPlayer mediaPlayer;
    public static String TAG = "DEEPMINE SERVICE";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("http://deepmine.by:8000/deepmine");
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.d(TAG, "Exception in streaming mediaplayer e = " + e);
        }
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
