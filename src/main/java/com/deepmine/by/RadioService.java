package com.deepmine.by;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.deepmine.by.helpers.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zyr3x on 01.10.13.
 */
public class RadioService extends Service implements Constants{
    public static MediaPlayer mediaPlayer = null;
    public static String TAG = MAIN_TAG+":RadioService";
    public static boolean isStartService = false;
    private AQuery aq = new AQuery(this);
    private static String lastTitle = "";
    public static Timer timer = new Timer();
    public static final int NOTIFICATION_ID = 1;
    private static NotificationManager mNotificationManager = null;
    private static MediaTask mediaTask = null;
    private static boolean isError = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        mediaTask = new MediaTask();
        mediaTask.execute();
        return START_STICKY;
    }


    private void stopService() {
        stop();
    }

    public void start()
    {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(RADIO_SERVER_URL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();

            isStartService = true;
        } catch (Exception e) {
            isError = true;
            isStartService = false;
            stop();
            Log.d(TAG, "Exception in streaming mediaplayer e = " + e);
        }
    }

    protected void updateTitle()
    {
        timer = new Timer();
        class UpdateTask extends TimerTask {

            public void run() {
                if(isStartService)
                {
                    aq.ajax(RADIO_DATA_URL, JSONObject.class, new AjaxCallback<JSONObject>() {

                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            if (json != null) {
                                try
                                {
                                    if(!lastTitle.equals(json.getString("title")))
                                    {
                                        lastTitle = json.getString("title");
                                        updateNotification(json.getString("artist"),json.getString("track"));
                                    }
                                }
                                catch (JSONException e)
                                {
                                    Log.d(TAG, "ERROR JSON");
                                }
                            }
                        }
                    });
                }

            }
        }

        TimerTask updateTask = new UpdateTask();
        timer.scheduleAtFixedRate(updateTask, 0, 5000);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    public static void stop()
    {
        isStartService = false;
        lastTitle = "";

        if(mediaTask !=null)
            mediaTask.cancel(false);

        if(mNotificationManager!=null)
            mNotificationManager.cancel(NOTIFICATION_ID);

        if(timer!=null)
            timer.cancel();

        if(mediaPlayer!=null)
            mediaPlayer.stop();
    }

    public static boolean isPlaying()
    {
       if(mediaPlayer!= null)
           return  isStartService;
        else
           return false;
    }

    public static boolean isErrors()
    {
        return isError;
    }
    public static void cleanErrors()
    {
         isError = false;
    }

    private void updateNotification(String title1, String title2) {

        if(mNotificationManager==null)
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.ic_media_play, RADIO_TITLE, System.currentTimeMillis());
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
