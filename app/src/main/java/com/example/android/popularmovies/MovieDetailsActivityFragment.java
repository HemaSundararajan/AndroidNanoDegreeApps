package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    public MovieDetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent movieIntent = getActivity().getIntent();
        if(movieIntent != null) {
            Movie movie = (Movie) movieIntent.getSerializableExtra("Movie");

            ImageView detailImageIcon = (ImageView) rootView.findViewById(R.id.movieImage);
            detailImageIcon.setAdjustViewBounds(true);
            Picasso.with(getContext()).load(movie.getMovieImageUrl()).into(detailImageIcon);

            TextView detailTextView = (TextView) rootView.findViewById(R.id.detail_title);
            detailTextView.setText(movie.getOriginalTitle().toString());

            TextView synopsisTextView = (TextView) rootView.findViewById(R.id.overview);
            synopsisTextView.setText(movie.getSynopsis().toString());

            RatingBar userRatingTextView = (RatingBar) rootView.findViewById(R.id.ratingBar);
            userRatingTextView.setRating(movie.getUserRating().floatValue() / 2);

            TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.releaseDate);
            releaseDateTextView.setText("Release Date : " +movie.getReleaseDate().toString());
        }
        return rootView;
    }

}
