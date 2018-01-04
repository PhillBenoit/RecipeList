package com.example.user1.recipelist.ContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.user1.recipelist.Objects.StepObject;
import com.example.user1.recipelist.Objects.RecipeObject;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 *  API for the content provider
 */

public class DBApi {

    //erases database
    static public void clearDB(Context c) {
        c.getContentResolver().delete
                (DBContract.Recipes.CONTENT_URI, null, null);
        c.getContentResolver().delete
                (DBContract.Ingredients.CONTENT_URI, null, null);
        c.getContentResolver().delete
                (DBContract.Steps.CONTENT_URI, null, null);
    }

    //add to recipe table
    static public void addRecipe(int id, int servings, String name, String image_url, Context c) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.Recipes.COLUMN_ID, id);
        cv.put(DBContract.Recipes.COLUMN_SERVINGS, servings);
        cv.put(DBContract.Recipes.COLUMN_NAME, name);
        cv.put(DBContract.Recipes.COLUMN_IMAGE_URL, image_url);

        c.getContentResolver().insert(DBContract.Recipes.CONTENT_URI, cv);
    }

    //add to ingredient table
    static public void addIngredient(int id, double quantity,
                                     String ingredient, String measure, Context c) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.Ingredients.COLUMN_ID, id);
        cv.put(DBContract.Ingredients.COLUMN_QUANTITY, quantity);
        cv.put(DBContract.Ingredients.COLUMN_INGREDIENT, ingredient);
        cv.put(DBContract.Ingredients.COLUMN_MEASURE, measure);

        c.getContentResolver().insert(DBContract.Ingredients.CONTENT_URI, cv);
    }

    //add to steps table
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

    //get all recipes from the recipe table and return as an array of recipe objects
    static public RecipeObject[] getRecipes(Context c) {
        //select which columns to return
        String[] projection = {DBContract.Recipes.COLUMN_ID,
                DBContract.Recipes.COLUMN_NAME};

        Cursor cursor = c.getContentResolver().query(
                DBContract.Recipes.CONTENT_URI, projection,
                null, null, null);

        //prevent processing if results did not return
        if (cursor == null) return null;

        RecipeObject[] return_array = null;
        int cursor_count = cursor.getCount();

        //prevent processing empty results
        if (cursor_count > 0) {
            return_array = new RecipeObject[cursor_count];
            cursor.moveToFirst();
            int index_id =
                    cursor.getColumnIndex(DBContract.Recipes.COLUMN_ID);
            int index_name =
                    cursor.getColumnIndex(DBContract.Recipes.COLUMN_NAME);
            int counter = 0;

            //process results
            do {
                int id = cursor.getInt(index_id);
                String name = cursor.getString(index_name);
                return_array[counter++] = new RecipeObject(id, name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return return_array;
    }

    //return ingredients table as a string array
    static public String[] getIngredients(Integer id, Context c) {

        //columns to return
        String[] projection = {DBContract.Ingredients.COLUMN_QUANTITY,
                DBContract.Ingredients.COLUMN_MEASURE,
                DBContract.Ingredients.COLUMN_INGREDIENT};

        Cursor cursor = c.getContentResolver().query(
                DBContract.Ingredients.CONTENT_URI,
                projection,
                //returns ingredients for only the selected recipe
                DBContract.Ingredients.COLUMN_ID + "=" + id.toString(),
                null,
                null);

        //prevents processing if results were not returned
        if (cursor == null) return null;

        String[] return_array = null;
        int cursor_count = cursor.getCount();

        //prevents processing empty results
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

            //format for quantity measurements
            DecimalFormat f = new DecimalFormat("###,###.###");

            //process results
            do {
                double quantity = cursor.getDouble(index_quantity);
                String measure = cursor.getString(index_measure);
                String ingredient = cursor.getString(index_ingredient);
                return_array[counter++] = String.format(Locale.US,"%s %s %s",
                        f.format(quantity), measure, ingredient);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return return_array;
    }

    //returns results from the steps table as an array of step objects
    static public StepObject[] getSteps(Integer recipe_index, Context c) {
        Cursor cursor = c.getContentResolver().query(
                DBContract.Steps.CONTENT_URI, null,
                //returns the steps only for the selected recipe
                DBContract.Steps.COLUMN_RECIPE_ID +"="+ recipe_index.toString(),
                null,
                //sorts the results by column ID
                DBContract.Steps.COLUMN_ID);

        //prevents processing if results were not returned
        if (cursor == null) return null;

        StepObject[] return_array = null;
        int cursor_count = cursor.getCount();

        //prevents processing empty results
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

            //process results
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

    //return a single step from a recipe
    static public StepObject getSingleStep(int r_id, int step, Context c) {

        //selection for specific recipe step
        String selection = String.format(Locale.US, "%s=%d AND %s=%d",
                DBContract.Steps.COLUMN_RECIPE_ID, r_id,
                DBContract.Steps.COLUMN_ID, step);

        Cursor cursor = c.getContentResolver().query(
                DBContract.Steps.CONTENT_URI, null,
                 selection, null, DBContract.Steps.COLUMN_ID);

        //prevents loading empty and null results
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

        //process result
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

    //get name of a recipe
    static public String getRecipeName(int id, Context c) {

        //selection of the recipe by id
        String selection = String.format(Locale.US, "%s=%d",
                DBContract.Recipes.COLUMN_ID, id);

        //return only the name
        String[] projection = {DBContract.Recipes.COLUMN_NAME};

        Cursor cursor = c.getContentResolver().query(
                DBContract.Recipes.CONTENT_URI, projection,
                selection, null, null);

        //prevents loading empty and null results
        if (cursor == null || cursor.getCount() < 1) return null;

        //process and return the result
        cursor.moveToFirst();
        int name_id = cursor.getColumnIndex(projection[0]);
        String return_name = cursor.getString(name_id);
        cursor.close();
        return return_name;
    }

}
