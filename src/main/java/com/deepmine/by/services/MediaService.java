package com.deepmine.by.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.deepmine.by.MainActivity;
import com.deepmine.by.R;
import com.deepmine.by.components.TimerTaskPlus;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.models.DataTitle;

import java.util.Timer;

/**
 * Created by zyr3x on 01.10.13.
 */
public class MediaService extends Service implements Constants{

    private static String TAG = MAIN_TAG+":MediaService";

    private static final int NOTIFICATION_ID = 1;
    private static boolean _isStartService = false;
    private static boolean _isError = false;
    private static DataTitle _dataTitle;
    private static Timer _timer = new Timer();
    private static NotificationManager mNotificationManager = null;
    private static MediaTask _mediaTask = null;
    private static MediaPlayer _mediaPlayer = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        _mediaTask = new MediaTask();
        return START_STICKY;
    }


    private void stopService() {
        stop();
    }

    public static void play()
    {
        RadioService.stop();
        if(_dataTitle!=null)
            _mediaTask.execute();
        else
            _isError = true;
    }

    public static void setDataTitle(DataTitle dataTitle)
    {
        _dataTitle =dataTitle;
        Log.d(TAG,dataTitle.title);
    }

    public static DataTitle getDataTitle()
    {
        return _dataTitle;
    }

    public void start()
    {
        try {
            _mediaPlayer = new MediaPlayer();
            _mediaPlayer.setDataSource(MEDIA_TRACK_URL+_dataTitle.title+".mp3"+MEDIA_TRACK_TYPE);
            _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            _mediaPlayer.prepare();
            _mediaPlayer.start();

            _isStartService = true;
        } catch (Exception e) {
            _isError = true;
            _isStartService = false;
            stop();
            Log.d(TAG, "Exception in streaming mediaplayer e = " + e);
        }
    }

    protected void updateTitle()
    {
        _timer = new Timer();
        _timer.scheduleAtFixedRate(new TimerTaskPlus() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (_isStartService) {
                                updateNotification(

                                        _dataTitle.artist,
                                        _dataTitle.track
                                );
                        }
                    }
                });
            }
        },1000,1000);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        updateTitle();
        super.onDestroy();
    }

    public static void stop()
    {
        _isStartService = false;

        if(_mediaTask !=null)
            _mediaTask.cancel(false);

        if(mNotificationManager!=null)
            mNotificationManager.cancel(NOTIFICATION_ID);

        if(_timer!=null)
            _timer.cancel();

        if(_mediaPlayer!=null)
            _mediaPlayer.stop();
    }

    public static boolean isPlaying()
    {
       if(_mediaPlayer!= null)
           return  _isStartService;
        else
           return false;
    }

    public static boolean isErrors()
    {
        return _isError;
    }
    public static void cleanErrors()
    {
         _isError = false;
    }

    private void updateNotification(String title1, String title2) {

        if(mNotificationManager==null)
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.ic_play, RADIO_TITLE, System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,notificationIntent, 0);
        notification.setLatestEventInfo(getApplicationContext(), title1, title2, contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID,notification);
    }

    class MediaTask extends AsyncTask<Object, Void, Boolean> {

        protected Boolean doInBackground(Object... arg) {
            start();
            return true;
        }

        protected void onPostExecute(Boolean flag) {
            if(flag)
                updateTitle();
        }
    }


}
