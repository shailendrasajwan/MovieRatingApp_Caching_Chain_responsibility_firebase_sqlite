package com.example.shailen.moviesystem2.Controller;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shailen.moviesystem2.Model.Movies;
import com.example.shailen.moviesystem2.R;
import com.example.shailen.moviesystem2.service.AppController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by shailen on 10/1/2015.
 */
public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private Movies[] movie;
    private ArrayList<Movies> movie1 = new ArrayList<Movies>();


    public MovieAdapter(Context context,Movies[] Movies){
        mContext = context;
        movie = Movies;
    }
    public MovieAdapter(Context context,ArrayList<Movies> Movies){
        mContext = context;
        Iterator<Movies> itr = Movies.iterator();
        while(itr.hasNext()){
            movie1.add(itr.next());
        }

    }

    @Override
    public int getCount() {
        //return movie.length;
        return movie1.size();
    }

    @Override
    public Object getItem(int position) {
        //return movie[position];
        return movie1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView=LayoutInflater.from(mContext).inflate(R.layout.activity_movie_list,null);
            holder = new ViewHolder();
            //holds id of  xml view for movie list
            holder.movie_image = (NetworkImageView) convertView.findViewById(R.id.movie_image);
            holder.movie_title_label = (TextView) convertView.findViewById(R.id.movie_title);
            holder.movie_short_plot_label = (TextView) convertView.findViewById(R.id.movie_short_plot);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        Movies Movies = movie1.get(position);
        //setting  value for a movie listview in a holder
        ImageLoader imL=  AppController.getInstance().getImageLoader();
        holder.movie_image.setImageUrl(Movies.getPoster(), imL);
        holder.movie_title_label.setText(Movies.getTitle());
        holder.movie_short_plot_label.setText(Movies.getShort_plot());
        return convertView;
    }

    private static class ViewHolder{
        NetworkImageView movie_image;
        TextView movie_title_label;
        TextView content_type;
        TextView movie_short_plot_label;
        TextView genre;
        TextView release_date;
    }
}
