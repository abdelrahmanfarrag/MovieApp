package com.example.mana.movieapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Details_Activity extends AppCompatActivity {
    TextView release,run_time,rate,title,over_view;
    ImageView poster;
    ListView listView,list2;
    trailer_adapter ta;
    review_adapter ra;
    Offline off;
    String poster_url,over_views,date,titles;
    double rates;
    int id;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_);

       set_views();
        off = new Offline(getApplicationContext());
        listView = (ListView)findViewById(R.id.list1);
        b = (Button) findViewById(R.id.btn1);
        list2 = new ListView(getApplicationContext());
        ta = new trailer_adapter(this,R.layout.single_trailer);
        ra = new review_adapter(this,R.layout.single_review);
         id = getIntent().getExtras().getInt("ID");
        new trailers().execute(String.valueOf(id));
        new reviews().execute(String.valueOf(id));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(Details_Activity.this);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ra.list.size()==0)
                        {

                        }
                        else {
                            ((ViewGroup) list2.getParent()).removeView(list2);
                        }

                    }
                });
                alert.setTitle("Reviews");
                if (ra.list.size()==0)
                {
                    alert.setMessage("No Available Reviews for the selected movie ");
                }
                else
                {
                    alert.setView(list2);

                }
                alert.show();
            }
        });



    }
    public void set_views()
    {
        release=(TextView)findViewById(R.id.release);
        run_time=(TextView)findViewById(R.id.run_time);
        rate=(TextView)findViewById(R.id.rate);
        over_view=(TextView)findViewById(R.id.over_view);
        title=(TextView)findViewById(R.id.tit);
        poster=(ImageView) findViewById(R.id.img_poster);

        String path = getIntent().getExtras().getString("path");
        poster_url = "http://image.tmdb.org/t/p/w500"+path;
        Picasso.with(this).load(poster_url).into(poster);
         over_views=getIntent().getExtras().getString("over_view");
         date = getIntent().getExtras().getString("Released");
         titles = getIntent().getExtras().getString("title");

         rates = getIntent().getExtras().getDouble("rate");

        release.setText("Released Data : " + date);
        run_time.setText("Duration : " + "120 min");
        rate.setText(String.valueOf(rates) + "/10");
        title.setText(titles);
        over_view.setText(over_views);
    }

    public void save_favourite(View view) {
        SQLiteDatabase sdb = off.getReadableDatabase();
        long res = off.insert_movie(poster_url, over_views, id, titles, rates, date);
        Cursor c = off.getData(sdb);
        int count = c.getCount();
        if (count==c.getCount()) {

            if (res==-1)
            {
                Toast.makeText(getApplicationContext(),"Already in Favourites !!",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Added to favourites Successfully !",Toast.LENGTH_LONG).show();
            }
        }
        c.close();
        sdb.close();



    }

    class trailers extends AsyncTask<String,Void,String> {
String api_key="ddacbc16669b4962d6c6c553654f7a22";
        StringBuffer buffer;


        @Override
        protected String doInBackground(String... params) {
            Uri.Builder u1 = Uri.parse("http://api.themoviedb.org/3/movie/").buildUpon()
                    .appendPath(params[0])
                    .appendPath("videos")
                    .appendQueryParameter("api_key", api_key);
            String myurl = u1.toString();
            try {
                URL u2 = new URL(myurl);
                HttpURLConnection huc = (HttpURLConnection) u2.openConnection();
                huc.setRequestMethod("GET");
                huc.connect();
                InputStream in = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                buffer = new StringBuffer();
                String resp;
                while ((resp = br.readLine()) != null) {
                    buffer.append(resp + "\n");
                }
                br.close();
                in.close();
                huc.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray jarr = jo.getJSONArray("results");
                int count = 0;
                while (count < jarr.length()) {
                    JSONObject jo2 = jarr.getJSONObject(count);
                    String link = jo2.getString("key");
                    trailers_data td = new trailers_data(link);
                    ta.add(td);
                    listView.setAdapter(ta);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    class reviews extends AsyncTask<String,Void,String>
    {
        String api_key="ddacbc16669b4962d6c6c553654f7a22";

        StringBuffer buffer;

        @Override
        protected String doInBackground(String... params) {
            Uri.Builder u1 = Uri.parse("http://api.themoviedb.org/3/movie/").buildUpon()
                    .appendPath(params[0])
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", api_key);
            String my_url = u1.toString();
            try {
                URL u2 = new URL(my_url);
                HttpURLConnection huc = (HttpURLConnection) u2.openConnection();
                huc.setRequestMethod("GET");
                huc.connect();
                InputStream in = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                buffer = new StringBuffer();
                String resp;
                while ((resp=br.readLine())!=null)
                {
                    buffer.append(resp+"\n");
                }
                br.close();
                in.close();
                huc.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            //   Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray jarr = jo.getJSONArray("results");
                int Count=0;
                while (Count<jarr.length())
                {
                    JSONObject JO = jarr.getJSONObject(Count);
                    String author = JO.getString("author");
                    String content= JO.getString("content");
                    review_encaps re =new review_encaps(content,author);
                    ra.add(re);
                    list2.setAdapter(ra);
                    Count++;


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
