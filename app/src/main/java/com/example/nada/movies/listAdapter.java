package com.example.nada.movies; /**
 * Created by nada on 19/12/2015.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.nada.movies.R;

import android.support.v7.app.AppCompatActivity.*;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;

import java.util.ArrayList;

public class listAdapter extends ArrayAdapter<Movies> {
    private Context context;
    private LayoutInflater inflater;
    private final String LOG_TAG = listAdapter.class.getSimpleName();
    private ArrayList<Movies> movie;
    int cnt;
    public listAdapter(Context context, ArrayList<Movies> movie) {
        super(context, R.layout.items, movie);
        this.movie = movie;
        //this.imageUrl = new ArrayList(imgUrl);
        this.context = context;
        //Log.v(LOG_TAG, "Movies 1: " + this.imageUrl.get(0));
        //Log.v(LOG_TAG, "Movies 2: " + imgUrl.get(0));
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.v(LOG_TAG, "Movies size cons: " + imageUrl.size());
        // inflater=LayoutInflater.from(context);

    }

    /*@Override public int getCount() {

        Log.v(LOG_TAG, "Movies size: " + imageUrl.size());
        return imageUrl.size();
    }
*/
    @Override
    public int getCount() {
        cnt = super.getCount();
        return cnt;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items, null);

        }
        ImageView img = (ImageView) convertView.findViewById(R.id.img2);
        Picasso
                .with(context)
                .load(this.movie.get(position).getPosters())
                .into(img);
        Log.v(LOG_TAG, "Movies 3: " + this.movie.get(position));
        return convertView;
    }
}
