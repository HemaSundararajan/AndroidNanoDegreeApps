package com.example.android.popularmovies;

/**
 * Created by hema on 27/2/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context, List<Movie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_list_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movieImageListItem);
        iconView.setAdjustViewBounds(true);
        Picasso.with(getContext()).load(movie.getMovieImageUrl()).into(iconView);
        return convertView;
    }
}
