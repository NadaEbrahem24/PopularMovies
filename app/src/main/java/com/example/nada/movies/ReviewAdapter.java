package com.example.nada.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nada on 10/01/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Review> reviews;
    public ReviewAdapter(Context context, ArrayList<Review> re) {
        super(context, R.layout.review_item, re);
        this.context=context;
        this.reviews=re;
        this.inflater=(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.review_item,null); }
        TextView authortxt=(TextView)convertView.findViewById(R.id.R_author);
        TextView contenttxt=(TextView)convertView.findViewById(R.id.R_content);
        authortxt.setText(this.reviews.get(position).getAuthor());
        contenttxt.setText(this.reviews.get(position).getContent());
        return convertView;

    }
}


