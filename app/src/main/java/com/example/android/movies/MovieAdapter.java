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
        ViewHodler holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.grid_item_movie, parent, false);
            holder = new ViewHodler();
            holder.posterImage = (ImageView) convertView.findViewById(R.id.movie_poster);
            convertView.setTag(holder);
        }else{
            holder = (ViewHodler) convertView.getTag();
        }
        movieData.ResultsEntity item= (movieData.ResultsEntity) getItem(position);
        if(MoviePoster.size()!=0) {
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w300" + item.getPoster_path())
                    .into(holder.posterImage);
        }
        return convertView;
    }
    class ViewHodler {
        // declare your views here
        ImageView posterImage;
    }

}
