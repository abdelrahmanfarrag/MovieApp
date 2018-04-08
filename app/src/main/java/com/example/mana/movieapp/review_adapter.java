package com.example.mana.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdelrahman on 8/19/2016.
 */
public class review_adapter extends ArrayAdapter {
    List list = new ArrayList();
    Context c;
    public review_adapter(Context context, int resource) {
        super(context, resource);
        this.c=context;
    }


    public void add(review_encaps object) {
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
        data_holder dh;
        if (convertView==null)
        {
            LayoutInflater linf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v= linf.inflate(R.layout.single_review,parent,false);
            dh =new data_holder();
            dh.author=(TextView)v.findViewById(R.id.author);
            dh.content=(TextView)v.findViewById(R.id.content);
            v.setTag(dh);
        }
        else {
            dh= (data_holder) v.getTag();
        }
        review_encaps re = (review_encaps) list.get(position);
        dh.author.setText("Author : "+"  "+re.getAuthor()+"\n"+"\n");
        dh.content.setText("Content : "+"  "+re.getContent()+"\n"+"\n");
        v.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


        return v;
    }

    @Override
    public void clear() {
        list.clear();
    }

    class data_holder
    {
        TextView author,content;
    }
}
