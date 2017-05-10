package com.batman.volleyproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by HP on 4/28/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private ArrayList<MovieItem> movieItems = new ArrayList<>();
    private Context context;

    public MovieListAdapter(ArrayList<MovieItem> arrayList , Context context) {
        movieItems = arrayList;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item , parent , false );
        MovieViewHolder holder = new MovieViewHolder(view , movieItems , context);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieItem item = movieItems.get(position);
        Glide.with(context)
                .load(item.getPoster_path())
                .placeholder(R.drawable.loadingplaceholder)
                .into(holder.poster);

        holder.title.setText(item.getTitle());
        holder.date.setText(" " + item.getRelease_date());
        holder.rating.setText(String.format(Locale.getDefault() , "%.1f" , item.getVote_average()));
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView title , date , rating;
        ArrayList<MovieItem> movieItemsArrayList = new ArrayList<>();
        Context context;

        public MovieViewHolder(final View itemView , ArrayList<MovieItem> arrayList , final Context context) {
            super(itemView);
            movieItemsArrayList = arrayList;
            this.context = context;

            poster = (ImageView) itemView.findViewById(R.id.movie_poster);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            rating = (TextView) itemView.findViewById(R.id.ratings);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , DetailsActivity.class);
                    Bundle args = new Bundle();
                    args.putInt("ID" , movieItemsArrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("Movie_Id" , args);
                    context.startActivity(intent);
                }
            });
        }
    }
}
