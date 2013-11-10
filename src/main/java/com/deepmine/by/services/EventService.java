package com.deepmine.by.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.deepmine.by.R;
import com.deepmine.by.components.GSONTransformer;
import com.deepmine.by.components.TimerTaskPlus;
import com.deepmine.by.helpers.Constants;

import com.deepmine.by.models.Event;
import com.deepmine.by.models.Events;

import java.util.Timer;

/**
 * Created by zyr3x on 04.10.13.
 */
public class EventService extends Service implements Constants {

    private static boolean _status = false;
    private static Timer _timer = new Timer();
    private AQuery _aQuery = new AQuery(this);
    public final Handler _handler = new Handler();

    private static final int NOTIFICATION_ID = 2;
    private static NotificationManager mNotificationManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        if(!_status)
        {
            _status = true;
            getData();
        }
        return START_STICKY;
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


    public static boolean status()
    {
       return _status;
    }

    public static void stop()
    {
        _status = false;
        if(_timer!=null)
            _timer.cancel();
    }

    protected void getData()
    {

        _timer = new Timer();
        _timer.scheduleAtFixedRate(new TimerTaskPlus() {
            @Override
            public void run() {
                _handler.post(new Runnable() {
                    public void run() {
                        _aQuery.transformer(new GSONTransformer())
                                .ajax(
                                        HOT_EVENT_URL,
                                        Events.class,
                                        new AjaxCallback<Events>() {
                                            public void callback(String url, Events events, AjaxStatus status) {
                                                 if(events.getEvent(0)!=null)
                                                 {
                                                     addNewEvent(events.getEvent(0));
                                                 }
                                            }
                                        }
                                );

                    }
                });
            }
        }, UPDATE_INTERVAL, UPDATE_INTERVAL * 36000);
    }

    private void addNewEvent(Event event)
    {
        updateNotification(event.description,event.title);
    }

    private void updateNotification(String description, String title) {

        if(mNotificationManager==null)
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.ic_launcher, description, System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(getApplicationContext(), description, title, null);
        mNotificationManager.notify(NOTIFICATION_ID,notification);
    }
}
