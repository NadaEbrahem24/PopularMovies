package com.example.nada.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.URL;

import com.example.nada.movies.data.MovieContract;
import com.example.nada.movies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import java.lang.ClassCastException;
import java.lang.NullPointerException;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment {
    GridView moviesposter;

    public MainActivityFragment() {
    }

    ArrayAdapter<String> MoviesAdapter;
    ArrayList<Movies> imgesurl = new ArrayList<Movies>();
    listAdapter listAdapter;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
       Intent seting=new Intent(getActivity(),SettingActivity.class);
            startActivity(seting);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);


        moviesposter = (GridView) root.findViewById(R.id.grid_movie);
        listAdapter = new listAdapter(getContext(), imgesurl);
        moviesposter.setAdapter(listAdapter);
        update_movies();

        moviesposter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Movies mov=listAdapter.getItem(position);
                ((CallBack)getActivity()).SendData(mov);

            }
        });
        return root;


    }

    private void update_movies(){


        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort=pref.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_default));
        if(sort.equalsIgnoreCase("Favorites")){
        ViewFavorites();
        }else{FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute(sort);}

    }

    @Override
    public void onStart() {
        super.onStart();
        update_movies();
    }

    @Override
    public void onResume() {
        super.onResume();
        update_movies();
    }

    private final String LOG_TAG = FetchMovies.class.getSimpleName();
    ///////////////////////View Favorites//////////////////////////////////
    public void ViewFavorites(){
        MovieDbHelper mydb=new MovieDbHelper(getContext());
        Cursor result=mydb.all();
        if(result.getCount()==0){}
        else {
            ArrayList<Movies> Favoritemovies=new ArrayList<>();

            StringBuffer buffer=new StringBuffer();
            for (int i=0;result.moveToNext();i++){
                Movies Fmovies=new Movies();
                Fmovies.set_ID(result.getString(1));
                Fmovies.setPosters(result.getString(2));
                Fmovies.setOverview(result.getString(3));
                Fmovies.setRelease_date(result.getString(4));
                Fmovies.setVote_avarg(result.getString(5));
                Fmovies.setVote_count(result.getString(7));
                Fmovies.setOriginal_title(result.getString(8));
                Favoritemovies.add(Fmovies);
                Log.v(LOG_TAG,"Favorites: "+Favoritemovies.get(i).getOriginal_title());
            }
            listAdapter.clear();
            listAdapter = new listAdapter(getContext(), Favoritemovies);
            listAdapter.notifyDataSetChanged();
            moviesposter.setAdapter(listAdapter);
        }


    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Movies[] parsejsonmove(String jsonmoviesstr) throws JSONException {


        final String OWM_LIST = "results";
        final String Over_view = "overview";
        final String Posters = "poster_path";
        final String release_date = "release_date";
        final String Original_title = "original_title";
        final String Popularity = "popularity";
        final String Vote_count = "vote_count";
        final String Vote_avarg = "vote_average";
        final String _D="id";
        JSONObject movies = new JSONObject(jsonmoviesstr);
        JSONArray detailsofmovie = movies.getJSONArray(OWM_LIST);
        Movies[] movie=new Movies[detailsofmovie.length()];
       // String[] re = new String[detailsofmovie.length()];
        for (int i = 0; i < detailsofmovie.length(); i++) {
            JSONObject index = detailsofmovie.getJSONObject(i);
            movie[i] = new Movies();
            movie[i].set_ID(index.getString(_D));
            movie[i].setPosters(index.getString(Posters));
            movie[i].setOriginal_title(index.getString((Original_title)));
            movie[i].setOverview(index.getString(Over_view));
            movie[i].setPopularity(index.getString(Popularity));
            movie[i].setRelease_date(index.getString(release_date));
            movie[i].setVote_count(index.getString(Vote_count));
            movie[i].setVote_avarg(index.getString(Vote_avarg));
           // re[i] = "http://image.tmdb.org/t/p/w185" + poster;
        }

        for (Movies s : movie) {
            Log.v(LOG_TAG, "Movie entry: " + s.getPosters());
        }

        return movie;
    }


    public class FetchMovies extends AsyncTask<String, Void, Movies[]> {


        @Override
        protected Movies[] doInBackground(String... params) {
            HttpURLConnection urlconnection = null;
            BufferedReader reader = null;
            String moviesStr = null;
            try {
                String sort = "popularity.desc";
                String format = "json";
                final String page="page";
                final String format_pram = "format";
                final String APIKEY_pram = "api_key";
                final String API_VALUE="";
                final String baseURL = "http://api.themoviedb.org/3/discover/movie?";
                final String sort_pram = "sort_by";
                Uri uribuild = Uri.parse(baseURL).buildUpon()
                        .appendQueryParameter(sort_pram, params[0])
                        .appendQueryParameter(format_pram, format)
                        .appendQueryParameter(APIKEY_pram, API_VALUE).build();

                URL url = new URL(uribuild.toString());
                urlconnection = (HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod("GET");
                urlconnection.connect();
                InputStream inputStream = urlconnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                if ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                moviesStr = buffer.toString();
                return parsejsonmove(moviesStr);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error" + e);
                return null;
            } finally {
                if (urlconnection != null) urlconnection.disconnect();
                if (reader != null) try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }


        protected void onPostExecute(Movies[] result) {
            super.onPostExecute(result);

            if (result != null) {
                listAdapter.clear();
                for (Movies mposter : result) {

                    imgesurl.add(mposter);

                }

                Log.v(LOG_TAG, "Movies links: " + imgesurl.get(0));
                listAdapter.notifyDataSetChanged();


            }
        }
    }
}
