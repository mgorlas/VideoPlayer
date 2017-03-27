package com.a203217.mgorlas.videoplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.VideoView;

import java.util.ArrayList;

public class VideoGallery extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private int visibleItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);


        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item, getData());
        gridView.setAdapter(gridAdapter);

        int position = getIntent().getIntExtra("position", 0);
        gridView.smoothScrollToPosition(position);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    VideoItem item = (VideoItem) parent.getItemAtPosition(position);
                    Intent intent = new Intent(VideoGallery.this, Player.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("position", position);
                    startActivity(intent);
                } catch(Exception exc)
                {
                    System.out.print(exc.getMessage());
                }

            }
        });



        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && getVisibleItemCount() >8 ) {
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
    }

    public void setVisibleItemCount(int visibleItemCount){
        this.visibleItemCount = visibleItemCount;
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    // Prepare some dummy data for gridview
    private ArrayList<VideoItem> getData() {
        final ArrayList<VideoItem> imageItems = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            int id = getResources().getIdentifier("video1", "raw", getPackageName());
            imageItems.add(new VideoItem(id));
        }
        return imageItems;
    }
}
