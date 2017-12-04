package com.example.user1.recipelist.ContentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */

public class DBCreator extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "recipeDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    DBCreator(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    //creates tables as defined in the contract
    public void onCreate(SQLiteDatabase db) {
        String sql_command;

        sql_command = "CREATE TABLE "  + DBContract.Recipes.TABLE_NAME + " (" +
                DBContract.Recipes.COLUMN_ID        + " INTEGER PRIMARY KEY, " +
                DBContract.Recipes.COLUMN_NAME      + " TEXT NOT NULL, " +
                DBContract.Recipes.COLUMN_SERVINGS  + " INTEGER NOT NULL, " +
                DBContract.Recipes.COLUMN_IMAGE_URL + " TEXT);";
        db.execSQL(sql_command);

        sql_command = "CREATE TABLE "  + DBContract.Ingredients.TABLE_NAME + " (" +
                DBContract.Ingredients.COLUMN_ID         + " INTEGER NOT NULL, " +
                DBContract.Ingredients.COLUMN_INGREDIENT + " TEXT NOT NULL, " +
                DBContract.Ingredients.COLUMN_MEASURE    + " TEXT NOT NULL, " +
                DBContract.Ingredients.COLUMN_QUANTITY   + " REAL NOT NULL);";
        db.execSQL(sql_command);

        sql_command = "CREATE TABLE "  + DBContract.Steps.TABLE_NAME + " (" +
                DBContract.Steps.COLUMN_RECIPE_ID         + " INTEGER NOT NULL, " +
                DBContract.Steps.COLUMN_ID                + " INTEGER NOT NULL, " +
                DBContract.Steps.COLUMN_DESCRIPTION       + " TEXT NOT NULL, " +
                DBContract.Steps.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                DBContract.Steps.COLUMN_VIDEO_URL         + " TEXT, " +
                DBContract.Steps.COLUMN_THUMBNAIL_URL     + " TEXT);";
        db.execSQL(sql_command);
    }

    @Override
    //updates database during schema changes
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Recipes.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Ingredients.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.Steps.TABLE_NAME);
            onCreate(db);
        }
    }
}
