package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    Movie movie;
    private ArrayAdapter<String> reviewArrayAdapter;
    private ArrayAdapter<String> trailersArrayAdapter;
    private FetchReviewsTask fetchReviewsTask;
    private FetchTrailersTask fetchTrailersTask;
    Button favButton;
    public MovieDetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchReviewsTask = new FetchReviewsTask(movie,reviewArrayAdapter);
        fetchTrailersTask = new FetchTrailersTask(movie,trailersArrayAdapter);
        fetchReviewsTask.execute();
        fetchTrailersTask.execute();
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
         Intent movieIntent = getActivity().getIntent();
        if (movieIntent != null) {
            movie = (Movie) movieIntent.getSerializableExtra("Movie");
            movie.setReviews(new ArrayList<String>());
            movie.setTrailerUrl(new ArrayList<String>());
        }
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        reviewArrayAdapter =new ArrayAdapter<String> (getContext(),R.layout.review_list_item,movie.getReviews());
        trailersArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.trailer_list_item,movie.getTrailerUrl());
        if(movie != null) {

            ListView listView = (ListView) rootView.findViewById(R.id.reviewListView);
            listView.setAdapter(reviewArrayAdapter);

            ListView listView1 = (ListView) rootView.findViewById(R.id.trailerListView);
            listView1.setAdapter(trailersArrayAdapter);

            ImageView detailImageIcon = (ImageView) rootView.findViewById(R.id.movieImage);
            detailImageIcon.setAdjustViewBounds(true);
            Picasso.with(getContext()).load(movie.getMovieImageUrl()).into(detailImageIcon);

            TextView detailTextView = (TextView) rootView.findViewById(R.id.detail_title);
            detailTextView.setText(movie.getOriginalTitle().toString());

            TextView synopsisTextView = (TextView) rootView.findViewById(R.id.overview);
            synopsisTextView.setText(movie.getSynopsis().toString());

            TextView userRatingTextView = (TextView) rootView.findViewById(R.id.ratingBar);
            userRatingTextView.setText("User Rating : " + movie.getUserRating().toString());

            TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.releaseDate);
            releaseDateTextView.setText("Release Date : " +movie.getReleaseDate().toString());

            favButton = (Button) rootView.findViewById(R.id.favouriteButton);

            favButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (movie.getIsFavourite() == false) {
                        AddToFavouriteTask addToFavouriteTask = new AddToFavouriteTask(getContext());
                        addToFavouriteTask.execute(movie);
                        movie.setIsFavourite(true);
                        favButton.setText("Marked Favourite");
                    }
                }
            });
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String trailer = trailersArrayAdapter.getItem(position);
                    String videoId;
                    if(!trailer.isEmpty() && trailer != null) {
                        videoId = trailer.substring(trailer.indexOf(":") + 1, trailer.length());
                        if(videoId.isEmpty() || videoId == null) {
                            Toast.makeText(getActivity(), "No Trailer Present",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                                startActivity(intent);
                            }
                        }
                    }
                }
            });
        }
        return rootView;
    }
}