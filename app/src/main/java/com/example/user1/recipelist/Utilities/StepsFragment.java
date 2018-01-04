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
 *  Handles code for the fragment with the steps list
 */

public class StepsFragment extends Fragment {

    //step and ingredient lists
    private StepsAdapter steps;
    private IngredientsAdapter ingredients;

    private int recipe_id;

    private StepsAdapter.StepsAdapterOnClickHandler ch;

    private LinearLayoutManager ingredients_lm, steps_lm;

    public StepsFragment() {}

    //sets the recipe ID for content provider query
    public void setID(int id) {
        recipe_id = id;
    }

    //sets Click Handler
    public void setCH(StepsAdapter.StepsAdapterOnClickHandler ch) {
        this.ch = ch;
    }

    public int getStepCount() {
        return steps.getItemCount();
    }

    //gets font size from ingredients to be used for the right hand layout for tablet mode
    public float getSmallFontSize() {
        return ingredients.getFontSize();
    }

    @Nullable
    @Override
    //executes on view creation
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.steps_fragment, container, false);

        //set recyclerviews from XML data
        RecyclerView ingredients_recycler =
                (RecyclerView) root_view.findViewById(R.id.ingredient_recycler),
                steps_recycler =
                        (RecyclerView) root_view.findViewById(R.id.step_recycler);

        //init adapters
        ingredients = new IngredientsAdapter();
        steps = new StepsAdapter(ch);

        //init layout managers
        ingredients_lm = new LinearLayoutManager(this.getContext());
        steps_lm = new LinearLayoutManager(this.getContext());

        //attach layout managers
        ingredients_recycler.setLayoutManager(ingredients_lm);
        steps_recycler.setLayoutManager(steps_lm);

        ingredients_recycler.setHasFixedSize(true);
        steps_recycler.setHasFixedSize(true);

        //attach adapters
        ingredients_recycler.setAdapter(ingredients);
        steps_recycler.setAdapter(steps);

        //load data from content provider
        ingredients.setData(DBApi.getIngredients(recipe_id, this.getContext()));
        steps.setData(DBApi.getSteps(recipe_id, this.getContext()));

        return root_view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ing_scroll", ingredients_lm.findFirstVisibleItemPosition());
        outState.putInt("step_scroll", steps_lm.findFirstVisibleItemPosition());
    }
}
