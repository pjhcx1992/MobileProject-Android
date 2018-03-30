package com.ck.mobileoperations.busniess.home;

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
import com.ck.mobileoperations.busniess.login.LoginActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by CK on 2017/12/15.
 */

public class ProjectItemAdapter extends BaseAdapter {

    private Context context;
    private List<String> dataList;
    private List<String> checkstatus;
    private String orgId;

    public  ProjectItemAdapter(Context context,List<String> dataList,List<String> checkstatus,String orgId){
        this.context=context;
        this.dataList=dataList;
        this.checkstatus=checkstatus;
        this.orgId=orgId;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.list_project_item,null);
        }
        TextView proname= (TextView) convertView.findViewById(R.id.projects_name);
        TextView check_pro=(TextView)convertView.findViewById(R.id.check_pro);
        ImageView prosend=(ImageView)convertView.findViewById(R.id.projects_send);
        final String name=dataList.get(position);
        String check=checkstatus.get(position);

        proname.setText(name);
        if(check.equals("0")){
            check_pro.setText("创建中");
        }else if(check.equals("1")){
            check_pro.setText("待审核");
        }else if(check.equals("2")){
            check_pro.setText("已驳回");
        }else if(check.equals("3")){
            check_pro.setText("已通过");
        }


        prosend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CheckRouteActivity.class);
                intent.putExtra("proname",name);
                intent.putExtra("orgId",orgId);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
