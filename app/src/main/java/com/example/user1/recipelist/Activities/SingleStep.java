package com.example.user1.recipelist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user1.recipelist.ContentProvider.DBApi;
import com.example.user1.recipelist.Objects.StepObject;
import com.example.user1.recipelist.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 *
 */

public class SingleStep extends Activity {

    int id, step, number_of_steps, min_step = 0;

    TextView step_detail;
    Button previous, next;

    private int window_index;
    private long playback_position;
    private SimpleExoPlayerView video_view;
    private SimpleExoPlayer video_player;

    private String video_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_step);
        Intent intent = getIntent();

        String step_extra_key = this.getString(R.string.step_extra);
        String r_id_key = this.getString(R.string.recipe_id_extra);
        String max_steps_key = this.getString(R.string.max_steps_extra);
        String title_key = this.getString(R.string.title_extra);

        id = intent.getIntExtra(r_id_key, 0);
        step = intent.getIntExtra(step_extra_key, 0);
        number_of_steps = intent.getIntExtra(max_steps_key, 0);
        setTitle(intent.getStringExtra(title_key));

        step_detail = (TextView) findViewById(R.id.single_step_detail);
        previous = (Button) findViewById(R.id.previous_navigator);
        next = (Button) findViewById(R.id.next_navigator);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float font_size = height * 0.03f;

        step_detail.setTextSize(font_size);
        previous.setTextSize(font_size*2);
        next.setTextSize(font_size*2);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step--;
                setData();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step++;
                setData();
            }
        });

        video_view = (SimpleExoPlayerView) findViewById(R.id.step_video);

        setData();
    }

    private void initializePlayer() {
        video_player = ExoPlayerFactory.newSimpleInstance(
                this, new DefaultTrackSelector(), new DefaultLoadControl());
        video_view.setPlayer(video_player);
        video_player.setPlayWhenReady(false);
        video_player.seekTo(window_index,playback_position);
        Uri uri = Uri.parse(video_url);
        MediaSource mediaSource = buildMediaSource(uri);
        video_player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    public void setData() {
        StepObject step_object = DBApi.getSingleStep(id, step, this);
        if (step_object == null) return;
        video_url = step_object.getVideo_url();
        step_detail.setText(step_object.getDescription());
        previous.setEnabled(step != min_step);
        next.setEnabled(step != number_of_steps);
        if (video_player != null) video_player.release();
        initializePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && video_url != null) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || video_player == null) && video_url != null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (video_player != null) {
            playback_position = video_player.getCurrentPosition();
            window_index = video_player.getCurrentWindowIndex();
            video_player.release();
            video_player = null;
        }
    }

}
