package com.deepmine.by;

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
    public static MediaPlayer mediaPlayer;
    public static ImageView playBtn;
    public static TextView trackTitle1;
    public static TextView trackTitle2;

    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq =new AQuery(this);
        playBtn =(ImageView) findViewById(R.id.playBtn);
        trackTitle1 =  (TextView) findViewById(R.id.tackTitle1);
        trackTitle2 =  (TextView) findViewById(R.id.tackTitle2);
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


    public void playMedia()
    {
        AsyncTask task = new playTask();
        task.execute();
        playBtn.setImageResource(R.drawable.ic_media_pause);
    }

    public void stopMedia()
    {
        mediaPlayer.stop();
        playBtn.setImageResource(R.drawable.ic_media_play);
    }

    public void onPlay(View view)
    {
        if( mediaPlayer != null && mediaPlayer.isPlaying())
            stopMedia();
        else
            playMedia();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    class playTask extends AsyncTask<Object, Void, Void> {
        protected Void doInBackground(Object... arg) {
            if(  MainActivity.this.mediaPlayer == null || ! MainActivity.this.mediaPlayer.isPlaying())
            {
                String url = "http://deepmine.by:8000/deepmine";
                MainActivity.this.mediaPlayer = new MediaPlayer();
                try {
                    MainActivity.this.mediaPlayer.setDataSource(url);
                    MainActivity.this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    MainActivity.this.mediaPlayer.prepare();
                    MainActivity.this.mediaPlayer.start();
                } catch (Exception e) {
                    Log.d(TAG, "Exception in streaming mediaplayer e = " + e);
                }
            }
            return null;
        }

        protected void onPostExecute() {

        }
    }


}
