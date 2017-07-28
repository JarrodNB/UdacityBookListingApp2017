package com.example.android.book_listing;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ParseBooks {

    // Parses JSON response and fills List
    public static List<Book> fetchBooks(String url) {
        URL urlSearch = null;
        try {
            urlSearch = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("ParseBooks", e.getMessage());
        }
        List<Book> books = new ArrayList<Book>();
        try {
            JSONObject object = new JSONObject(getJsonResponse(urlSearch));
            JSONArray array = object.getJSONArray("items");
            String title = "";
            String author = "";
            String website = "";
            for (int j = 0; j < array.length(); j++) {
                JSONObject book = array.getJSONObject(j);
                JSONObject bookInfo = book.getJSONObject("volumeInfo");
                if (bookInfo.has("title")){
                    title = bookInfo.getString("title");
                } else {
                    title = "Title Unavailable";
                }
                if (bookInfo.has("infoLink")){
                    website = bookInfo.getString("infoLink");
                } else {
                    website = "N/A";
                }
                if (bookInfo.has("authors")){
                    JSONArray authorArray = bookInfo.getJSONArray("authors");
                    for (int i = 0; i < authorArray.length(); i++) {
                        author += authorArray.getString(i) + ", ";
                    }
                    author = author.trim().substring(0, author.length() - 2);
                } else {
                    author = "Authors unavailable";
                }
                books.add(new Book(title, author, website));
                title = "";
                author = "";
            }
        } catch (JSONException e) {
            Log.e("ParseBooks", e.getMessage());
        }
        return books;
    }

    // Gets Json response in string format
    private static String getJsonResponse(URL url) {
        String response = "";
        if (url == null) return response;
        HttpsURLConnection connection = null;
        InputStream input = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                input = connection.getInputStream();
                response = getStringResponse(input);
            }
        } catch (IOException e) {
            Log.e("ParseBooks", e.getMessage());
        }
        connection.disconnect();
        try {
            input.close();
        } catch (IOException e) {
            Log.e("ParseBooks", e.getMessage());
        } catch (NullPointerException x) {
            Log.e("ParseBooks", x.getMessage());
        }
        return response;
    }

    // Reads from input stream
    private static String getStringResponse(InputStream input) throws IOException {
        StringBuilder response = new StringBuilder();
        if (input != null) {
            InputStreamReader reader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader bReader = new BufferedReader(reader);
            String line = bReader.readLine();
            while (line != null) {
                response.append(line);
                line = bReader.readLine();
            }
        }
        return response.toString();
    }
}
