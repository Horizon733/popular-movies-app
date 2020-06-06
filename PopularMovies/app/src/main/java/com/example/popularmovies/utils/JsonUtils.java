package com.example.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Reviews;
import com.example.popularmovies.model.YoutubeTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String MOVIE_DETAILS = "results";
    private static final String NAME = "title";
    private static final String MOVIE_ID = "id";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACK_DROP_POSTER = "backdrop_path";

    public static List<Movie> parseMovieJson(String json) {
        ArrayList<Movie> movieList = new ArrayList<Movie>();
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JSONObject movieBaseJson = new JSONObject(json);
            JSONArray results = movieBaseJson.getJSONArray(MOVIE_DETAILS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentMovie = results.getJSONObject(i);
                int movieId = currentMovie.getInt(MOVIE_ID);
                String movieName = currentMovie.getString(NAME);
                String moviePoster = currentMovie.getString(POSTER_PATH);
                String overview = currentMovie.getString(OVERVIEW);
                String release_date = currentMovie.getString(RELEASE_DATE);
                Double vote_average = currentMovie.getDouble(VOTE_AVERAGE);
                String backDropPoster = currentMovie.getString(BACK_DROP_POSTER);

                movieList.add(new Movie(movieId, movieName, moviePoster, overview, release_date, vote_average, backDropPoster));
            }
        } catch (JSONException e) {
            Log.e("JsonException", "Problem receiving Json results: ", e);
            e.printStackTrace();
        }

        return movieList;
    }

    public static List<YoutubeTrailer> parseYoutubeLinks(String json) {
        List<YoutubeTrailer> trailers = new ArrayList<>();
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JSONObject trailerBaseJson = new JSONObject(json);
            JSONArray results = trailerBaseJson.getJSONArray(MOVIE_DETAILS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentTrailer = results.getJSONObject(i);
                String trailerKey = currentTrailer.getString("key");
                String trailerTitle = currentTrailer.getString("name");
                trailers.add(new YoutubeTrailer(trailerKey, trailerTitle));
                Log.d("title", "" + trailerTitle);
            }
        } catch (JSONException e) {
            Log.e("JsonException Trailers", "Problem receiving Json results: ", e);
            e.printStackTrace();
        }
        return trailers;
    }

    public static List<Reviews> parseReviews(String json) {
        List<Reviews> reviews = new ArrayList<>();
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JSONObject reviewsBaseJson = new JSONObject(json);
            JSONArray results = reviewsBaseJson.getJSONArray(MOVIE_DETAILS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentReview = results.getJSONObject(i);
                String author = currentReview.getString("author");
                String content = currentReview.getString("content");
                String url = currentReview.getString("url");
                reviews.add(new Reviews(author, content, url));
                Log.d("author", "" + author);
            }
        } catch (JSONException e) {
            Log.e("JsonException Reviews", "Problem receiving Json results: ", e);
            e.printStackTrace();
        }
        return reviews;
    }

    public static URL createUrl(String stringUrl, String prefrences) {
        URL url = null;
        Uri baseUri;
        if (prefrences == null) {
            baseUri = Uri.parse(stringUrl);
        } else {
            baseUri = Uri.parse(stringUrl + prefrences);
        }
        Uri.Builder uriBuilder = baseUri.buildUpon();
        String api_key = "8732a1a334f8c7b9de6b5f76160cf31b";
        //String api_key = "<api-key>";
        uriBuilder.appendQueryParameter("api_key", api_key)
                .appendQueryParameter("language", "en-US")
                .build();
        try {
            url = new URL(uriBuilder.toString());
            Log.e("MainActivity", "building url " + url);

        } catch (MalformedURLException e) {
            Log.e("MainActivity", "problem building url ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("MainActivity", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Problem retrieving the earthquake JSON results: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Movie> fetchMovieData(String requestUrl, String preferences) {
        // Create URL object
        URL url = createUrl(requestUrl, preferences);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {

            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("MainActivity", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Movie> movies = parseMovieJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return movies;
    }


}
