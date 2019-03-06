package com.wattpad.codechallenge.wattpadcodechallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ArrayList<Story> storiesModelList = new ArrayList<>();
    StoriesAdapter storiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeAPIcall();

        //textView = (TextView) findViewById(R.id.tv_stories);


    }

    private void makeAPIcall() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<StoriesModel> call = apiInterface.getStoriesList();
        call.enqueue(new Callback<StoriesModel>() {
            @Override
            public void onResponse(Call<StoriesModel> call, Response<StoriesModel> response) {

                storiesModelList = response.body().getStories();


                for (int i = 0; i < storiesModelList.size(); i++) {


                    Log.d("Main",i + 1 + ">" + storiesModelList.get(i).getTitle() + "\n");

                }



                storiesAdapter = new StoriesAdapter(storiesModelList,getApplicationContext());
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());


                recyclerView.setAdapter(storiesAdapter);

                    Log.d("Main","length="+storiesModelList.size());


            }

            @Override
            public void onFailure(Call<StoriesModel> call, Throwable t) {
                Log.d("Main", "fail");
            }
        });
    }
}
