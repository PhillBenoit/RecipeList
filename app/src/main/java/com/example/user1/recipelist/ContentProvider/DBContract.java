package com.example.user1.recipelist.ContentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table structure for the content provider
 */

public class DBContract {

    //base URI for all tables
    static final String AUTHORITY = "com.example.user1.recipelist";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //recipes table
    static final class Recipes implements BaseColumns {
        static final String TABLE_NAME = "recipes";

        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_ID = "id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_SERVINGS = "servings";
        static final String COLUMN_IMAGE_URL = "image_url";
    }

    //ingredients table
    static final class Ingredients implements BaseColumns {
        static final String TABLE_NAME = "ingredients";

        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_ID = "id";
        static final String COLUMN_INGREDIENT = "ingredient";
        static final String COLUMN_QUANTITY = "quantity";
        static final String COLUMN_MEASURE = "measure";
    }

    //steps table
    static final class Steps implements BaseColumns {
        static final String TABLE_NAME = "steps";

        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_RECIPE_ID = "recipe_id";
        static final String COLUMN_ID = "id";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        static final String COLUMN_VIDEO_URL = "video_url";
        static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
   }
}
