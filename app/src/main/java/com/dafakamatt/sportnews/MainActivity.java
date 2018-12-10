package com.dafakamatt.sportnews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    // Instantiating global variables:
    private ArticleAdaptor mAdaptor;
    private static final int ARTICLE_LOADER_ID = 1;
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?section=sport&page-size=50&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loading in the listView in activity_main.xml:
        ListView articleListView = findViewById(R.id.list);

        // Loading in an instance of the article adaptor:
        mAdaptor = new ArticleAdaptor(this, new ArrayList<Article>());

        // Applying adaptor to our ListView:
        articleListView.setAdapter(mAdaptor);

        // Instantiating Loader Manager to pull REST data from the Guardian in the background
        // away from the main UI thread:
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        Log.i("LoaderMonitoring", "initLoader() called");

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article currentArticle = mAdaptor.getItem(position);
                String url = currentArticle.getUrl();
                Intent openUrlInBrowser = new Intent(Intent.ACTION_VIEW);
                openUrlInBrowser.setData(Uri.parse(url));
                startActivity(openUrlInBrowser);
            }
        });

    }

    // Create new article loader once instantiated. This will kick off the process to pull REST data
    // and begin populating data in the ListView:
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle bundle) {
        return new ArticleLoader(this,GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Article>> loader, List<Article> articles) {
        mAdaptor.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdaptor.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Article>> loader) {
        mAdaptor.clear();
    }

}
