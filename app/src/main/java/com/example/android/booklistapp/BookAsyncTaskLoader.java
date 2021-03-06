package com.example.android.booklistapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static com.example.android.booklistapp.QueryUtils.LOG_TAG;

/**
 * Created by dnj on 11/2/16.
 */

public class BookAsyncTaskLoader extends AsyncTaskLoader {

    // url variable
    private String mUrl;

    // constructor
    public BookAsyncTaskLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "TaskLOADER URL: " + mUrl);
        forceLoad();
    }

    // run network request on background thread
    @Override
    public List<Book> loadInBackground() {
        List<Book> bookList = QueryUtils.fetchData(mUrl);
        return bookList;
    }
}
