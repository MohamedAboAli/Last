package com.example.android.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<movieData.ResultsEntity> MoviePoster;


    public MovieAdapter(Context context, List<movieData.ResultsEntity> moviePoster) {
        this.context = context;
        MoviePoster = moviePoster;
    }

    @Override
    public int getCount() {
        return MoviePoster.size();


    }

    @Override
    public Object getItem(int position) {
        return MoviePoster.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.grid_item_movie,parent,false);
        movieData.ResultsEntity item= (movieData.ResultsEntity) getItem(position);
        ImageView poster= (ImageView) rowView.findViewById(R.id.movie_poster);


        String imageUrl = item.getPoster_path();
        Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + imageUrl).into(poster);

        return rowView;
    }
}
