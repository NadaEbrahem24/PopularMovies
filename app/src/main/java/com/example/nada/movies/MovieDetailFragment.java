package com.example.nada.movies;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nada.movies.data.MovieContract;
import com.example.nada.movies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    TextView detailtxt;
    TextView movieTitle;
    TextView relesedate;
    TextView votaAvarg;
    ImageView posters;
    Button favorite;
    Button check;

    public MovieDetailFragment() {
    }

    ArrayList<Trailer> trailerslist = new ArrayList<>();
    ArrayList<Review> reviewslist = new ArrayList<>();
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        favorite = (Button) root.findViewById(R.id.favorite_btn);
        check = (Button) root.findViewById(R.id.check);
        detailtxt = (TextView) root.findViewById(R.id.details);
        movieTitle = (TextView) root.findViewById(R.id.movieTitle);
        relesedate = (TextView) root.findViewById(R.id.relasedate);
        votaAvarg = (TextView) root.findViewById(R.id.vote_avarg);
        posters = (ImageView) root.findViewById(R.id.poster);
        ///////////to get valu from the intent//////////////////
        Intent i = getActivity().getIntent();
        Bundle extra = getArguments();
        if (extra == null) {
            extra = i.getExtras();
            if (extra == null) {
                return root;
            }
        }
        String movieID = extra.getString("id");
        String poster = extra.getString("poster");
        String title = extra.getString("title");
        String overview = extra.getString("overview");
        String redate = extra.getString("redate");
        String voteavag = extra.getString("voteavg");
        String votecount = extra.getString("votecount");
        String popularity = extra.getString("pop");
        final Movies thisMovie = new Movies();
        thisMovie.set_ID(movieID);
        thisMovie.setPosters(poster);
        thisMovie.setOriginal_title(title);
        thisMovie.setOverview(overview);
        thisMovie.setRelease_date(redate);
        thisMovie.setPopularity(popularity);
        thisMovie.setVote_count(votecount);
        thisMovie.setVote_avarg(voteavag);
        ///////////////////////////////////////////////////////////////
        boolean ifFavorite;
        ifFavorite = checkmovie(thisMovie.get_ID());
        if (ifFavorite == true) {
            favorite.setText("Remove it from Favorite");
        }

        //////////////////////////////////////////////////////////////////
        movieTitle.setText(title);
        relesedate.setText(redate);
        votaAvarg.setText(voteavag);
        Picasso.with(getContext()).load(poster).into(posters);
        detailtxt.setText(overview);
        //   detailtxt.setText("Title : "+title+"\n"+"OverView: "+overview);
        ListView tralerView = (ListView) root.findViewById(R.id.T_listview);
        trailerAdapter = new TrailerAdapter(getContext(), trailerslist);
        tralerView.setAdapter(trailerAdapter);
        ListView reviewView = (ListView) root.findViewById(R.id.R_listview);
        reviewAdapter = new ReviewAdapter(getContext(), reviewslist);
        reviewView.setAdapter(reviewAdapter);
        FetchTrailer fetchTrailer = new FetchTrailer();
        fetchTrailer.execute(movieID);
        FetchReviews fetchReviews = new FetchReviews();
        fetchReviews.execute(movieID);
        //////////////////////to open the trailer///////////////////////
        tralerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = trailerAdapter.getItem(position);
                String Tkey = trailer.getKey();
                Intent video = new Intent(Intent.ACTION_VIEW);
                String url = "https://www.youtube.com/watch?v=" + Tkey;
                video.setData(Uri.parse(url));
                startActivity(video);
            }
        });
        //////////////////////////////////////////////////////////////////

        favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean check;
                check = Favorite(thisMovie);
                if (check == true) {
                    favorite.setText("Remove it from Favorit");
                } else {
                    favorite.setText("Set it as favorite");
                }

            }
        });
        ///////////////////////////////////////////////////////////////////////
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDbHelper mydb = new MovieDbHelper(getContext());
                Cursor result = mydb.all();
                if (result.getCount() == 0) {
                } else {
                    StringBuffer buffer = new StringBuffer();
                    while (result.moveToNext()) {

                        buffer.append("Movie Name: " + result.getString(8) + "\n\n");
                    }
                    Msg("My Favorite Movies", buffer.toString());
                }
            }
        });
        return root;
    }
    /////////////////////////Dialog msg/////////////////////////////////////////////////////

    public void Msg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean checkmovie(String movie_id) {
        MovieDbHelper mydb = new MovieDbHelper(getContext());
        Cursor result = mydb.SelectMovie(movie_id);
        if (result.getCount() == 0) {
            return false;
        } else return true;
    }


    public boolean Favorite(Movies movies) {
        MovieDbHelper mydb = new MovieDbHelper(getContext());
        Cursor result = mydb.SelectMovie(movies.get_ID());
        if (result.getCount() == 0) {
            boolean re = mydb.insertMovie(movies);
            if (re) {
                Toast toast = Toast.makeText(getContext(), "Movie is made as Favorite", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            } else {
                return false;
            }
        } else {
            mydb.DeleteMovie(movies.get_ID());
            Toast toast = Toast.makeText(getContext(), "Movie isremoved from Favorites", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //parse fun for Trailer
    public Trailer[] parsejsontrailer(String TrailerjsonStr) throws JSONException {
        final String main_list = "results";
        final String _ID = "id";
        final String KEY = "key";
        final String Name = "name";
        final String Size = "size";
        final String Site = "site";
        final String Type = "type";
        JSONObject trailersjson = new JSONObject(TrailerjsonStr);
        JSONArray trailerArray = trailersjson.getJSONArray(main_list);
        Trailer[] trailer = new Trailer[trailerArray.length()];
        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject index = trailerArray.getJSONObject(i);
            trailer[i] = new Trailer();
            trailer[i].setId(index.getString(_ID));
            trailer[i].setKey(index.getString(KEY));
            trailer[i].setName(index.getString(Name));
            trailer[i].setSite(index.getString(Site));
            trailer[i].setSize(index.getString(Size));
            trailer[i].setType(index.getString(Type));
        }
        for (Trailer z : trailer)
            Log.v(LOG_TAG, "Trailer: " + z.getName());
        return trailer;
    }

    //parse fun fro Review
    public Review[] parsejsonReview(String ReviewjsonStr) throws JSONException {
        final String main_list = "results";
        final String _ID = "id";
        final String Author = "author";
        final String Content = "content";
        JSONObject reviewsjson = new JSONObject(ReviewjsonStr);
        JSONArray reviewsArray = reviewsjson.getJSONArray(main_list);
        Review[] review = new Review[reviewsArray.length()];
        for (int x = 0; x < reviewsArray.length(); x++) {
            JSONObject index = reviewsArray.getJSONObject(x);
            review[x] = new Review();
            review[x].setId(index.getString(_ID));
            review[x].setAuthor(index.getString(Author));
            review[x].setContent(index.getString(Content));
        }
        for (Review z : review)
            Log.v(LOG_TAG, "Review: " + z.getAuthor());
        return review;
    }

    private final String LOG_TAG = FetchTrailer.class.getSimpleName();

    ///////////////////////////////////////////////////////////////////////////////
    // Async Task class for trailer
    public class FetchTrailer extends AsyncTask<String, Void, Trailer[]> {

        @Override
        protected Trailer[] doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            InputStream inputStream = null;
            StringBuffer buffer = null;
            String TrailersStr = null;
            String ReviewsStr = null;
            String ID = params[0];
            String apikey = "api_key";
            String ApiKeyValue = "3fcab612aec23e3f34fc7048433dbd11";
            String baseUrl = "http://api.themoviedb.org/3/movie";

            try {

                Uri TrailerURL = Uri.parse(baseUrl).buildUpon()
                        .appendPath(ID)
                        .appendPath("videos")
                        .appendQueryParameter(apikey, ApiKeyValue).build();

                URL Turl = new URL(TrailerURL.toString());
                connection = (HttpURLConnection) Turl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                inputStream = connection.getInputStream();
                buffer = new StringBuffer();
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

                TrailersStr = buffer.toString();
                Log.v(LOG_TAG, "Trailer : " + TrailersStr);
                return parsejsontrailer(TrailersStr);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error" + e);
                return null;
            } finally {
                if (connection != null) connection.disconnect();
                if (reader != null) try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected void onPostExecute(Trailer[] trailer) {
            super.onPostExecute(trailer);
            if (trailer != null) {
                for (Trailer tr : trailer) {
                    trailerslist.add(tr);
                }
                trailerAdapter.notifyDataSetChanged();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Async Task for Reviews
    public class FetchReviews extends AsyncTask<String, Void, Review[]> {

        @Override
        protected Review[] doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            InputStream inputStream = null;
            StringBuffer buffer = null;
            String ReviewsStr = null;
            String ID = params[0];
            String apikey = "api_key";
            String ApiKeyValue = "3fcab612aec23e3f34fc7048433dbd11";
            String baseUrl = "http://api.themoviedb.org/3/movie";

            try {
                Uri ReviewURL = Uri.parse(baseUrl).buildUpon()
                        .appendPath(ID)
                        .appendPath("reviews")
                        .appendQueryParameter(apikey, ApiKeyValue).build();
                URL RevURL = new URL(ReviewURL.toString());
                connection = (HttpURLConnection) RevURL.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                inputStream = connection.getInputStream();
                buffer = new StringBuffer();
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

                ReviewsStr = buffer.toString();
                Log.v(LOG_TAG, "Reviews :" + ReviewsStr);
                return parsejsonReview(ReviewsStr);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error" + e);
                return null;
            } finally {
                if (connection != null) connection.disconnect();
                if (reader != null) try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            super.onPostExecute(reviews);
            for (Review re : reviews) {
                reviewslist.add(re);
            }
            reviewAdapter.notifyDataSetChanged();
        }
    }


}
