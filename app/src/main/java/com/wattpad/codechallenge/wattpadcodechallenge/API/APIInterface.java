package com.wattpad.codechallenge.wattpadcodechallenge.API;

import com.wattpad.codechallenge.wattpadcodechallenge.Model.StoriesModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    // get method to path of URL from where reponse would be fetched as an interface
    @GET("/api/v3/stories?offset=0&limit=10&fields=stories(id,title,cover,user)")
    Call<StoriesModel> getStoriesList();

}
