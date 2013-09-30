package com.deepmine.by;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    public static MediaPlayer mediaPlayer;
    public static Button playBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playBtn =(Button) findViewById(R.id.playBtn);
    }

    public void playMedia()
    {
        String url = "http://deepmine.by:8000/deepmine";
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.d("Exception", "Exception in streaming mediaplayer e = " + e);
        }
        playBtn.setText("Stop");
    }

    public void stopMedia()
    {
        mediaPlayer.stop();
        playBtn.setText("Play");
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
        // Inflate the menu; this adds items to the action bar if it is present.
       /// getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
