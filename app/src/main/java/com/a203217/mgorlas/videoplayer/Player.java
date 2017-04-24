package com.a203217.mgorlas.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.content.Context;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

/**
 * Created by marze on 18.03.2017.
 */

public class Player extends Activity {

    private Button backButton;
    private Button playButton;
    private Button pauseButton;
    private SeekBar progress;
    private int position;
    private int startTime = 0;
    private int finalTime = 0;
    private Handler myHandler = new Handler();
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        position = getIntent().getIntExtra("position", 0);

        backButton = (Button) findViewById(R.id.btnClose);
        playButton = (Button) findViewById(R.id.play);
        pauseButton = (Button) findViewById(R.id.pause);
        progress = (SeekBar) findViewById(R.id.progress);

        viewPager = (ViewPager) findViewById(R.id.video);
        VideoPagerAdapter adapter = new VideoPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        setVolume();
        createListeners();
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onStop(){
        super.onStop();
        if( getCurrentView() != null)
            getCurrentView().stopPlayback();
    }

    public VideoView getCurrentView(){
        return (VideoView)viewPager.getFocusedChild();
    }

    public void setVolume(){
        try {
            progress.setMax(getCurrentView().getDuration());
            progress.setProgress(0);
        } catch (Exception e){}

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    getCurrentView().seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekbar) {
                myHandler.removeCallbacks(updateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {
                myHandler.removeCallbacks(updateTimeTask);
                getCurrentView().seekTo(progress.getProgress());
                updateProgressBar();
            }

        });
    }

    public void createListeners(){

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                progress.setProgress(getCurrentView().getCurrentPosition());
                if(getCurrentView().getCurrentPosition() == getCurrentView().getDuration()){
                    getCurrentView().seekTo(0);
                }
                setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                getCurrentView().pause();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getCurrentView().stopPlayback();
                    Intent intent = new Intent(Player.this, VideoGallery.class);
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
                    VideoView vv = getCurrentView();
                    if(!vv.isPlaying()) {
                        vv.start();
                        finalTime = vv.getDuration();
                        startTime = vv.getCurrentPosition();
                        progress.setProgress((int) startTime);
                        updateProgressBar();
                    }
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
                    if(getCurrentView().isPlaying())
                        getCurrentView().pause();
                }
                catch(Exception exc)
                {
                    System.out.print(exc.getMessage());
                }
            }
        });
    }

    private void updateProgressBar() {
        myHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            progress.setProgress(getCurrentView().getCurrentPosition());
            progress.setMax(finalTime);
            myHandler.postDelayed(this, 100);
        }
    };


    private class VideoPagerAdapter extends PagerAdapter {

        private int[] mVideos;

        public VideoPagerAdapter(){
            this.mVideos = getData();
        }

        private int[] getData() {
            int[] results = new int[12];
            for (int i = 0; i < 12; i++) {
                int id = getResources().getIdentifier("video"+(i+1), "raw", getPackageName());
                results[i] = id;
            }
            return results;
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((VideoView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = Player.this;
            VideoView videoView = new VideoView(context);

            videoView.setVideoPath("android.resource://" + context.getPackageName() + "/" + mVideos[position]);
            MediaController mediaController = new MediaController(context);
            mediaController.setVisibility(View.GONE);
            videoView.setMediaController(mediaController);
            videoView.seekTo(100);

            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            ((ViewPager) container).addView(videoView, 0);

            return videoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((VideoView) object);
        }
    }
}
