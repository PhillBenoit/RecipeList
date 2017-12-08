package com.example.user1.recipelist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;

import com.example.user1.recipelist.Adapters.RecipesAdapter;
import com.example.user1.recipelist.ContentProvider.DBApi;

public class RecipesList extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private static RecipesAdapter recipe_grid;
    private GridLayoutManager layout_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int columns = (width > 600 && width > height) ? 3 : 1;

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recipe_recycler);
        recipe_grid = new RecipesAdapter(this);

        layout_manager = new GridLayoutManager(this, columns);
        recycler.setLayoutManager(layout_manager);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(recipe_grid);

        if (savedInstanceState == null) runGetRecipes();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scroll", layout_manager.findFirstVisibleItemPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        new getRecipes().onPostExecute(this);
        layout_manager.scrollToPosition(savedInstanceState.getInt("scroll"));
    }

    private void runGetRecipes() {
        new getRecipes().execute(this);
    }

    @Override
    public void onClick(int id) {
        Context context = RecipesList.this;
        Class destination = Steps.class;
        Intent intent = new Intent(context, destination);
        String step_extra_key = this.getString(R.string.recipe_id_extra);
        intent.putExtra(step_extra_key, id);
        startActivity(intent);
    }

    private static class getRecipes extends AsyncTask<Context, Context, Context> {
        @Override
        protected Context doInBackground(Context... contexts) {
            RecipeJSONUtil.parseJSON(RecipeJSONUtil.getData(), contexts[0]);
            return contexts[0];
        }

        @Override
        protected void onPostExecute(Context context) {
            recipe_grid.setData(DBApi.getRecipes(context));
        }
    }
}
