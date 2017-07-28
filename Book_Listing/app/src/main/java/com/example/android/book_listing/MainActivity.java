package com.example.android.book_listing;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String url;
    private BookAdapter adapter;
    @BindView(R.id.list_view) ListView listView;
    @BindView(R.id.search_bar) EditText searchBar;
    @BindView(R.id.empty_state) TextView emptystate;
    @BindView(R.id.bar) ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<Book> books = new ArrayList<Book>();
        adapter = new BookAdapter(this, R.layout.list_item, books);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptystate);
        Button submit = (Button) findViewById(R.id.search_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                emptystate.setText("");
                String query = searchBar.getText().toString();
                updateList(query);
            }
        });
        final Toast website = Toast.makeText(this, "Website unavailable", Toast.LENGTH_SHORT);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = adapter.getItem(position);
                if (book.getWebsiteUrl().equals("N/A")){
                    website.show();
                }
                Uri bookURI = Uri.parse(book.getWebsiteUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookURI);
                startActivity(websiteIntent);
            }
        });
    }

    // Fills ListView with query results
    private void updateList(String query) {
        if (!checkConnection()) {
            adapter.clear();
            emptystate.setText(R.string.no_connection);
            bar.setVisibility(View.GONE);
            return;
        }
        url = "https://www.googleapis.com/books/v1/volumes?q=%s";
        getLoaderManager().destroyLoader(0);
        query = prepareQuery(query);
        url = String.format(url, query);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        adapter.clear();
        bar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else emptystate.setText(R.string.no_books);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }

    // Adds +'s so API understand search query
    private String prepareQuery(String query) {
        String[] split = query.split(" ");
        String result = "";
        for (String string : split) {
            result += string + "+";
        }
        return result.substring(0, result.length() - 1);
    }

    // Checks if phone is connected to internet
    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
