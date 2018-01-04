package com.example.user1.recipelist.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.user1.recipelist.ContentProvider.DBApi;
import com.example.user1.recipelist.R;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget);

        setRemoteAdapter(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        Log.d("TESTBUG","setRemoteAdapter");
        int recipe_id = 1;
        String recipe_name = DBApi.getRecipeName(recipe_id, context);
        Intent i = new Intent(context, WidgetService.class);
        i.putExtra(context.getString(R.string.recipe_id_extra), recipe_id);
        views.setString(R.id.recipe_name_textview, "setText", recipe_name);
        views.setRemoteAdapter(R.id.ingredient_listview, i);
    }
}

