package com.example.android.booklistapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklistapp.QueryUtils.LOG_TAG;

public class ListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    // global variables
    private ProgressBar mProgressBar;
    private BookAdapter mAdapter;
    private String mSearchUrl;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        // create list view object
        ListView listView = (ListView) findViewById(R.id.list_view);

        // use search parameters to create searchable URL
        String mAPIurl = "https://www.googleapis.com/books/v1/volumes?q=";
        String mSearchParams = MainActivity.search_input.getText().toString();
        String paramsEncoded = "";

        try {
            paramsEncoded = URLEncoder.encode(mSearchParams, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "Unsupported encoding error", e);
        }

        mSearchUrl = mAPIurl + paramsEncoded;

        // initialize progress bar view
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // initialize empty text view
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyTextView);

        // initialize adapter as empty array list of Book objects
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

        // add onClick. takes user to json item page with more info
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = mAdapter.getItem(position);

                Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getmURL()));
                startActivity(website);
            }
        });

        // create ConnectivityManager and NetworkInfo to test network connectivity
        ConnectivityManager cnnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cnnMgr.getActiveNetworkInfo();

        // conditional responses to connectivity state
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);

            // Update empty state with no connection message
            mEmptyTextView.setText(R.string.connection_fail);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        return new BookAsyncTaskLoader(this, mSearchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mAdapter.clear();

        mProgressBar.setVisibility(View.GONE);

        // display data in adapter if results found, otherwise return no results
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            mEmptyTextView.setText(R.string.empty);
        }
        Log.v(LOG_TAG, "LoaderFinished");
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

}
