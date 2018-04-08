package com.example.mana.movieapp;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainActivity_Frag.movie_data {
    DetailActivity_Frag frag;
    MainActivity_Frag frax;
    View frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (savedInstanceState==null)
        {
            MainActivity_Frag fr = new MainActivity_Frag();
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_1,fr).commit();
        }

    }


    @Override
    public void data(String img_path, String date, String title, String over_view, int id, double rate) {

        if (frame == null) {
            Intent in = new Intent(this, Details_Activity.class);
            in.putExtra("Released", date);
            in.putExtra("rate", rate);
            in.putExtra("ID", id);
            in.putExtra("title", title);
            in.putExtra("path", img_path);
            in.putExtra("over_view", over_view);
            Offline line = new Offline(this);
            SQLiteDatabase db = line.getReadableDatabase();
            startActivity(in);

        } else {

            DetailActivity_Frag f = new DetailActivity_Frag();
            Bundle data = new Bundle();
            data.putString("path",img_path);
            data.putString("date",date);
            data.putInt("id", id);
            data.putString("oview",over_view);
            data.putString("title",title);
            data.putDouble("rate",rate);
            f.setArguments(data);
            getFragmentManager().beginTransaction().replace(R.id.frag_2,f).commit();




        }
    }
}
