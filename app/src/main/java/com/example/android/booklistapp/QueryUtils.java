package com.example.android.booklistapp;

import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnj on 11/2/16.
 */

public class QueryUtils {

    // set LOG_TAG constant
    public static final String LOG_TAG = MainActivity.class.getName();

    // retrieve requested data
    public static List<Book> fetchData(String urlString) {

        // convert url string to URL object
        URL url = createURL(urlString);
        String jsonResponse = "";

        // run url and store results in string
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "http request error", e);
        }

        // filter for required data and store in List
        List<Book> bookList = extractData(jsonResponse);

        return bookList;
    }

    // convert url
    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "malformed url", e);
        }
        return url;
    }

    // http request
    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream;
        String jsonResponse = "";
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "connection error", e);
        }
        return jsonResponse;
    }

    // stream reader
    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "input stream reader error", e);
        }
        return output.toString();
    }

    // filter data
    private static List<Book> extractData(String data) {

        List<Book> bookList = new ArrayList<>();

        if (data == null) {
            return null;
        }
        try {
            JSONObject root = new JSONObject(data);
            JSONArray items = root.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject element = items.getJSONObject(i);
                JSONObject info = element.getJSONObject("volumeInfo");

                String title = info.getString("title");
                String url = element.getString("selfLink");
                String author = "";
                if (info.has("authors")) {
                    author = info.getString("authors");
                } else {
                    author = " No author provided ";
                }
                Book book = new Book(title, author, url);

                bookList.add(book);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "json parsing error", e);
        }
        return bookList;
    }

}

