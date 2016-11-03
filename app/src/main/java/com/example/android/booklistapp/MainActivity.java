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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklistapp.QueryUtils.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private ProgressBar mProgressBar;
    private BookAdapter mAdapter;
    private String mSearchUrl;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText search_input = (EditText) findViewById(R.id.search_parameter);


        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.list_view);

                ListView listView = (ListView) findViewById(R.id.list_view);

                String mAPIurl = "https://www.googleapis.com/books/v1/volumes?q=";
                String mSearchParams = search_input.getText().toString();
                mSearchUrl = mAPIurl + mSearchParams;
                Log.v(LOG_TAG, "SEARCH PaRaMeTeR URL: " + mSearchParams);

                mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

                mEmptyTextView = (TextView) findViewById(R.id.empty_view);
                listView.setEmptyView(mEmptyTextView);

                mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Book currentBook = mAdapter.getItem(position);

                        Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getmURL()));
                        startActivity(website);
                    }
                });

                ConnectivityManager cnnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cnnMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(0, null, MainActivity.this);
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {

                    mProgressBar.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    mEmptyTextView.setText(R.string.empty);
                }
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        return new BookAsyncTaskLoader(this, mSearchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mAdapter.clear();

        mProgressBar.setVisibility(View.GONE);

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            mEmptyTextView.setText("empty");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
