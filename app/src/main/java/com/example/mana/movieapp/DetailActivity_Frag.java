package com.example.mana.movieapp;

import android.app.AlertDialog;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Created by Abdelrahman on 8/18/2016.
 */
public class DetailActivity_Frag extends Fragment {
    String path,mov_title,mov_date,move_overview,url;
    int id;
    double rate;
    TextView titles,dates,run_time,rates,over_view;
    ImageView iv;
    ListView listView,list2;
    trailer_adapter ta;
    review_adapter ra;

    Button b;
    Offline off;
    Button btn;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.detail_frag,container,false);
        titles=(TextView)v.findViewById(R.id.tit);
        dates = (TextView)v.findViewById(R.id.release);
        run_time=(TextView)v.findViewById(R.id.run_time);
        rates=(TextView)v.findViewById(R.id.rate);
        iv = (ImageView)v.findViewById(R.id.img_poster);
        over_view=(TextView)v.findViewById(R.id.over_view);
        listView=(ListView)v.findViewById(R.id.list1);
        list2 = new ListView(getActivity());
        ta= new trailer_adapter(getActivity(),R.layout.single_trailer);
        ra = new review_adapter(getActivity(),R.layout.single_review);
         b = (Button)v.findViewById(R.id.btn);
        off = new Offline(getActivity());
        btn = (Button)v.findViewById(R.id.btn1);
        try {
            Bundle b1 = this.getArguments();
            // Get Views Data from Bundle
            path = b1.getString("path");
            id = b1.getInt("id");
            mov_date = b1.getString("date");
            move_overview = b1.getString("oview");
            mov_title = b1.getString("title");
            rate = b1.getDouble("rate");

            // Set View Data
             url = "http://image.tmdb.org/t/p/w500" + path;
            Picasso.with(getActivity()).load(url).into(iv);
            dates.setText("Date : " + mov_date);
            over_view.setText("Over View : " + "\n" + move_overview);
            titles.setText(mov_title);
            rates.setText("Rate : " + String.valueOf(rate) + "/10");
        }
        catch (Exception e)
        {

        }

        ta.clear();
        new trailers().execute(String.valueOf(id));
        // Add movie to Favourites
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase sdb = off.getReadableDatabase();
                long res = off.insert_movie(url, move_overview, id, mov_title, rate, mov_date);
                Cursor c = off.getData(sdb);
                int count = c.getCount();
                if (count == c.getCount()) {
                    if (res == -1) {
                        Toast.makeText(getActivity(), "Already in Favourites !!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Added to favourites Successfully !", Toast.LENGTH_LONG).show();
                    }
                }}
        });
        //Show Movie Trailers
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                trailers_data td = (trailers_data) adapterView.getItemAtPosition(i);
                String link = td.getLink();
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + link));
                startActivity(in);
            }
        });

        //Get Reviews
        ra.clear();
        new reviews().execute(String.valueOf(id));




        //Show a review in an alert Dialog
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ra.list.size() == 0) {

                        } else {
                            ((ViewGroup) list2.getParent()).removeView(list2);
                        }}
                });
                alert.setTitle("Reviews");

                if (ra.list.size() == 0) {
                    alert.setMessage("No Available Reviews for the selected movie ");
                } else {
                    alert.setView(list2);

                }
                alert.show();
            }
        });
        return v;
    }

    class trailers extends AsyncTask<String, Void, String> {
                String api_key="ddacbc16669b4962d6c6c553654f7a22";

                StringBuffer buffer;


                @Override
                protected String doInBackground(String... params) {
                    Uri.Builder u1 = Uri.parse("http://api.themoviedb.org/3/movie/").buildUpon()
                            .appendPath(params[0])
                            .appendPath("videos")
                            .appendQueryParameter("api_key",api_key);
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
                            JSONObject j = jarr.getJSONObject(count);
                            String link = j.getString("key");
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

            class reviews extends AsyncTask<String, Void, String> {
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
                    //   Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jo = new JSONObject(s);
                        JSONArray jarr = jo.getJSONArray("results");
                        int Count = 0;
                        while (Count < jarr.length()) {
                            JSONObject JO = jarr.getJSONObject(Count);
                            String author = JO.getString("author");
                            String content = JO.getString("content");
                            review_encaps re = new review_encaps(content, author);

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
