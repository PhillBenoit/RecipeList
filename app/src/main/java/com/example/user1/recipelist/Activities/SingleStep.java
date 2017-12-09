package com.example.user1.recipelist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user1.recipelist.ContentProvider.DBApi;
import com.example.user1.recipelist.Objects.StepObject;
import com.example.user1.recipelist.R;

/**
 *
 */

public class SingleStep extends Activity {

    int id, step, number_of_steps, min_step = 0;

    TextView step_detail;
    Button previous, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_step);
        Intent intent = getIntent();

        String step_extra_key = this.getString(R.string.step_extra);
        String r_id_key = this.getString(R.string.recipe_id_extra);
        String max_steps_key = this.getString(R.string.max_steps_extra);

        id = intent.getIntExtra(r_id_key, 0);
        step = intent.getIntExtra(step_extra_key, 0);
        number_of_steps = intent.getIntExtra(max_steps_key, 0);

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

        setData();
    }

    public void setData() {
        StepObject step_object = DBApi.getSingleStep(id, step, this);
        if (step_object == null) return;
        step_detail.setText(step_object.getDescription());
        previous.setEnabled(step != min_step);
        next.setEnabled(step != number_of_steps);
    }

}
