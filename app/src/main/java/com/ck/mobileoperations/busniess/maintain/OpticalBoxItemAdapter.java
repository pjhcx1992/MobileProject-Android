package com.ck.mobileoperations.busniess.maintain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ck.mobileoperations.R;
import java.util.List;

/**
 * Created by CK on 2017/2/9.
 */

public class OpticalBoxItemAdapter extends BaseAdapter {

    private Context context;
    private List<String> dataList;
    private ImageView boxImage;
    private String gjjxname;//光交接箱名

    public OpticalBoxItemAdapter(Context context, List<String> dataList,String gjjxname){
        this.context=context;
        this.dataList=dataList;
        this.gjjxname=gjjxname;

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
            convertView= LayoutInflater.from(context).inflate(R.layout.list_optical_box_item,null);
        }
        TextView textView=(TextView) convertView.findViewById(R.id.order);

        boxImage=(ImageView)convertView.findViewById(R.id.list_boxs);
        final String name=dataList.get(position);
        textView.setText(name);

        boxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,OpticalFiberJumperActivity.class);
                intent.putExtra("order",name);
                intent.putExtra("gjjxname",gjjxname);
                context.startActivity(intent);
            }
        });


        return convertView;


    }
}
