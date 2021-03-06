package com.example.user1.recipelist.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;

import com.example.user1.recipelist.Adapters.RecipesAdapter;
import com.example.user1.recipelist.ContentProvider.DBApi;
import com.example.user1.recipelist.Objects.RecipeObject;
import com.example.user1.recipelist.R;
import com.example.user1.recipelist.Utilities.RecipeJSONUtil;

public class RecipesList extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private static RecipesAdapter recipe_grid;
    private GridLayoutManager layout_manager;

    //Main entry point for the program
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        /*
        old tablet mode code

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int columns = (width > 600 && width > height) ? 3 : 1;
        */

        //selects number of columns for the recyclerview based on the screen mode
        boolean is_tablet = getResources().getBoolean(R.bool.isTablet);
        int columns = (is_tablet) ? 3 : 1;

        //set up recyclerview
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recipe_recycler);
        recipe_grid = new RecipesAdapter(this);
        layout_manager = new GridLayoutManager(this, columns);
        recycler.setLayoutManager(layout_manager);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(recipe_grid);

        //get new recipie list if the activity was started from scratch
        if (savedInstanceState == null) runGetRecipes();
    }

    //save scroll state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scroll", layout_manager.findFirstVisibleItemPosition());
    }

    //restore recipes and scroll position
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        new getRecipes().onPostExecute(this);
        layout_manager.scrollToPosition(savedInstanceState.getInt("scroll"));
    }

    //runs the process to get new recipes from the data source
    private void runGetRecipes() {
        new getRecipes().execute(this);
    }

    //launches hte steps for a selected recipe
    @Override
    public void onClick(RecipeObject recipe) {
        Context context = RecipesList.this;
        Class destination = Steps.class;
        Intent intent = new Intent(context, destination);
        String step_extra_key = this.getString(R.string.recipe_id_extra);
        intent.putExtra(step_extra_key, recipe.getId());
        String title_extra_key = this.getString(R.string.title_extra);
        intent.putExtra(title_extra_key, recipe.getName());
        startActivity(intent);
    }

    //gets recipes from the internet
    private static class getRecipes extends AsyncTask<Context, Context, Context> {
        @Override
        protected Context doInBackground(Context... contexts) {
            RecipeJSONUtil.parseJSON(RecipeJSONUtil.getData(), contexts[0]);
            return contexts[0];
        }

        //populate the recyclerview with retrieved data
        @Override
        protected void onPostExecute(Context context) {
            recipe_grid.setData(DBApi.getRecipes(context));
        }
    }
}
