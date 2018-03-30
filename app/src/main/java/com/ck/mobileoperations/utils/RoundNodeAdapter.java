package com.ck.mobileoperations.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ck.mobileoperations.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenkai on 2018/1/31.
 */

public class RoundNodeAdapter extends BaseAdapter {

    Context context;
    private List<String> list=new ArrayList<>();
    private LayoutInflater inflater;

    public RoundNodeAdapter(Context context, List<String> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder=new Holder();
            convertView=inflater.inflate(R.layout.item_listview,null);
            holder.nodename=(TextView)convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        }else {
            holder=(Holder)convertView.getTag();
        }
        holder.nodename.setText(list.get(position));
        return convertView;
    }
    protected class Holder{
        TextView nodename;
    }
}
