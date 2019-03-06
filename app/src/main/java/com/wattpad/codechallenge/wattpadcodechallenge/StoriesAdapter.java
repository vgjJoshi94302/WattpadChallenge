package com.wattpad.codechallenge.wattpadcodechallenge;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.MyViewHolder> {

    private ArrayList<Story> storiesList;
    Context context;

    public StoriesAdapter(ArrayList<Story> storiesList, Context context)
    {
        this.storiesList = storiesList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


    Story story = storiesList.get(position);

    holder.title.setText(story.getTitle());
    holder.author.setText(story.getUser().getFullname());

        Glide.with(context)
                .load(story.getCover())
                .circleCrop()
                .into(holder.cover);

    }

    @Override
    public int getItemCount() {
        // Log.d("Story","Count"+storiesList.size());
        return storiesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author;
        ImageView cover;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            cover = (ImageView) view.findViewById(R.id.cover);
        }
    }
}
