package com.example.user1.recipelist.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.user1.recipelist.R;

/**
 *  ingredient list for a recyclerview
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientBox> {

    private float font_size;
    private String[] ingredients;

    //constructor
    public  IngredientsAdapter() {
        font_size = 0.0f;
        ingredients = null;
    }

    //allows retrieving font size information
    public float getFontSize() {
        return font_size;
    }

    //event triggered on view holder creation
    @Override
    public IngredientBox onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        //calculate font size based on screen size
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        font_size = height * 0.03f;

        //assign XML resource information and return an inflated view
        int layoutIdForListItem = R.layout.ingredient_checkbox_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new IngredientBox(view);
    }

    //binds view with data from ingredient list
    @Override
    public void onBindViewHolder(IngredientBox holder, int position) {
        holder.bind(ingredients[position]);
    }

    @Override
    public int getItemCount() {
        return (ingredients == null) ? 0 : ingredients.length;
    }

    //sets step data and initializes the view
    public void setData(String[] s) {
        ingredients = s;
        notifyDataSetChanged();
    }

    //check box with a single ingredient
    class IngredientBox extends RecyclerView.ViewHolder {

        CheckBox box;

        IngredientBox(View itemView) {
            super(itemView);
            box = (CheckBox) itemView.findViewById(R.id.ingredient_checkbox);
            box.setTextSize(font_size);
        }

        void bind(String s) {
            box.setText(s);
        }
    }
}
