package com.example.android.booklistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // global variable storing search input
    public static EditText search_input = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize input view
        search_input = (EditText) findViewById(R.id.search_parameter);

        // initialize search button and start ListActivity when clicked
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listActivity = new Intent(MainActivity.this, com.example.android.booklistapp.ListActivity.class);
                startActivity(listActivity);
            }
        });
    }

}