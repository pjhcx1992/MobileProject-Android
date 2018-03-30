package com.ck.mobileoperations.busniess.maintain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ck.mobileoperations.R;

import java.util.List;

/**
 * Created by chenkai on 2018/2/12.
 */

public class JumpOperationAdapter extends BaseAdapter {
    private Context context;
    private List<String> dataList;
    public JumpOperationAdapter(Context context, List<String> dataList){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.list_optical_jump_item,null);
        }
        TextView operation=(TextView)convertView.findViewById(R.id.jump_operations);
        TextView username=(TextView)convertView.findViewById(R.id.operation_username);
        String jumpoperation=dataList.get(position);
        operation.setText(jumpoperation);
        return convertView;
    }
}
