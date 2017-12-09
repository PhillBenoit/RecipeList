package com.example.user1.recipelist.Utilities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user1.recipelist.Adapters.IngredientsAdapter;
import com.example.user1.recipelist.Adapters.StepsAdapter;
import com.example.user1.recipelist.ContentProvider.DBApi;
import com.example.user1.recipelist.R;

/**
 *
 */

public class StepsFragment extends Fragment {

    private StepsAdapter steps;
    private IngredientsAdapter ingredients;

    private int recipe_id;

    private StepsAdapter.StepsAdapterOnClickHandler ch;

    private LinearLayoutManager ingredients_lm, steps_lm;

    public StepsFragment() {}

    public void setID(int id) {
        recipe_id = id;
    }

    public void setCH(StepsAdapter.StepsAdapterOnClickHandler ch) {
        this.ch = ch;
    }

    public int getStepCount() {
        Log.d("TESTBUG frag send", Integer.toString(steps.getItemCount()));
        return steps.getItemCount();
    }

    public float getSmallFontSize() {
        return ingredients.getFontSize();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.steps_fragment, container, false);
        RecyclerView ingredients_recycler =
                (RecyclerView) root_view.findViewById(R.id.ingredient_recycler),
                steps_recycler =
                        (RecyclerView) root_view.findViewById(R.id.step_recycler);
        ingredients = new IngredientsAdapter();
        steps = new StepsAdapter(ch);

        ingredients_lm = new LinearLayoutManager(this.getContext());
        steps_lm = new LinearLayoutManager(this.getContext());

        ingredients_recycler.setLayoutManager(ingredients_lm);
        steps_recycler.setLayoutManager(steps_lm);

        ingredients_recycler.setHasFixedSize(true);
        steps_recycler.setHasFixedSize(true);

        ingredients_recycler.setAdapter(ingredients);
        steps_recycler.setAdapter(steps);

        ingredients.setData(DBApi.getIngredients(recipe_id, this.getContext()));
        steps.setData(DBApi.getSteps(recipe_id, this.getContext()));

        //step_count = steps.getItemCount();
        //Log.d("TESTBUG", Integer.toString(step_count));

        return root_view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ing_scroll", ingredients_lm.findFirstVisibleItemPosition());
        outState.putInt("step_scroll", steps_lm.findFirstVisibleItemPosition());
    }
}