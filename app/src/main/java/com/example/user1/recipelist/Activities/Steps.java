package com.example.user1.recipelist.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.user1.recipelist.Adapters.StepsAdapter;
import com.example.user1.recipelist.Objects.StepObject;
import com.example.user1.recipelist.R;
import com.example.user1.recipelist.Utilities.StepsFragment;

/**
 *
 */

public class Steps extends AppCompatActivity implements StepsAdapter.StepsAdapterOnClickHandler{

    private int r_id;
    private StepsFragment steps;
    private boolean is_tablet;

    TextView step_detail;

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

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.steps_container, steps).commit();
    }

    @Override
    public void onClick(StepObject step) {
        if (is_tablet) {
            setStepData(step);
        } else {
            launchSingleStep(step.getId());
        }
    }

    public void setStepData(StepObject step) {
        step_detail = (TextView) findViewById(R.id.single_step_detail);
        step_detail.setTextSize(steps.getSmallFontSize());
        step_detail.setText(step.getDescription());
    }

    public void launchSingleStep(int id) {
        Context context = Steps.this;
        Class destination = SingleStep.class;
        Intent intent = new Intent(context, destination);

        String step_extra_key = this.getString(R.string.step_extra);
        intent.putExtra(step_extra_key, id);

        String r_id_key = this.getString(R.string.recipe_id_extra);
        intent.putExtra(r_id_key, r_id);

        int step_count = steps.getStepCount();
        String max_steps_key = this.getString(R.string.max_steps_extra);
        intent.putExtra(max_steps_key, step_count - 1);

        startActivity(intent);
    }
}
