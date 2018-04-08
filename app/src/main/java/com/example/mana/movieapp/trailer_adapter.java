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
 * Created by Abdelrahman on 8/18/2016.
 */
public class trailer_adapter extends ArrayAdapter{
    List list = new ArrayList();
    Context c;
    public trailer_adapter(Context context, int resource) {
        super(context, resource);
        this.c=context;
    }


    public void add(trailers_data object) {
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
        v = convertView;
        view_holder vh;
        if (convertView == null)
        {
            LayoutInflater linf = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=linf.inflate(R.layout.single_trailer,parent,false);
            vh= new view_holder();
            vh.tv=(TextView)v.findViewById(R.id.trailer_number);
            v.setTag(vh);
        }
        else
        {
            vh = (view_holder) v.getTag();
        }
        int Count=position+1;
        vh.tv.setText("Trailer "+Count);



        return v;
    }
    @Override
    public void clear() {
        list.clear();
    }
class view_holder
{
    TextView tv;
}
}
