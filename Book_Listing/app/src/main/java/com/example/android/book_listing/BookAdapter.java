package com.example.android.book_listing;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Book> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.author = (TextView) view.findViewById(R.id.authors);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Book book = getItem(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        return view;
    }

    private static class ViewHolder{
        TextView title;
        TextView author;
    }
}

