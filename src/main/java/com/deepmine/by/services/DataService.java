package com.deepmine.by.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.deepmine.by.R;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ItemImageBinder;
import com.deepmine.by.helpers.ResourceHelper;
import com.deepmine.by.models.Blocks;
import com.deepmine.by.models.DataTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zyr3x on 04.10.13.
 */
public class DataService extends Service implements Constants {

    public static String TAG = MAIN_TAG+":DataService";
    private static boolean _status = false;
    private static Timer _timer = new Timer();
    private AQuery _aQuery = new AQuery(this);
    private static DataTitle _dataTitle = new DataTitle();

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
    protected void getData()
    {
        _timer = new Timer();
        class UpdateTask extends TimerTask {

            public void run() {
                _aQuery.ajax(RADIO_DATA_URL, JSONObject.class, new AjaxCallback<JSONObject>() {

                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {
                            if (json != null) {
                                try
                                {

                                    if(!_dataTitle.title.equals(json.getString("title")))
                                    {
                                        _dataTitle.title = json.getString("title");
                                        _dataTitle.artist = json.getString("artist");
                                        _dataTitle.track = json.getString("track");
                                        _dataTitle.cover = RADIO_COVER_URL+json.getString("title")+".jpg";
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

        TimerTask updateTask = new UpdateTask();
        _timer.scheduleAtFixedRate(updateTask, 0, 5000);
    }

}
