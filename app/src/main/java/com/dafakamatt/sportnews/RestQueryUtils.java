package com.dafakamatt.sportnews;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class RestQueryUtils {

    public static final String LOG_TAG = RestQueryUtils.class.getSimpleName();

    private RestQueryUtils() {
    }

    // This is the method called by the article loader.. Bit of a bouncing ball to follow from
    // here:
    public static ArrayList<Article> fetchArticleDataFromWeb(String requestUrl) {
        // Converting our url string into a Url Object:
        URL url = createUrl(requestUrl);
        // Getting ready to make our http request:
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input steam", e);
        }
        // Extracting the JSON data into an ArrayList, returning this to the ArticleLoader()
        // class:
        ArrayList<Article> articles = extractArticles(jsonResponse);

        return articles;
    }

    // Simple helper method to convert our URL string to a Url object, then return to
    // fetchArticleDataFromWeb:
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL: ", e);
        }
        return url;
    }

    // Helper method to create the HTTP request from the guardian
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the Url is null, no point in continuing, return empty string
        // and leave method:
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        // To to make the http request. If it fails, catch to an IOException:
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful, response code will always be 200,
            // and then read the input stream and parse the response, otherwise, error out:
            if (urlConnection.getResponseCode() == 200) {
                Log.i(LOG_TAG, "HttpRequest successfull!");
                inputStream = urlConnection.getInputStream();
                // Read our response from the input stream:
                jsonResponse = readFromSteam(inputStream);
            } else {
                // Print error (if there is one) to Logcat, so we can troubleshoot issue:
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Article JSON response");
        } finally {
            // Clean up our open connections, regardless of success/failure of request:
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        // Return our JSON response to fetchArticleDataFromWeb() method.
        return jsonResponse;
    }

    // Helper method to convert our input stream into a human readable string:
    private static String readFromSteam(InputStream inputStream) throws IOException {
        // StringBuilder object to help us build the JSON string incrementally:
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            // Reading the stream with an InputSteamReader Object:
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // Buffering the data:
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // Reading each line
            String line = reader.readLine();
            while (line != null) {
                // Appending each line to our String
                output.append(line);
                line = reader.readLine();
            }
        }
        // Returning our output to makeHttpRequest() method.
        return output.toString();
    }

    public static ArrayList<Article> extractArticles(String strJsonResponse) {

        ArrayList<Article> articles = new ArrayList<>();

        try {
            // Storing response string into a JSON Object:
            JSONObject objJsonResponse = new JSONObject(strJsonResponse);
            // Expanding a level:
            JSONObject objJsonTopLevel = objJsonResponse.getJSONObject("response");
            // Expanding a level:
            JSONArray jsonArrResults = objJsonTopLevel.getJSONArray("results");

            // Looping through each article, then adding the appropriate data into a
            // article object.
            for (int i = 0; i < jsonArrResults.length(); i++) {
                JSONObject jsonObjArticle = jsonArrResults.getJSONObject(i);
                String webTitle = jsonObjArticle.getString("webTitle");
                String date = jsonObjArticle.getString("webPublicationDate");
                String section = jsonObjArticle.getString("sectionName");
                String articleUrl = jsonObjArticle.getString("webUrl");
                articles.add(new Article(webTitle, section, date, articleUrl));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the article json response", e);
        }
        return articles;
    }

}
