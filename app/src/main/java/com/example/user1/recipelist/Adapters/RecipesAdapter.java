package com.example.user1.recipelist.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user1.recipelist.R;
import com.example.user1.recipelist.Objects.RecipeObject;

/**
 *  recipes list for a recyclerview
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.TitleCell>{

    private RecipeObject[] recipes;
    private final RecipesAdapterOnClickHandler click_handler;
    private float font_size;

    //constructor
    public RecipesAdapter(RecipesAdapterOnClickHandler click_handler) {
        this.recipes = null;
        this.click_handler = click_handler;
        font_size = 0.0f;
    }

    //event triggered on view holder creation
    @Override
    public TitleCell onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        //set font size based on display size
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        font_size = height * 0.05f;

        //assign XML resource information and return an inflated view
        int layoutIdForListItem = R.layout.title_cell_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TitleCell(view);
    }

    //binds view with data from recipe list
    @Override
    public void onBindViewHolder(TitleCell holder, int position) {
        holder.bind(recipes[position]);
    }

    @Override
    public int getItemCount() {
        return (recipes == null) ? 0 : recipes.length;
    }

    public interface RecipesAdapterOnClickHandler {
        void onClick(RecipeObject recipe);
    }

    //sets data and initializes the view
    public void setData(RecipeObject[] recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    //single recipe cell
    class TitleCell extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView cell;

        //constructor
        public TitleCell(View itemView) {
            super(itemView);
            cell = (TextView) itemView.findViewById(R.id.title_cell);
            itemView.setOnClickListener(this);
            cell.setTextSize(font_size);
        }

        //binds data
        void bind(RecipeObject r) {
            cell.setText(r.getName());
        }

        //click listener
        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            click_handler.onClick(recipes[index]);
        }
    }
}
