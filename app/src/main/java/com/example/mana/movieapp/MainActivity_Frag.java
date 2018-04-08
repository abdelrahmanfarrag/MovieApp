package com.example.mana.movieapp;

import android.app.Activity;
import android.app.AlertDialog;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
public class MainActivity_Frag extends Fragment {


    GridView gv;
    Grid_Adapter ad;
    movie_data test;
    Offline of;
    public interface movie_data {
        public void data(String img_path,String date,String title,String over_view,int id, double rate);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        of =  new Offline(getActivity());







    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
          this.test = (movie_data) context;
        }catch (Exception e)
        {
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.grid,container,false);
        gv = (GridView) v.findViewById(R.id.gridv);
     ad= new Grid_Adapter(getActivity(),R.layout.single_image);


        if (isOnline())
        {
            new async_posters().execute();
        }
        else
        {
            {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Internet Error")
                        .setMessage("Connect to wifi or mobile data")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        }




        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Encaps_poster ep = (Encaps_poster) parent.getItemAtPosition(i);
                String poster = ep.getImg_path();
                String date = ep.getDate();
                String title = ep.getTitle();
                String over_view = ep.getOverview();
                int id = ep.getId();
                double rate = ep.getRate();
                try {
                    d.data(poster, date, title, over_view, id, rate);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Im Exception from MainActivity_Frag", Toast.LENGTH_LONG).show();
                }
            }
        });


        return v;
    }







    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.top_rate) {
            if (isOnline()) {
                Toast.makeText(getActivity(), "Loading........ ", Toast.LENGTH_LONG).show();
                new Top_Rated().execute();
            } else {
                Toast.makeText(getActivity(), "No Internet Connection ", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.most) {
            if (isOnline()) {
                Toast.makeText(getActivity(), "Loading........ ", Toast.LENGTH_LONG).show();
                new async_posters().execute();
            } else {
                Toast.makeText(getActivity(), "No Internet Connection ", Toast.LENGTH_LONG).show();

            }
        } else if (id == R.id.favourite) {
            Toast.makeText(getActivity(), "Loading........ ", Toast.LENGTH_LONG).show();
            ad.clear();
            SQLiteDatabase db = of.getReadableDatabase();
            Cursor c = of.getData(db);

            if (c!=null)
                while (c.moveToNext()) {
                    String Poster = c.getString(0);
                    String overview = c.getString(1);
                    int movie_id = c.getInt(2);
                    String title = c.getString(3);
                    double rate = c.getDouble(4);
                    String data = c.getString(5);
                    Encaps_poster e = new Encaps_poster(Poster, overview, data, title, movie_id, rate);
                    ad.add(e);
                    //     Toast.makeText(getActivity(),overview,Toast.LENGTH_LONG).show();
                    gv.setAdapter(ad);




                }
            c.close();
            db.close();



        }
            return super.onOptionsItemSelected(item);
        }




    class async_posters extends AsyncTask<String,Void,String> {
        String url;
        StringBuffer buffer;

        @Override
        protected void onPreExecute() {
            url = "http://api.themoviedb.org/3/movie/popular?api_key=ddacbc16669b4962d6c6c553654f7a22";
            ad.clear();

        }

        @Override
        protected String doInBackground(String... strings) {


                try {
                    URL u1 = new URL(url);
                    HttpURLConnection huc = (HttpURLConnection) u1.openConnection();
                    huc.setRequestMethod("GET");
                    huc.connect();
                    InputStream in = huc.getInputStream();
                    if (in == null) {
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        buffer = new StringBuffer();
                        String resp;
                        while ((resp = br.readLine()) != null) {

                            buffer.append(resp + "\n");

                        }
                        br.close();
                        huc.disconnect();
                    }
                }catch(MalformedURLException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }



                return buffer.toString();
            }




        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray ja = jo.getJSONArray("results");
                int count=0;
                while (count<ja.length())
                {
                    JSONObject jobj = ja.getJSONObject(count);
                    String image = jobj.getString("poster_path");
                    String date = jobj.getString("release_date");
                    String over_view = jobj.getString("overview");
                    int id = jobj.getInt("id");
                    double rate = jobj.getDouble("vote_average");
                    String title = jobj.getString("original_title");
                    Encaps_poster ep = new Encaps_poster(image,over_view,date,title,id,rate);
                    ad.add(ep);
                    gv.setAdapter(ad);
                    count++;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }
    class Top_Rated extends AsyncTask<String,Void,String>
    {
        String url;
        StringBuffer buffer2;

        @Override
        protected void onPreExecute() {
            url= "http://api.themoviedb.org/3/movie/top_rated?api_key=ddacbc16669b4962d6c6c553654f7a22";
            ad.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL u1 = new URL(url);
                HttpURLConnection huc = (HttpURLConnection) u1.openConnection();
                huc.setRequestMethod("GET");
                huc.connect();
                InputStream in = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                buffer2 = new StringBuffer();
                String resp;
                while ((resp=br.readLine())!=null)
                {
                    buffer2.append(resp+"\n");
                }
                br.close();
                in.close();
                huc.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer2.toString();
        }

        @Override
        protected void onPostExecute(String s) {
         //   Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray ja = jo.getJSONArray("results");
                int count =0;
                while (count<ja.length())
                {
                    JSONObject jobj = ja.getJSONObject(count);
                    String image = jobj.getString("poster_path");
                    String date = jobj.getString("release_date");
                    String over_view = jobj.getString("overview");
                    int id = jobj.getInt("id");
                    double rate = jobj.getDouble("vote_average");
                    String title = jobj.getString("original_title");
                    Encaps_poster ep = new Encaps_poster(image,over_view,date,title,id,rate);
                    ad.add(ep);
                    gv.setAdapter(ad);
                    count++;


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
