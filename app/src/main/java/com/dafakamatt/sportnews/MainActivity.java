package com.dafakamatt.sportnews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    // Instantiating global variables:
    private static final int ARTICLE_LOADER_ID = 1;
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?";
    private ArticleAdaptor mAdaptor;
    private TextView mEmptyStateTextView;
    private TextView mNoInternetTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking for internet connection, otherwise advise user to check connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Loading in progress bar
        mProgressBar = findViewById(R.id.loading_progress_bar);

        if (isConnected) {
            // Loading in the listView in activity_main.xml:
            ListView articleListView = findViewById(R.id.list);

            // Loading in an instance of the article adaptor:
            mAdaptor = new ArticleAdaptor(this, new ArrayList<Article>());

            // Should the Guardian servers not respond, applying TextView that says so:
            mEmptyStateTextView = findViewById(R.id.empty_text_view);
            articleListView.setEmptyView(mEmptyStateTextView);

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

        } else {
            // No connection was found:
            mProgressBar.setVisibility(View.GONE);
            mNoInternetTextView = findViewById(R.id.check_network);
            mNoInternetTextView.setVisibility(View.VISIBLE);
        }
    }

    // Create new article loader once instantiated. This will kick off the process to pull REST data
    // and begin populating data in the ListView:
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle bundle) {
        // Getting an instance of our shared preferences:
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Accessing our shared preferences:
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_relevance_value)
        );

        // Parsing the base URI and building it based on the what preferences the user prefers:
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "debates");
        uriBuilder.appendQueryParameter("section", "politics");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");

        Log.i("ArticleLoader", uriBuilder.toString());

        // Loading the article:
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Article>> loader, List<Article> articles) {
        mAdaptor.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdaptor.addAll(articles);
        }
        // If Articles is empty, the empty state textview will show. Setting the text here
        // so we don't show the error message while we're waiting on the response:
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(getString(R.string.empty_state_message));
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Article>> loader) {
        mAdaptor.clear();
    }

    //Inflating menu from mail.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Intent to open the Settings Activity when Settings is selected:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent articleSettingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(articleSettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


