package com.dafakamatt.sportnews;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    // Instantiating global variables:
    public static final String LOG_TAG = ArticleLoader.class.getName();

    private String mQueryUrl;

    public ArticleLoader(Context context, String queryUrl) {
        super(context);
        mQueryUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
       Log.i("LoaderMonitoring","onStartLoading() method called");
       forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {

        if(mQueryUrl == null) {
            return null;
        }

        List<Article> articles = RestQueryUtils.fetchArticleDataFromWeb(mQueryUrl);
        return articles;
    }
}
