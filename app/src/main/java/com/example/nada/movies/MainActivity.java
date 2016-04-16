package com.example.nada.movies;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.lang.ClassCastException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CallBack {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_detail) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail, new MovieDetailFragment()).commit();
            }

        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent seting = new Intent(this, SettingActivity.class);
            startActivity(seting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void SendData(Movies mov) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString("id", mov.get_ID());
            args.putString("title", mov.getOriginal_title());
            args.putString("overview", mov.getOverview());
            args.putString("pop", mov.getPopularity());
            args.putString("redate", mov.getRelease_date());
            args.putString("voteavg", mov.getVote_avarg());
            args.putString("votecount", mov.getVote_count());
            args.putString("poster", mov.getPosters());

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail, fragment)
                    .commit();
        } else {

            Intent intent = new Intent(this, MovieDetail.class);
            Bundle args = new Bundle();
            args.putString("id", mov.get_ID());
            args.putString("title", mov.getOriginal_title());
            args.putString("overview", mov.getOverview());
            args.putString("pop", mov.getPopularity());
            args.putString("redate", mov.getRelease_date());
            args.putString("voteavg", mov.getVote_avarg());
            args.putString("votecount", mov.getVote_count());
            args.putString("poster", mov.getPosters());
           /* intent.putExtra("id", mov.get_ID());
            intent.putExtra("title", mov.getOriginal_title());
            intent.putExtra("overview", mov.getOverview());
            intent.putExtra("pop", mov.getPopularity());
            intent.putExtra("redate", mov.getRelease_date());
            intent.putExtra("voteavg", mov.getVote_avarg());
            intent.putExtra("votecount", mov.getVote_count());
            intent.putExtra("poster", mov.getPosters());*/
            intent.putExtras(args);
            startActivity(intent);
        }



    }
}
