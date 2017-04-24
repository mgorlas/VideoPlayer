package com.a203217.mgorlas.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

/**
 * Created by marze on 09.03.2017.
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.video = (VideoView) row.findViewById(R.id.video);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        //holder.video.setClickable(false);
        //holder.video.setFocusable(false);

        setVideo(holder, position);
        return row;
    }

    private void setVideo(ViewHolder holder, int position) {
        VideoItem item = (VideoItem) data.get(position);

        holder.video.setVideoPath("android.resource://"+ context.getPackageName() + "/" + item.getDemoId());
        MediaController mediaController = new MediaController(getContext());
        mediaController.setVisibility(View.GONE);
        holder.video.setMediaController(mediaController);
        holder.video.seekTo(100);

        holder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    static class ViewHolder {
        VideoView video;

    }



}
