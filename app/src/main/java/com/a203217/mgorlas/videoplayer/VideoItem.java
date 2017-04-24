package com.a203217.mgorlas.videoplayer;

import android.graphics.Bitmap;

/**
 * Created by marze on 09.03.2017.
 */

public class VideoItem {
    private int videoId;
    private int demoId;

    public VideoItem(int videoId, int demoId) {
        super();
        this.videoId = videoId;
        this.demoId = demoId;
    }

    public int getVideoId(){
        return videoId;
    }

    public int getDemoId() {
        return demoId;
    }
}
