package com.ck.mobileoperations.busniess.design.opticalcable;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.home.CheckRouteActivity;

import java.util.List;

/**
 * Created by CK on 2017/1/15.
 */

public class OpticalCableAdapter extends BaseAdapter {

    private Context context;
    private List<String> dataList;


    public OpticalCableAdapter(Context context, List<String> dataList){
        this.context=context;
        this.dataList=dataList;

    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.list_opticalcable_item,null);
        }
        TextView opticalname= (TextView) convertView.findViewById(R.id.opticalcable_name);

        ImageView opticalsend=(ImageView)convertView.findViewById(R.id.optical_send);
        final String name=dataList.get(position);

        opticalname.setText(name);


        opticalsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OpticCabModificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra("opticalcablename",name);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
