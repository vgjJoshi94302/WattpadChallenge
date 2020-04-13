
/*
Model class for stories list

 */
package com.wattpad.codechallenge.wattpadcodechallenge.Model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoriesModel {

    @SerializedName("stories")
    @Expose
    private ArrayList<Story> stories = null;


    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

}
