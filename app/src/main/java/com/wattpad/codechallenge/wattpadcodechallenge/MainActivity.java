package com.wattpad.codechallenge.wattpadcodechallenge;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvNointernet;
    SearchView searchView;
    ProgressBar progressBar;

    ArrayList<Story> storiesModelList = new ArrayList<>();
    StoriesAdapter storiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNointernet = (TextView) findViewById(R.id.tv_no_internet);
        searchView = (SearchView) findViewById(R.id.search);
        progressBar = (ProgressBar) findViewById(R.id.progress);


        if (checkConnection()) {

            tvNointernet.setVisibility(View.INVISIBLE);
            makeAPIcall();

        } else {
            progressBar.setVisibility(View.INVISIBLE);

        }


    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void makeAPIcall() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<StoriesModel> call = apiInterface.getStoriesList();
        call.enqueue(new Callback<StoriesModel>() {
            @Override
            public void onResponse(Call<StoriesModel> call, Response<StoriesModel> response) {

                storiesModelList = response.body().getStories();


                for (int i = 0; i < storiesModelList.size(); i++) {


                    Log.d("Main", i + 1 + ">" + storiesModelList.get(i).getTitle() + "\n");

                }


                storiesAdapter = new StoriesAdapter(storiesModelList, getApplicationContext());
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());


                recyclerView.setAdapter(storiesAdapter);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        storiesAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                Log.d("Main", "length=" + storiesModelList.size());
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<StoriesModel> call, Throwable t) {
                Log.d("Main", "fail");

            }
        });
    }
}
