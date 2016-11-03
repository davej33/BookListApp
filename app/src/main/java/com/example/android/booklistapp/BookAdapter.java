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

    // constructor
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // display list items, inflate if not already
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
        }

        // create Book instance position
        final Book currentBook = getItem(position);

        // create title textview object and set to book instance title
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentBook.getmTitle());

        // create author textview object. parse author data and set.
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(removePunctuation(currentBook.getmAuthor()));

        return listItemView;
    }

    private String removePunctuation(String author) {

        // convert string to character array
        char[] stringToChar = author.toCharArray();

        // create StringBuilder object
        StringBuilder ss = new StringBuilder();

        // interate through each char[] element. starting at index 1 and ignoring final index removes square brackets
        for (int i = 1; i < (stringToChar.length - 1); i++) {
            String s = Character.toString(stringToChar[i]);

            // remove quotes
            if (s.matches("\"")) {
                continue;
            }

            // add all other char to StringBuilder obj
            ss.append(s);
        }

        return ss.toString();

    }
}

