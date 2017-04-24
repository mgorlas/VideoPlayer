package com.a203217.mgorlas.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.VideoView;
import java.util.ArrayList;

public class VideoGallery extends Activity {

    private GridView gridView;
    private int visibleItemCount = 0;
    final private static String TAG = "VideoGallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);

        gridView = (GridView) findViewById(R.id.gridView);
        GridViewAdapter gridAdapter = new GridViewAdapter(this, R.layout.grid_item, getData());
        gridView.setAdapter(gridAdapter);

        int position = getIntent().getIntExtra("position", 0);
        gridView.smoothScrollToPosition(position);
        setGridViewListeners();
    }

    public void setGridViewListeners(){
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && getVisibleItemCount() > 8) {
                    View v = view.getChildAt(0);
                    int top = -v.getTop();
                    int height = v.getHeight();
                    if (top < height / 2) {
                        gridView.smoothScrollToPosition(view.getFirstVisiblePosition());
                    } else {
                        gridView.scrollBy(0, height - top);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                setVisibleItemCount(visibleItemCount);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((VideoView)((LinearLayout)view).getChildAt(0)).start();
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoItem item = (VideoItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(VideoGallery.this, Player.class);
                intent.putExtra("videoId", item.getVideoId());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        gridView.setOnTouchListener(new GridView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int position = gridView.pointToPosition((int) event.getX(), (int) event.getY());
                VideoView vv = ((VideoView)((LinearLayout)gridView.getChildAt(position)).getChildAt(0));
                (vv).onTouchEvent(event);

                if(MotionEvent.ACTION_UP == event.getAction()){
                    if(vv.isPlaying()) vv.pause();
                }

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

                    if(!rect.contains((int) event.getX(), (int) event.getY())){
                        if(vv.isPlaying()) vv.pause();
                    }
                }
                return false;
            }
        });
    }


    public void setVisibleItemCount(int visibleItemCount){
        this.visibleItemCount = visibleItemCount;
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    // Prepare some dummy data for gridview
    private ArrayList<VideoItem> getData() {
        final ArrayList<VideoItem> videoItems = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            int videoId = getResources().getIdentifier("video_demo_1", "raw", getPackageName());
            int demoId = getResources().getIdentifier("video_demo_1", "raw", getPackageName());
            videoItems.add(new VideoItem(videoId, demoId));
        }
        return videoItems;
    }
}
