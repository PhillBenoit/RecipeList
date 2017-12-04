package com.example.user1.recipelist.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 */

public class RecipeProvider extends ContentProvider {

    public static final int RECIPES = 100;
    public static final int INGREDIENTS = 200;
    public static final int STEPS = 300;

    private static final UriMatcher uri_matcher = buildUriMatcher();

    private DBCreator db_tables;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DBContract.AUTHORITY,
                DBContract.Recipes.TABLE_NAME, RECIPES);

        uriMatcher.addURI(DBContract.AUTHORITY,
                DBContract.Ingredients.TABLE_NAME, INGREDIENTS);

        uriMatcher.addURI(DBContract.AUTHORITY,
                DBContract.Steps.TABLE_NAME, STEPS);

        return uriMatcher;
    }

    private String matchURI(int match) {
        String table_name;

        //forks selecting the appropriate table
        switch (match) {
            case RECIPES:
                table_name = DBContract.Recipes.TABLE_NAME;
                break;
            case INGREDIENTS:
                table_name = DBContract.Ingredients.TABLE_NAME;
                break;
            case STEPS:
                table_name = DBContract.Steps.TABLE_NAME;
                break;
            default:
                table_name = null;
        }

        return table_name;
    }

    @Override
    public boolean onCreate() {
        db_tables = new DBCreator(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        String table_name = matchURI(uri_matcher.match(uri));
        if (table_name == null) throw new UnsupportedOperationException("Unknown uri: " + uri);

        final SQLiteDatabase db = db_tables.getReadableDatabase();
        Cursor return_cursor;

        return_cursor =  db.query(table_name, projection, selection,
                selectionArgs, null, null, sortOrder);

        Context c = getContext();
        if (c != null) return_cursor.setNotificationUri(c.getContentResolver(), uri);

        return return_cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String table_name = matchURI(uri_matcher.match(uri));
        if (table_name == null) throw new UnsupportedOperationException("Unknown uri: " + uri);

        final SQLiteDatabase db = db_tables.getWritableDatabase();
        Uri return_uri;

        long id = db.insert(table_name, null, contentValues);

        if ( id > 0 ) return_uri = ContentUris.withAppendedId(uri, id);
        else throw new android.database.SQLException
                ("Failed to insert row into " + table_name);

        Context c = getContext();
        if (c != null) c.getContentResolver().notifyChange(uri, null);

        return return_uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        String table_name = matchURI(uri_matcher.match(uri));
        if (table_name == null) throw new UnsupportedOperationException("Unknown uri: " + uri);

        final SQLiteDatabase db = db_tables.getWritableDatabase();

        int tasksDeleted = db.delete(table_name, selection, selectionArgs);

        Context c = getContext();
        if (tasksDeleted > 0 && c != null)
            c.getContentResolver().notifyChange(uri, null);

        return tasksDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
