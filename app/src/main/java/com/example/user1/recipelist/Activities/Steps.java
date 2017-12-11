package com.example.user1.recipelist.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.user1.recipelist.Adapters.StepsAdapter;
import com.example.user1.recipelist.Objects.StepObject;
import com.example.user1.recipelist.R;
import com.example.user1.recipelist.Utilities.StepsFragment;
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

public class Steps extends AppCompatActivity implements StepsAdapter.StepsAdapterOnClickHandler{

    private int r_id, window_index;
    private long playback_position;
    private StepsFragment steps;
    private boolean is_tablet;
    private SimpleExoPlayerView video_view;
    private SimpleExoPlayer video_player;
    private String recipe_title;

    TextView step_detail;
    StepObject step_detail_object;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        is_tablet = getResources().getBoolean(R.bool.isTablet);
        setContentView(R.layout.activity_steps);

        steps = new StepsFragment();
        r_id = 0;
        Intent intent = getIntent();
        String step_extra_key = this.getString(R.string.recipe_id_extra);
        if (intent.hasExtra(step_extra_key))
            r_id = intent.getIntExtra(step_extra_key, 0);
        steps.setID(r_id);
        steps.setCH(this);

        String title_extra_key = this.getString(R.string.title_extra);
        if (intent.hasExtra(title_extra_key))
            recipe_title = intent.getStringExtra(title_extra_key);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.steps_container, steps).commit();

        if (is_tablet) video_view = (SimpleExoPlayerView) findViewById(R.id.step_video);
    }

    private void initializePlayer() {
        video_player = ExoPlayerFactory.newSimpleInstance(
                this, new DefaultTrackSelector(), new DefaultLoadControl());
        video_view.setPlayer(video_player);
        video_player.setPlayWhenReady(false);
        video_player.seekTo(window_index,playback_position);
        Uri uri = Uri.parse(step_detail_object.getVideo_url());
        MediaSource mediaSource = buildMediaSource(uri);
        video_player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && step_detail_object != null) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || video_player == null) && step_detail_object != null) {
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

    @Override
    public void onClick(StepObject step) {
        if (is_tablet) {
            setStepData(step);
        } else {
            launchSingleStep(step);
        }
    }

    public void setStepData(StepObject step) {
        if (video_player != null) video_player.release();
        step_detail_object = step;
        step_detail = (TextView) findViewById(R.id.single_step_detail);
        step_detail.setTextSize(steps.getSmallFontSize());
        step_detail.setText(step.getDescription());
        initializePlayer();
    }

    public void launchSingleStep(StepObject step) {
        Context context = Steps.this;
        Class destination = SingleStep.class;
        Intent intent = new Intent(context, destination);

        String step_extra_key = this.getString(R.string.step_extra);
        intent.putExtra(step_extra_key, step.getId());

        String r_id_key = this.getString(R.string.recipe_id_extra);
        intent.putExtra(r_id_key, r_id);

        int step_count = steps.getStepCount();
        String max_steps_key = this.getString(R.string.max_steps_extra);
        intent.putExtra(max_steps_key, step_count - 1);

        String title_extra = this.getString(R.string.title_extra);
        intent.putExtra(title_extra, recipe_title);

        startActivity(intent);
    }
}
