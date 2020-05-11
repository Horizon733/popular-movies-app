package com.example.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.example.popularmovies.model.Movie;

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
    private static final String MOVIE_DETAILS = "movie";
    private static final String NAME = "title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "poster_path";

    public static List<Movie> parseMovieJson(String json) {
        List<Movie> movieList = new ArrayList<>();
        if (json == null || json.isEmpty()) {
            return null;
        }
        Movie movie = new Movie();
        try {
            JSONObject movieBaseJson = new JSONObject(json);
            JSONObject movieDetails = movieBaseJson.getJSONObject(MOVIE_DETAILS);
            String movieName = movieDetails.getString(NAME);
            movie.setMovieName(movieName);
            String moviePoster = movieDetails.getString(POSTER_PATH);
            movie.setMoviePoster(moviePoster);
            String overview = movieDetails.getString(OVERVIEW);
            movie.setOverview(overview);
            String release_date = movieDetails.getString(RELEASE_DATE);
            movie.setReleaseDate(release_date);
            float vote_average = movieDetails.getLong(VOTE_AVERAGE);
            movie.setVoteAverage(vote_average);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieList.add(movie);
        return movieList;
    }

    private static URL createUrl(String stringUrl, String prefrences) {
        URL url = null;
        Uri baseUri = Uri.parse(stringUrl + prefrences);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        String api_key = "8732a1a334f8c7b9de6b5f76160cf31b";
        uriBuilder.appendQueryParameter("api_key", api_key)
                .appendQueryParameter("language", "en-US")
                .build();
        try {
            url = new URL(uriBuilder.toString());

        } catch (MalformedURLException e) {
            Log.e("MainActivity", "problem building url ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
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
