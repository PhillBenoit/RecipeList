package com.example.user1.recipelist.Widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user1.recipelist.ContentProvider.DBApi;
import com.example.user1.recipelist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * WidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    //private static final String TAG = "WidgetDataProvider";

    private List<String> mCollection = new ArrayList<>();
    private Context mContext = null;
    private int recipe_index;

    WidgetDataProvider(Context context, Intent intent) {
        if (intent.hasExtra(context.getString(R.string.recipe_id_extra))) {
            recipe_index = intent.getIntExtra(
                    context.getString(R.string.recipe_id_extra), 1);
        } else {
            recipe_index = 1;
        }
        mContext = context;
    }

    @Override
    public void onCreate() {
        Log.d("TESTBUG", "INIT");
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("TESTBUG", "count " + Integer.toString(mCollection.size()));
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, mCollection.get(position));
        Log.d("TESTBUG", "getViewAt " + Integer.toString(position));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        mCollection.clear();
        String ingredients[] = DBApi.getIngredients(recipe_index, mContext);
        if (ingredients != null) {
            for(String ingredient: ingredients) {
                mCollection.add(ingredient);
                Log.d("TESTBUG", "add " + ingredient);
            }
        }

    }

}
