package com.deepmine.by;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    public static String TAG = "DEEPMINE";
    private static ImageView playBtn;
    public static TextView trackTitle1;
    public static TextView trackTitle2;
    private Intent radioService;

    private AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

        playBtn =(ImageView) findViewById(R.id.playBtn);
        trackTitle1 =  (TextView) findViewById(R.id.tackTitle1);
        trackTitle2 =  (TextView) findViewById(R.id.tackTitle2);

        radioService = new Intent(this, RadioService.class);
        updateTitle();

    }

    protected void updateTitle()
    {
        Timer timer = new Timer();

        class UpdateTask extends TimerTask {

            public void run() {
                AQuery ajax = aq.ajax("http://deepmine.by/d/index.php/ajaxRadioTitle", JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        if (json != null) {
                            try
                            {
                                String title = json.getString("title");
                                String artist = json.getString("artist");
                                String track = json.getString("track");

                                MainActivity.this.trackTitle1.setText(artist);
                                MainActivity.this.trackTitle2.setText(track);

                                aq.id(R.id.trackCover).image("http://deepmine.by/d/static/music/cover/"+title+".jpg",true, true, 0, R.drawable.ic_launcher_full);
                                updatePlayButton();
                            }
                            catch (JSONException e)
                            {
                                Log.d(TAG, "Exception parse");
                            }

                            Log.d(TAG, "JSON:" + json.toString());

                        } else {
                            Log.d(TAG, "Exception in ajax:" + status.getCode());

                        }
                    }
                });

            }
        }

        TimerTask updateTask = new UpdateTask();
        timer.scheduleAtFixedRate(updateTask, 0, 5000);
    }

    public void onPlay(View view)
    {
        if(RadioService.isPlaying())
            stopMedia();
        else
            playMedia();
    }

    private void playMedia()
    {
        startService(radioService);
        updatePlayButton();
    }

    private void stopMedia()
    {
        stopService(radioService);
        RadioService.stop();
        updatePlayButton();
    }

    private void updatePlayButton()
    {
        if(RadioService.isPlaying())
            playBtn.setImageResource(R.drawable.ic_media_pause);
        else
            playBtn.setImageResource(R.drawable.ic_media_play);
    }




}
