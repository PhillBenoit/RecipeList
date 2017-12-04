package com.example.user1.recipelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user1.recipelist.Adapters.IngredientsAdapter;
import com.example.user1.recipelist.Adapters.StepsAdapter;
import com.example.user1.recipelist.ContentProvider.DBApi;

/**
 *
 */

public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler{

    private int recipe_id;

    private LinearLayoutManager ingredients_lm, steps_lm;

    public StepsFragment() {
        recipe_id = 0;
    }

    public interface IndexInterface {
        void setID(int id);
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
        IngredientsAdapter ingredients = new IngredientsAdapter();
        StepsAdapter steps = new StepsAdapter(this);

        ingredients_lm = new LinearLayoutManager(this.getContext());
        steps_lm = new LinearLayoutManager(this.getContext());

        ingredients_recycler.setLayoutManager(ingredients_lm);
        steps_recycler.setLayoutManager(steps_lm);

        ingredients_recycler.setHasFixedSize(true);
        steps_recycler.setHasFixedSize(true);

        ingredients_recycler.setAdapter(ingredients);
        steps_recycler.setAdapter(steps);

        //ingredients.setData(DBApi.getIngredients(0, this.getContext()));
        //steps.setData(DBApi.getSteps(0, this.getContext()));

        return root_view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ing_scroll", ingredients_lm.findFirstVisibleItemPosition());
        outState.putInt("step_scroll", steps_lm.findFirstVisibleItemPosition());
    }

    @Override
    public void onClick(int id) {

    }
}
