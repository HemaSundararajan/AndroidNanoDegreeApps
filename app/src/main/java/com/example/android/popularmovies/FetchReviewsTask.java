package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hema on 22/3/16.
 */
public class FetchReviewsTask extends AsyncTask<Void, Void, List<String>> {

    private Movie movie = null;
    private ArrayAdapter<String> reviewArrayAdapter = null;

    public FetchReviewsTask(Movie movie,ArrayAdapter<String> reviewArrayAdapter) {
        this.movie = movie;
        this.reviewArrayAdapter = reviewArrayAdapter;
    }

    public List<String> setReviews(String movieApiResponse) throws JSONException {
        JSONObject reviewJson = new JSONObject(movieApiResponse);
        List<String> reviews = new ArrayList<String>();
        JSONArray reviewArray = reviewJson.getJSONArray("results");
        if(reviewArray.length() !=0) {
            for(int i=0;i<reviewArray.length();i++) {
                reviews.add(reviewArray.getJSONObject(i).getString("content"));
            }
            movie.setReviews(reviews);
        } else {
            movie.setReviews(null);
            reviews = null;
        }
        return reviews;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        List<String> reviews = null;
        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Integer.toString(movie.getMovieId()))
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", "6d08284342d5ac2f8c6cc8e8669be4e7");

            URL reviewUrl = new URL (builder.build().toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) reviewUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                movie.setReviews(null);
            }
            else {
                String movieReviewResponse = buffer.toString();
                System.out.print(movieReviewResponse);
                reviews =setReviews(movieReviewResponse);
            }
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
        return reviews;
    }
    @Override
    public void onPostExecute(List<String> param) {
        if(param != null) {
            reviewArrayAdapter.clear();
            for (String movieReview : param) {
                if(movieReview != null) {
                    reviewArrayAdapter.add(movieReview);
                }
            }
        }
        /*else {
            reviewArrayAdapter.clear();
            reviewArrayAdapter.add("No Reviews");
        }*/
    }
}

