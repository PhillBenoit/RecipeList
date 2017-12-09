package com.example.user1.recipelist.Utilities;

import android.content.Context;

import com.example.user1.recipelist.ContentProvider.DBApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 *
 */

public class RecipeJSONUtil {

    public static void parseJSON(String http_string_results, Context c) {
        if (http_string_results == null) return;
        try {
            DBApi.clearDB(c);

            //breaks the string up in to the main array
            JSONArray recipe_array = new JSONArray(http_string_results);

            //steps through the array to assign values
            for (int counter = 0; counter < recipe_array.length(); counter++) {
                JSONObject recipe_object = recipe_array.getJSONObject(counter);

                int id = recipe_object.getInt("id");
                int servings = recipe_object.getInt("servings");
                String name = recipe_object.getString("name");
                String image_url = recipe_object.getString("image");
                DBApi.addRecipe(id, servings, name, image_url, c);

                JSONArray steps_array = recipe_object.getJSONArray("steps");
                for (int step_counter = 0; step_counter< steps_array.length(); step_counter++) {
                    JSONObject step_object = steps_array.getJSONObject(step_counter);

                    int s_id = step_object.getInt("id");
                    String short_description = step_object.getString("shortDescription");
                    String description = step_object.getString("description");
                    String video_url = step_object.getString("videoURL");
                    String thumbnail_url = step_object.getString("thumbnailURL");
                    DBApi.addStep(id, s_id, description, short_description,
                            video_url, thumbnail_url, c);
                }

                JSONArray ingredients_array = recipe_object.getJSONArray("ingredients");
                for (int ingredient_counter = 0;
                     ingredient_counter < ingredients_array.length(); ingredient_counter++) {
                    JSONObject ingredient_object =
                            ingredients_array.getJSONObject(ingredient_counter);

                    double quantity = ingredient_object.getDouble("quantity");
                    String measure = ingredient_object.getString("measure");
                    String ingredient = ingredient_object.getString("ingredient");
                    DBApi.addIngredient(id, quantity, ingredient, measure, c);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //http://go.udacity.com/android-baking-app-json
    private static URL getURL() {
        try {
            return new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //task used to connect to the database
    public static String getData() {
        HttpURLConnection connection = null;
        String return_string = null;
        URL connection_url = getURL();
        if (connection_url == null) return null;

        try {
            connection = (HttpURLConnection) connection_url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) return_string = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (connection != null) connection.disconnect();
        return return_string;
    }
}
