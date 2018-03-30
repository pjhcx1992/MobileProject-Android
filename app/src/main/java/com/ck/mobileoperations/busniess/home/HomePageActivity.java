package com.ck.mobileoperations.busniess.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.widget.ImageView;
import android.widget.ListView;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.login.LoginActivity;
import com.ck.mobileoperations.entity.ProName;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CK on 2017/12/7.
 * TODO 审核人员
 */

public class HomePageActivity extends Activity {
    private ListView mRecyclerView;
    private ProjectItemAdapter projectItemAdapter;
    private List<String> pronameList;
    private List<String> pronamecheck;
    @Bind(R.id.out_image)
    ImageView outImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);
        mRecyclerView = (ListView) findViewById(R.id.projects_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        pronameList=new ArrayList<>();
        pronamecheck=new ArrayList<>();
        List<ProName> proName= DataSupport.findAll(ProName.class);
        if(proName!=null){
            for(int i=0;i<proName.size();i++){
                pronameList.add(proName.get(i).getProname());
                pronamecheck.add(proName.get(i).getCheckstatus());
            }
        }
/*
        projectItemAdapter=new ProjectItemAdapter(this,pronameList,pronamecheck);
        mRecyclerView.setAdapter(projectItemAdapter);*/
    }
    @OnClick(R.id.out_image)
    void outback(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
