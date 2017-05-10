package com.batman.volleyproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private String JSON_URL;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w780";
    Bundle args;
    private ImageView imageView;
    private TextView title, tagline, genre, release_date, description, runtime, production_companies;
    private String URL = "";
    private ProgressDialog pd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        args = this.getIntent().getBundleExtra("Movie_Id");
        int id = args.getInt("ID");
        JSON_URL = "https://api.themoviedb.org/3/movie/" + id +
                "?api_key=" + Helper.getValue(this , "api_key") +
                "&append_to_response=videos";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.movie_backdrop);
        title = (TextView) findViewById(R.id.movie_title);
        tagline = (TextView) findViewById(R.id.movie_tagline);
        genre = (TextView) findViewById(R.id.movie_genre);
        release_date = (TextView) findViewById(R.id.movie_release_date);
        description = (TextView) findViewById(R.id.movie_description);
        runtime = (TextView) findViewById(R.id.movie_runtime);
        production_companies = (TextView) findViewById(R.id.movie_production_companies);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.trailer_button);

        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...!!!");
        pd.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();

                        try {
                            //Get the backdrop
                            Glide.with(DetailsActivity.this)
                                    .load(BASE_URL + response.getString("backdrop_path"))
                                    .placeholder(R.drawable.loadingplaceholder)
                                    .into(imageView);
                            //Get the movie title
                            String mov_title = response.getString("title");
                            if (!response.getBoolean("adult")) {
                                mov_title = mov_title + "(U/A)";
                            } else {
                                mov_title = mov_title + "(A)";
                            }
                            title.setText(mov_title);
                            //Get the tagline
                            if(!TextUtils.isEmpty(response.getString("tagline"))) {
                                tagline.setText("\"" + response.getString("tagline") + "\"");
                            }
                            else{
                                tagline.setText("");
                            }
                            //Get the genres
                            genre.setText(getArray(response, "genres"));
                            //Get Release Date
                            release_date.setText(response.getString("release_date"));
                            //Get Description/Overview
                            description.setText(response.getString("overview"));
                            //Get runtime
                            runtime.setText(String.format(Locale.getDefault(), "%d", response.getInt("runtime")) + " minutes");
                            //Get Production companies
                            production_companies.setText(getArray(response, "production_companies"));
                            //Get Video URL
                            JSONArray videos = response.getJSONObject("videos").getJSONArray("results");
                            URL = URL + VIDEO_URL + videos.getJSONObject(0).getString("key");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                if(error instanceof NoConnectionError || error instanceof TimeoutError) {
                    Toast.makeText(DetailsActivity.this, "Please check your connection and try again!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(error instanceof ServerError) {
                    Toast.makeText(DetailsActivity.this, "Server error! Please try again in some time!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        MySingleton.getmInstance(DetailsActivity.this).addToRequestQueue(request);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DetailsActivity", URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(intent);
            }
        });
    }

    private String getArray(JSONObject response, String com) {
        int count = 0;
        String result = "";
        try {
            JSONArray array = response.getJSONArray(com);
            while (count < array.length() - 1) {
                JSONObject ob = array.getJSONObject(count);
                result = result + ob.getString("name") + ", ";
                count++;
            }
            result = result + array.getJSONObject(count).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
