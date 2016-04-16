package com.example.nada.movies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nada on 25/01/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.nada.movies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    public  static final class MovieEntry implements BaseColumns{

            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
            public static final String CONTENT_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;



        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_POSTER_URL = "movie_poster_url";
        public static final String COLUMN_MOVIE_Vote_Count= "movie_rate";
        public static final String COLUMN_MOVIE_RELEASE_date = "movie_release_date";
        public static final String COLUMN_MOVIE_Vote_Avarg = "movie_duration";
        public static final String COLUMN_MOVIE_FAVORITE_OR_NOT = "movie_favorite_or_not";
        public static final String COLUMN_MOVIE_Overview = "movie_overview";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
    }
}
