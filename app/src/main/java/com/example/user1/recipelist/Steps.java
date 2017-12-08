package com.example.user1.recipelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user1.recipelist.Adapters.StepsAdapter;

/**
 *
 */

public class Steps extends AppCompatActivity implements StepsAdapter.StepsAdapterOnClickHandler{

    private int r_id;
    private StepsFragment steps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onClick(int id) {
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
