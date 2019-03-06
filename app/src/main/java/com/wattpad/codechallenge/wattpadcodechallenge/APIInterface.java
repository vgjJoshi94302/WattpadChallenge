package com.wattpad.codechallenge.wattpadcodechallenge;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/api/v3/stories?offset=0&limit=30&fields=stories(id,title,cover,user)")
    Call<StoriesModel> getStoriesList();

}
