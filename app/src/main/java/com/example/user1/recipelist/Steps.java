package com.example.user1.recipelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 *
 */

public class Steps extends AppCompatActivity {

    StepsFragment steps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        steps = new StepsFragment();
        int id = 0;
        Intent intent = getIntent();
        String step_extra_key = this.getString(R.string.step_extra);
        if (intent.hasExtra(step_extra_key))
            id = intent.getIntExtra(step_extra_key, 0);
        steps.setID(id);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.steps_container, steps).commit();
    }

}
