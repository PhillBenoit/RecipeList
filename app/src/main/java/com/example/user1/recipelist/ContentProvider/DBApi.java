package com.example.user1.recipelist.ContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.user1.recipelist.StepObject;
import com.example.user1.recipelist.RecipeObject;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 *
 */

public class DBApi {

    static public void clearDB(Context c) {
        c.getContentResolver().delete
                (DBContract.Recipes.CONTENT_URI, null, null);
        c.getContentResolver().delete
                (DBContract.Ingredients.CONTENT_URI, null, null);
        c.getContentResolver().delete
                (DBContract.Steps.CONTENT_URI, null, null);
    }

    static public void addRecipe(int id, int servings, String name, String image_url, Context c) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.Recipes.COLUMN_ID, id);
        cv.put(DBContract.Recipes.COLUMN_SERVINGS, servings);
        cv.put(DBContract.Recipes.COLUMN_NAME, name);
        cv.put(DBContract.Recipes.COLUMN_IMAGE_URL, image_url);

        c.getContentResolver().insert(DBContract.Recipes.CONTENT_URI, cv);
    }

    static public void addIngredient(int id, double quantity,
                                     String ingredient, String measure, Context c) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.Ingredients.COLUMN_ID, id);
        cv.put(DBContract.Ingredients.COLUMN_QUANTITY, quantity);
        cv.put(DBContract.Ingredients.COLUMN_INGREDIENT, ingredient);
        cv.put(DBContract.Ingredients.COLUMN_MEASURE, measure);

        c.getContentResolver().insert(DBContract.Ingredients.CONTENT_URI, cv);
    }

    static public void addStep(int recipe_id, int id, String description, String short_description,
                               String video_url, String thumbnail_url, Context c) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.Steps.COLUMN_RECIPE_ID, recipe_id);
        cv.put(DBContract.Steps.COLUMN_ID, id);
        cv.put(DBContract.Steps.COLUMN_DESCRIPTION, description);
        cv.put(DBContract.Steps.COLUMN_SHORT_DESCRIPTION, short_description);
        cv.put(DBContract.Steps.COLUMN_VIDEO_URL, video_url);
        cv.put(DBContract.Steps.COLUMN_THUMBNAIL_URL, thumbnail_url);

        c.getContentResolver().insert(DBContract.Steps.CONTENT_URI, cv);
    }

    static public RecipeObject[] getRecipes(Context c) {
        String[] projection = {DBContract.Recipes.COLUMN_ID,
                DBContract.Recipes.COLUMN_NAME};
        Cursor cursor = c.getContentResolver().query(
                DBContract.Recipes.CONTENT_URI, projection,
                null, null, null);
        if (cursor == null) return null;
        RecipeObject[] return_array = null;
        int cursor_count = cursor.getCount();
        if (cursor_count > 0) {
            return_array = new RecipeObject[cursor_count];
            cursor.moveToFirst();
            int index_id =
                    cursor.getColumnIndex(DBContract.Recipes.COLUMN_ID);
            int index_name =
                    cursor.getColumnIndex(DBContract.Recipes.COLUMN_NAME);
            int counter = 0;
            do {
                int id = cursor.getInt(index_id);
                String name = cursor.getString(index_name);
                return_array[counter++] = new RecipeObject(id, name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return return_array;
    }

    static public String[] getIngredients(Integer id, Context c) {
        String[] projection = {DBContract.Ingredients.COLUMN_QUANTITY,
                DBContract.Ingredients.COLUMN_MEASURE,
                DBContract.Ingredients.COLUMN_INGREDIENT};
        Cursor cursor = c.getContentResolver().query(
                DBContract.Ingredients.CONTENT_URI,
                projection,
                DBContract.Ingredients.COLUMN_ID + "=" + id.toString(),
                null,
                null);
        if (cursor == null) return null;
        String[] return_array = null;
        int cursor_count = cursor.getCount();
        if (cursor_count > 0) {
            return_array = new String[cursor_count];
            cursor.moveToFirst();
            int index_quantity =
                    cursor.getColumnIndex(DBContract.Ingredients.COLUMN_QUANTITY);
            int index_measure =
                    cursor.getColumnIndex(DBContract.Ingredients.COLUMN_MEASURE);
            int index_ingredient =
                    cursor.getColumnIndex(DBContract.Ingredients.COLUMN_INGREDIENT);
            int counter = 0;
            do {
                double quantity = cursor.getDouble(index_quantity);
                DecimalFormat f = new DecimalFormat("###,###.###");
                String measure = cursor.getString(index_measure);
                String ingredient = cursor.getString(index_ingredient);
                return_array[counter++] = String.format(Locale.US,"%s %s %s",
                        f.format(quantity), measure, ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return return_array;
    }

    static public StepObject[] getSteps(Integer recipe_index, Context c) {
        Cursor cursor = c.getContentResolver().query(
                DBContract.Steps.CONTENT_URI, null,
                DBContract.Steps.COLUMN_RECIPE_ID +"="+ recipe_index.toString(),
                null, DBContract.Steps.COLUMN_ID);
        if (cursor == null) return null;
        StepObject[] return_array = null;
        int cursor_count = cursor.getCount();
        if (cursor_count > 0) {
            return_array = new StepObject[cursor_count];
            cursor.moveToFirst();
            int index_id =
                    cursor.getColumnIndex(DBContract.Steps.COLUMN_ID);
            int index_description =
                    cursor.getColumnIndex(DBContract.Steps.COLUMN_DESCRIPTION);
            int index_short_description =
                    cursor.getColumnIndex(DBContract.Steps.COLUMN_SHORT_DESCRIPTION);
            int index_video_url =
                    cursor.getColumnIndex(DBContract.Steps.COLUMN_VIDEO_URL);
            int index_thumbnail_url =
                    cursor.getColumnIndex(DBContract.Steps.COLUMN_THUMBNAIL_URL);
            int counter = 0;
            do {
                int id = cursor.getInt(index_id);
                String description = cursor.getString(index_description);
                String short_description = cursor.getString(index_short_description);
                String video_url = cursor.getString(index_video_url);
                String thumbnail_url = cursor.getString(index_thumbnail_url);
                return_array[counter++] = new StepObject(id, description,
                        short_description, video_url, thumbnail_url);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return return_array;
    }

    static public StepObject getSingleStep(int r_id, int step, Context c) {
        String selection = String.format(Locale.US, "%s=%d AND %s=%d",
                DBContract.Steps.COLUMN_RECIPE_ID, r_id,
                DBContract.Steps.COLUMN_ID, step);

        Cursor cursor = c.getContentResolver().query(
                DBContract.Steps.CONTENT_URI, null,
                 selection, null, DBContract.Steps.COLUMN_ID);

        if (cursor == null || cursor.getCount() < 1) return null;
        cursor.moveToFirst();

        int index_id =
                cursor.getColumnIndex(DBContract.Steps.COLUMN_ID);
        int index_description =
                cursor.getColumnIndex(DBContract.Steps.COLUMN_DESCRIPTION);
        int index_short_description =
                cursor.getColumnIndex(DBContract.Steps.COLUMN_SHORT_DESCRIPTION);
        int index_video_url =
                cursor.getColumnIndex(DBContract.Steps.COLUMN_VIDEO_URL);
        int index_thumbnail_url =
                cursor.getColumnIndex(DBContract.Steps.COLUMN_THUMBNAIL_URL);

        int id = cursor.getInt(index_id);
        String description = cursor.getString(index_description);
        String short_description = cursor.getString(index_short_description);
        String video_url = cursor.getString(index_video_url);
        String thumbnail_url = cursor.getString(index_thumbnail_url);
        StepObject return_step = new StepObject(id, description,
                short_description, video_url, thumbnail_url);

        cursor.close();
        return return_step;
    }

}
