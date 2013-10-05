package com.deepmine.by.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.deepmine.by.components.TimerTaskPlus;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.GSONTransformer;
import com.deepmine.by.models.DataTitle;
import com.deepmine.by.models.Playlist;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by zyr3x on 04.10.13.
 */
public class DataService extends Service implements Constants {

    private static boolean _status = false;
    private static Timer _timer = new Timer();
    private AQuery _aQuery = new AQuery(this);
    private static DataTitle _dataTitle = new DataTitle();
    private static Playlist _playlist =new  Playlist();
    private static String _lastTitle = "";
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

    public static DataTitle getDataTitle()
    {
        return _dataTitle;

    }

    public  static String getLastTitle()
    {
        return _lastTitle;
    }

    public static Playlist getPlaylist()
    {
        return _playlist;
    }

    protected void getData()
    {

        _timer = new Timer();
        _timer.scheduleAtFixedRate(new TimerTaskPlus() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        _aQuery.transformer(new GSONTransformer())
                                .ajax(
                                        RADIO_DATA_URL,
                                        DataTitle.class,
                                        new AjaxCallback<DataTitle>() {
                                            public void callback(String url, DataTitle dataTitle, AjaxStatus status) {
                                                _dataTitle = dataTitle;

                                            }
                                        }
                                );
                        if(!_lastTitle.equals(_dataTitle.title))
                        {
                            _lastTitle = _dataTitle.title;
                            _aQuery.transformer(new GSONTransformer())
                                    .ajax(
                                            RADIO_LIST_URL,
                                            Playlist.class,
                                            new AjaxCallback<Playlist>() {
                                                public void callback(String url, Playlist playlist, AjaxStatus status) {
                                                    _playlist = playlist;
                                                }
                                            }
                                    );
                        }
                    }
                });
            }
        },1000,5000);
    }

}
