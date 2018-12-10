package com.dafakamatt.sportnews;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String mQueryUrl;

    // Query URL passed in here from the MainActivity:
    public ArticleLoader(Context context, String queryUrl) {
        super(context);
        mQueryUrl = queryUrl;
    }

    // forceLoad, as required by loaders.
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // Start running tools in the RestQueryUtils class.
    // When done, return articles to main activity within a List
    @Override
    public List<Article> loadInBackground() {

        if (mQueryUrl == null) {
            return null;
        }
        // Making the Rest Request in our RestQueryUtils class:
        List<Article> articles = RestQueryUtils.fetchArticleDataFromWeb(mQueryUrl);

        // Return the result of the creation of the List to our MainActivity:
        return articles;
    }
}
