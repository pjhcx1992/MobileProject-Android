package com.ck.mobileoperations.busniess.design;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.details.ProjectDetailsActivity;

import java.util.List;

/**
 * Created by CK on 2017/12/15.
 */

public class RouteItemAdapter extends BaseAdapter {

    private Context context;
    private List<String> dataList;
    private List<String> statusList;
    private String selectorganizationId;


    public RouteItemAdapter(Context context, List<String> dataList,List<String> statusList,String selectorganizationId){
        this.context=context;
        this.statusList=statusList;
        this.dataList=dataList;
        this.selectorganizationId=selectorganizationId;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.list_proname_shejiyuan_item,null);


        }
        TextView proname= (TextView) convertView.findViewById(R.id.projects_name);
        TextView prosend=(TextView) convertView.findViewById(R.id.projects_status);

        final String name=dataList.get(position);
        proname.setText(name);

       final String status=statusList.get(position);
        prosend.setText(status);

        prosend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DesignRouteActivity.class);
                intent.putExtra("proname",name);
                intent.putExtra("selectorganizationId",selectorganizationId);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
