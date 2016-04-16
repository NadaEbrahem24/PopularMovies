package com.example.nada.movies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nada.movies.Movies;

/**
 * Created by nada on 25/01/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String Datatbase_Name = "Movies.db";

    public MovieDbHelper(Context context) {
        super(context, Datatbase_Name, null, 2);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY , " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT UNIQE , " +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URL + " TEXT UNIQUE , " +
                MovieContract.MovieEntry.COLUMN_MOVIE_Overview + " TEXT , " +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_date + " TEXT , " +
                MovieContract.MovieEntry.COLUMN_MOVIE_Vote_Avarg + " REAL , " +
                MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE_OR_NOT + " INTEGER DEFAULT 0 , " +
                MovieContract.MovieEntry.COLUMN_MOVIE_Vote_Count + " REAL  ," +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT " +
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean insertMovie(Movies movie) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.get_ID());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URL, movie.getPosters());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_Overview, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getOriginal_title());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_date, movie.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_Vote_Avarg, movie.getVote_avarg());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_Vote_Count, movie.getVote_count());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE_OR_NOT, 1);
        long result = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int DeleteMovie(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //return effected rows
        return db.delete(MovieContract.MovieEntry.TABLE_NAME, "movie_id = ?", new String[]{id});

    }

    public Cursor SelectMovie(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + MovieContract.MovieEntry.TABLE_NAME + " where movie_id = '" + id + "'", null);
        return result;
    }


    public Cursor all() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+MovieContract.MovieEntry.TABLE_NAME,null);
        return result;
    }
}
