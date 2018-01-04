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
import com.example.user1.recipelist.Objects.StepObject;

/**
    steps list for a recyclerview
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepCell>{

    private StepObject[] steps;
    private final StepsAdapterOnClickHandler click_handler;
    private float font_size;

    //constructor
    public StepsAdapter(StepsAdapterOnClickHandler click_handler) {
        steps = null;
        font_size = 0.0f;
        this.click_handler = click_handler;
    }

    public interface StepsAdapterOnClickHandler {
        void onClick(StepObject step);
    }

    //event triggered on view holder creation
    @Override
    public StepCell onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        //set font size based on display size
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        font_size = height * 0.05f;

        //assign XML resource information and return an inflated view
        int layoutIdForListItem = R.layout.step_cell_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new StepCell(view);
    }

    //binds view with data from the list
    @Override
    public void onBindViewHolder(StepCell holder, int position) {
        holder.bind(steps[position]);
    }

    @Override
    public int getItemCount() {
        return (steps == null) ? 0 : steps.length;
    }

    //sets data and initializes the view
    public void setData(StepObject[] s) {
        steps = s;
        notifyDataSetChanged();
    }

    //single cell for a step on the list
    class StepCell extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView cell;

        //constructor
        StepCell(View itemView) {
            super(itemView);
            cell = (TextView) itemView.findViewById(R.id.step_cell);
            itemView.setOnClickListener(this);
            cell.setTextSize(font_size);
        }

        //bind data to cell
        void bind(StepObject s) {
            cell.setText(s.getShort_description());
        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            click_handler.onClick(steps[index]);
        }
    }

}
