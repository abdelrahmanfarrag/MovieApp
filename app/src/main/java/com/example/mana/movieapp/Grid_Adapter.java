package com.example.mana.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdelrahman on 8/18/2016.
 */
public class Grid_Adapter extends ArrayAdapter {
    Context c;
    List list = new ArrayList();
    public Grid_Adapter(Context context, int resource) {
        super(context, resource);
        this.c=context;
    }


    public void add(Encaps_poster object) {
        list.add(object);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        v=convertView;
        image_holder img;
        if(convertView==null)
        {
            LayoutInflater linf = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=linf.inflate(R.layout.single_image,parent,false);
            img = new image_holder();
            img.iv=(ImageView)v.findViewById(R.id.image1);
            v.setTag(img);
        }
        else
        {
        img = (image_holder) v.getTag();
        }
       Encaps_poster ep = (Encaps_poster) list.get(position);
        String img_url = "http://image.tmdb.org/t/p/w500"+ep.getImg_path();
        Picasso.with(c).load(img_url).into(img.iv);



        return v;
    }

    @Override
    public void clear() {
        list.clear();
    }

    class image_holder {
        ImageView iv;
    }
}
