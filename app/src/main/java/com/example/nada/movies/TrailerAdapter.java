package com.example.nada.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nada on 10/01/2016.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Trailer> trailers;
   // private ArrayList<Object> o;
    public TrailerAdapter(Context context, ArrayList<Trailer> t) {
        super(context,R.layout.trailer_item,t);
        this.context=context;
        this.trailers=t;
       // this.o=o;
        this.inflater=(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.trailer_item,null); }
        TextView trailerName=(TextView)convertView.findViewById(R.id.Trailer_name);
        trailerName.setText(trailers.get(position).getName());
        return convertView;
    }
}
