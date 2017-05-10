package com.batman.volleyproject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //TODO: To get Now Playing
    private String JSON_URL;

    //Tag for this activity
    private static final String TAG = MainActivity.class.getSimpleName();
    ArrayList<MovieItem> movieItems = new ArrayList<>();
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w780";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<MovieItem> list = new ArrayList<>();
    Toolbar toolbar;
    ProgressDialog pd;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSON_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key="
                + Helper.getValue(this , "api_key") +
                "&language=en-US";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Now Playing");

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...!!!");
        pd.show();

        errorText = (TextView) findViewById(R.id.text_error);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        list = getMovieItems();
        adapter = new MovieListAdapter(list , this);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<MovieItem> getMovieItems() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            int count = 0;
                            while(count<jsonArray.length()) {
                                JSONObject object = jsonArray.getJSONObject(count);
                                MovieItem item = new MovieItem(object.getString("title"),
                                        object.getString("release_date"),
                                        BASE_URL + object.getString("backdrop_path"),
                                        object.getInt("id"),
                                        object.getDouble("vote_average"));
                                movieItems.add(item);
                                count++;
                            }
                            adapter.notifyDataSetChanged();
                        }catch(JSONException e) {
                            Log.e(TAG , "Error is" , e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                recyclerView.setVisibility(RecyclerView.GONE);
                if(error instanceof NoConnectionError || error instanceof TimeoutError) {
                    errorText.setText("No Internet Connection!! Please Try Again!");
                }
                else if(error instanceof ServerError) {
                    errorText.setText("Something happened!! Please try again after some time!");
                }
            }
        });
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
        return movieItems;
    }
}