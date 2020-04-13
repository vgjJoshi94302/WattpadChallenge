package com.wattpad.codechallenge.wattpadcodechallenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wattpad.codechallenge.wattpadcodechallenge.API.APIClient;
import com.wattpad.codechallenge.wattpadcodechallenge.API.APIInterface;
import com.wattpad.codechallenge.wattpadcodechallenge.Model.StoriesModel;
import com.wattpad.codechallenge.wattpadcodechallenge.Model.Story;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Object variables for textview,searchview and progressbar
    private TextView tvInternetStatus;
    private SearchView svStories;
    private ProgressBar progressBar;

    // networkchange receiver class object
    private NetworkChangeReceiver receiver;

    //arraylist to store data fetched by api call
    private ArrayList<Story> storiesModelList = new ArrayList<>();
    //object for storieslist adapter class
    private StoriesAdapter storiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //inistantiation for textview,searchview and progressbar
        tvInternetStatus = (TextView) findViewById(R.id.tv_no_internet);
        svStories = (SearchView) findViewById(R.id.sv_stories);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //method call to register broadcast receiver
        registerBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver); // unregister broadcast receiver on activity destroy
    }

    // method to registe broadcast receiver
    void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter); // register receiver for broadcast
    }

    //method to make API call
    private void makeAPIcall() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<StoriesModel> call = apiInterface.getStoriesList();

        //making API call
        call.enqueue(new Callback<StoriesModel>() {
            @Override // on successful API call
            public void onResponse(Call<StoriesModel> call, Response<StoriesModel> response) {
                storiesModelList = response.body().getStories(); // store response to arraylist of stories
                addStoriesListToRecyclerView(); // method to show arraylist in recyclerview
                doFilterOnStoriesList(); // method to perform search filter on storylist

            }

            @Override // on failure of API call
            public void onFailure(Call<StoriesModel> call, Throwable t) {
            }
        });

    }

    // method to show storydata fetched by API call in recyclerview
    private void addStoriesListToRecyclerView() {
        storiesAdapter = new StoriesAdapter(storiesModelList, getApplicationContext());// adding storylist in storyadapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView); // object for recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // making recylerview linear vertical
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL)); // separate two records by divider
        recyclerView.setItemAnimator(new DefaultItemAnimator()); // setting animation for recycler view
        recyclerView.setAdapter(storiesAdapter); // passing story adapter to recycler view
        saveDataOffline();

        //make progressbar and textview for internet status invisible
        progressBar.setVisibility(View.INVISIBLE);
        tvInternetStatus.setVisibility(View.INVISIBLE);


    }

    // method to perform search filter on recyclerview
    private void doFilterOnStoriesList() {
        svStories.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                storiesAdapter.getFilter().filter(newText); // aaply filter method on storylist
                return false;
            }
        });
    }

    //method to save data offline using Shared Preferences
    private void saveDataOffline() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(storiesModelList);
        editor.putString("task list", json);
        editor.apply();
    }

    //method to display data offline using SharedPreferences
    private void loadDataOffline() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Story>>() {
        }.getType();
        storiesModelList = gson.fromJson(json, type);

        if (storiesModelList == null) {
            storiesModelList = new ArrayList<>();
        }
    }


    // class for broadcast receiver
    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            doNetworkCheck(context); // on receiving brodcast, do network check
        }

        //method to checkNetwork
        private void doNetworkCheck(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo(); // get network info
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    makeAPIcall(); // if internet found, make API call
                    saveDataOffline();
                } else {
                    // if no internet found, hide progressbar and set message in textview about no internet access
                    progressBar.setVisibility(View.INVISIBLE);
                    tvInternetStatus.setText("You can use this on offline mode as well");
                    loadDataOffline();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

