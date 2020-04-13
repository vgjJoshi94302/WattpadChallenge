/*
Adapter class for recyclerview
 */
package com.wattpad.codechallenge.wattpadcodechallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wattpad.codechallenge.wattpadcodechallenge.Model.Story;

import java.util.ArrayList;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Story> storiesList;  // arraylist to hold stories data
    private ArrayList<Story> storiesListFull; // arraylist to hold searched data from stories data
    Context context;


    public StoriesAdapter(ArrayList<Story> storiesList, Context context) {
        this.storiesList = storiesList;
        this.context = context; // applicaton context to use for glide library
        storiesListFull = new ArrayList<>(storiesList);  // instantiation for filtered list
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

        // show data for each row in recylerview

        Story story = storiesList.get(position); // add each arraylist data to sotry model class
        holder.title.setText(story.getTitle()); // set title fieled for row
        holder.author.setText(story.getUser().getFullname()); // set author name for row

        Glide.with(context)
                .load(story.getCover())
                .circleCrop()
                .into(holder.cover); // show image by downloading from given URL, using glide library
    }

    @Override
    public int getItemCount() {
        return storiesList.size(); // size of arraylist
    }

    @Override
    public Filter getFilter() {
        return storiesFilter; // performs search filter on given arraylist
    }

    // this method, performs filter on every character sequence entered by user, add them to new arraylist, and shows them in recyclerview
    private Filter storiesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Story> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(storiesListFull); // if no characters entered by user, then add all storylist data in filterlist
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim(); // character sequence entered by user

                // if entered sequence found in storylist, then add them to filter list
                for (Story item : storiesListFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results; // return data to shown in recylerview
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // on completing task, clear given storylist data and add new returned data and notify about that to recyclerview
            storiesList.clear();
            storiesList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author;
        ImageView cover;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_title);
            author = (TextView) view.findViewById(R.id.tv_author);
            cover = (ImageView) view.findViewById(R.id.iv_cover);
        }
    }
}
