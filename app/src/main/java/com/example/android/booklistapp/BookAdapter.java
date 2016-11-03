package com.example.android.booklistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dnj on 11/2/16.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
        }

        final Book currentBook = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentBook.getmTitle());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentBook.getmAuthor());

        return listItemView;
    }
}
