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

    public static ArrayList<Article> fetchArticleDataFromWeb(String requestUrl) {
        Log.i("LoaderMonitoring","fetchArticleDataFromWeb called");
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error closing input steam",e);
        }

        ArrayList <Article> articles = extractArticles(jsonResponse);

        return articles;
    }

    public static URL createUrl (String stringUrl) {
        URL url = null;
        try{
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL: ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the Url is null, no point in continuing, return empty string
        // and leave method:
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful, response code will always be 200,
            // and then read the input stream and parse the response, otherwise, error out:
            if(urlConnection.getResponseCode() == 200) {
                Log.i(LOG_TAG,"HttpRequest successfull!");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromSteam(inputStream);
            } else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem retrieving the Article JSON response");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromSteam(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Article> extractArticles(String articleJson) {

        ArrayList<Article> articles = new ArrayList<>();

        try {
            // Storing response string into a JSON Object:
            JSONObject jsonResponse = new JSONObject(articleJson);
            // Expanding a level:
            JSONObject jsonTopResponse = jsonResponse.getJSONObject("response");
            // Expanding a level:
            JSONArray jsonArrResults = jsonTopResponse.getJSONArray("results");

            // Looping through each article, then adding the appropriate data into a
            // article object.
            for (int i = 0; i < jsonArrResults.length(); i ++) {
                JSONObject jsonObjArticle = jsonArrResults.getJSONObject(i);
                String webTitle = jsonObjArticle.getString("webTitle");
                String date = jsonObjArticle.getString("webPublicationDate");
                String section = jsonObjArticle.getString("sectionName");
                String articleUrl = jsonObjArticle.getString("webUrl");
                articles.add(new Article(webTitle,section,date,articleUrl));
                Log.i("RestQueryUtils","Value of webTitle is: " + webTitle);

            }





        } catch (JSONException e) {
            Log.e(LOG_TAG,"Problem parsing the article json response");
        }
        return articles;
    }

}
