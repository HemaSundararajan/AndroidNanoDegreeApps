package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.example.android.popularmovies.Data.MovieContract;

import java.util.ArrayList;

/**
 * Created by hema on 21/3/16.
 * get all details, not just movie url
 */
public class FetchFavouritesTask extends AsyncTask<Void, Void, Void> {

    private ArrayAdapter<Movie> mMovieAdapter;
    private final Context mContext;
    private ArrayList<Movie> movieDetails;

    public FetchFavouritesTask(Context context, ArrayAdapter<Movie> MovieArrayAdapter) {
        mContext = context;
        mMovieAdapter = MovieArrayAdapter;
        movieDetails = new ArrayList<Movie>();
    }

    @Override
    protected Void doInBackground(Void... params) {

        int i =0;
        Cursor locationCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (locationCursor != null && locationCursor.moveToNext()) {
            Movie movie = new Movie();
            int movieId = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            movie.setMovieId(locationCursor.getInt(movieId));
            int movieUrlId = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMG__URL);
            movie.setMovieImageUrl(locationCursor.getString(movieUrlId));
            int OriginalTitleId = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
            movie.setOriginalTitle(locationCursor.getString(OriginalTitleId));
            int synopsisId = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS);
            movie.setSynopsis(locationCursor.getString(synopsisId));
            int releaseDateId = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            movie.setReleaseDate(locationCursor.getString(releaseDateId));
            int ratingId = locationCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING);
            movie.setUserRating(locationCursor.getDouble(ratingId));
            movieDetails.add(movie);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(movieDetails != null) {
            mMovieAdapter.clear();

            for (Movie movie : movieDetails) {
                System.out.println("ImageUrlAfterExecution" + movie.getMovieImageUrl());
                mMovieAdapter.add(movie);
            }
        }
    }
}
