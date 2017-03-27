package com.a203217.mgorlas.videoplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by marze on 18.03.2017.
 */

public class Player extends AppCompatActivity{

    private Button backButton;
    private Button playButton;
    private Button pauseButton;
    private SeekBar volume;
    private ImageView imageView;
    private int imageId;
    private int musicId;
    private int position;
    private MediaPlayer mediaPlayer = null;
    private AudioManager audioManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);


        imageId = getIntent().getIntExtra("id", 0);
        musicId = getIntent().getIntExtra("musicId", 0);
        position = getIntent().getIntExtra("position", 0);

        backButton = (Button) findViewById(R.id.btnClose);
        playButton = (Button) findViewById(R.id.play);
        pauseButton = (Button) findViewById(R.id.pause);

        imageView = (ImageView) findViewById(R.id.image);
        volume = (SeekBar) findViewById(R.id.volume);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setImage();
        setMusic();
        setVolume();
        createListeners();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onStop(){
        super.onStop();
        mediaPlayer.stop();
    }

    public void setVolume(){
        try
        {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setMusic(){
        mediaPlayer = MediaPlayer.create(Player.this, musicId);
    }

    public void setImage(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
        imageView.setImageBitmap(bitmap);
    }

    public void createListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.stop();
                    Intent intent = new Intent(Player.this, VideoGallery.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
                catch(Exception exc)
                {
                    System.out.print(exc.getMessage());
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.start();
                }
                catch(Exception exc)
                {
                    System.out.print(exc.getMessage());
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mediaPlayer == null) setMusic();
                    mediaPlayer.pause();
                }
                catch(Exception exc)
                {
                    System.out.print(exc.getMessage());
                }
            }
        });
    }
}
