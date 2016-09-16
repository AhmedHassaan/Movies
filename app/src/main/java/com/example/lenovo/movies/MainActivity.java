package com.example.lenovo.movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list;
    ArrayList<Movies> moviesList;
    Context context;
    ScrollView layout;
    ImageView showImage;
    TextView name,rate,date,desc;
    Save save;
    TextView showNo;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        moviesList = new ArrayList<>();
        layout=(ScrollView) findViewById(R.id.layout);
        showImage=(ImageView)findViewById(R.id.showImage);
        name=(TextView)findViewById(R.id.name);
        rate=(TextView)findViewById(R.id.rate);
        desc=(TextView)findViewById(R.id.desc);
        date=(TextView)findViewById(R.id.date);
        showNo=(TextView)findViewById(R.id.showNo);
        showNo.setVisibility(View.INVISIBLE);
        new MoviesAsyncTask().execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=4a09ef88946390c1359f633a7987bf5f");
        save=new Save(getApplicationContext());
        if(save.getSave()){
            layout.setVisibility(View.VISIBLE);
            layout.animate().cancel();
            layout.setScaleX(0);
            layout.setScaleY(0);
            layout.animate().scaleX(1).scaleY(1).setDuration(500).start();
            name.setText(save.getName());
            rate.setText("Rate: " + save.getRate());
            desc.setText("Description: " + save.getDesc());
            date.setText("Date: " + save.getDate());
            Picasso.with(getApplicationContext()).load(save.getBack()).placeholder(R.mipmap.ic_launcher).into(showImage);
            list.setVisibility(View.INVISIBLE);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                layout.setVisibility(View.VISIBLE);
                layout.animate().cancel();
                layout.setScaleX(0);
                layout.setScaleY(0);
                layout.animate().scaleX(1).scaleY(1).setDuration(500).start();
                name.setText(moviesList.get(i).getName());
                rate.setText("Rate: " + moviesList.get(i).getRate());
                desc.setText("Description: " + moviesList.get(i).getOverview());
                date.setText("Date: " + moviesList.get(i).getDate());
                Picasso.with(context).load(moviesList.get(i).getBackdrop()).placeholder(R.mipmap.ic_launcher).into(showImage);
                list.setVisibility(View.INVISIBLE);
                save.setSave(true);
                save.setDate(moviesList.get(i).getDate());
                save.setName(moviesList.get(i).getName());
                save.setBack(moviesList.get(i).getBackdrop());
                save.setDesc(moviesList.get(i).getOverview());
                save.setRate(moviesList.get(i).getRate());
            }
        });
    }

    public class MoviesAsyncTask extends AsyncTask<String, Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show Progress
            //vgvgvgvgvgvg
            dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading..", true);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(strings[0]);
                HttpResponse response = client.execute(post);
                int statue=response.getStatusLine().getStatusCode();
                if(statue>=1){
                    HttpEntity entity=response.getEntity();
                    String data= EntityUtils.toString(entity);
                    JSONObject jObj=new JSONObject(data);
                    JSONArray jArray = jObj.getJSONArray("results");//name of array at API
                    for(int i=0;i<jArray.length();i++){
                        Movies movies = new Movies();
                        JSONObject jReal=jArray.getJSONObject(i);
                        movies.setName(jReal.getString("title"));
                        movies.setImage("https://image.tmdb.org/t/p/w600" + jReal.getString("poster_path"));
                        movies.setDate(jReal.getString("release_date"));
                        movies.setOverview(jReal.getString("overview"));
                        movies.setRate(jReal.getString("vote_average"));
                        movies.setBackdrop("https://image.tmdb.org/t/p/w600" + jReal.getString("backdrop_path"));
                        moviesList.add(movies);
                    }
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss(); // dismiss Progress Dialogue
            if(aBoolean){
                MoviesAdapter adapter=new MoviesAdapter(MainActivity.this,R.layout.row,moviesList);
                list.setAdapter(adapter);
            }
            else{
//                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                showNo.setVisibility(View.VISIBLE);
            }
        }



    }

    @Override
    public void onBackPressed() {
        save.setSave(false);
        if(layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
        }
        else
            finish();
    }
}
