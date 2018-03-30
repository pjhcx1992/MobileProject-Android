package com.ck.mobileoperations.busniess.design.opticalcable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.design.SheJiYuanActivity;
import com.ck.mobileoperations.busniess.design.model.ProjectModelImpl;
import com.ck.mobileoperations.busniess.home.ProjectItemAdapter;
import com.ck.mobileoperations.entity.OpticalCable;
import com.ck.mobileoperations.utils.RequestResultListener;
import com.ck.mobileoperations.widget.OpticalCableDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenkai on 2018/1/15. OpticalCableAdapter
 */

public class OpticalCableActivity extends Activity {
    private static final String TAG="OpticalCableActivity";
    private ListView mRecyclerView;
    private OpticalCableAdapter opticalCableAdapter;
    private List<String> opticalcableList;
    private int countOptocalname=0;
    private ProjectModelImpl projectModel;
    private RequestQueue mQueue;

    @Bind(R.id.out_image)
    ImageView outImage;

    @Bind(R.id.create_optical_cable)
    Button ctreateOpticalCable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shejiyuan_opticalcable);
        ButterKnife.bind(this);
        mRecyclerView = (ListView) findViewById(R.id.shejiyuan_optical_cable_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        opticalcableList=new ArrayList<>();

        List<OpticalCable> opticalCableList= DataSupport.findAll(OpticalCable.class);
        if(opticalCableList!=null){
            for(int i=0;i<opticalCableList.size();i++){
                opticalcableList.add(opticalCableList.get(i).getOpticalcablename());
            }
        }

        ctreateOpticalCable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpticalCable();
            }
        });
        opticalCableAdapter=new OpticalCableAdapter(getApplicationContext(),opticalcableList);
        mRecyclerView.setAdapter(opticalCableAdapter);

        outImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(getApplicationContext(), SheJiYuanActivity.class);
                startActivity(intent);
            }
        });

        mQueue = Volley.newRequestQueue(OpticalCableActivity.this);
        projectModel=new ProjectModelImpl(mQueue);
    }
    OpticalCableDialog opticalCableDialog;

    private void showOpticalCable(){
        opticalCableDialog=new OpticalCableDialog(this,R.style.loading_dialog,monClickListener,onItemSelectedListener);
        opticalCableDialog.show();
    }
    private String coreamount;
    AdapterView.OnItemSelectedListener onItemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            coreamount=opticalCableDialog.opticalcableSize.getSelectedItem().toString().trim();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    View.OnClickListener monClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.send_optical:
                    opticalCableDialog.dismiss();
                    final String opticalcablename=opticalCableDialog.opticalcableName.getText().toString().trim();
                    final String opticalcablemodel=opticalCableDialog.opticalcableModel.getText().toString().trim();

                    //光缆型号
                    if(opticalcablemodel.length()>0 && opticalcablemodel!=" " ){

                        //光缆名
                        if(opticalcablename!=" "&&opticalcablename.length()>0){
                            List<OpticalCable> opticalCables=DataSupport.findAll(OpticalCable.class);
                            if(opticalCables.size()>0){
                                for(int i=0;i<opticalCables.size();i++){
                                    if(opticalCables.get(i).getOpticalcablename().equals(opticalcablename)){
                                        countOptocalname++;
                                    }
                                }
                                if(countOptocalname==0){
//                                opticalCable.setOpticalcablename(opticalcablename);//添加到数据库

                                    final OpticalCable opticalCable=new OpticalCable();
                                    opticalCable.setOpticalcablename(opticalcablename);
                                    opticalCable.setCoremodel(opticalcablemodel);//型号
                                    opticalCable.setCoreamount(coreamount);//芯数
                                    opticalCable.setAddtime(String.valueOf(System.currentTimeMillis()));
                                    opticalCable.save();
                                    opticalcableList.add(opticalcablename);//添加到list


                                    final OpticalCable opticalCableCreate=DataSupport.where("opticalcablename=?",opticalcablename)
                                            .find(OpticalCable.class).get(0);
                                    projectModel.volleyAddCable(opticalcablename, coreamount, opticalcablemodel, opticalCableCreate.getAddtime(),
                                            new RequestResultListener() {
                                                @Override
                                                public void onSuccess(JSONObject s) {
                                                    opticalCableCreate.setOpticalcableId(s.getString("_id"));
                                                    opticalCableCreate.save();
                                                }

                                                @Override
                                                public void onError(String s) {
                                                    Log.i(TAG,"createOpticalcable error="+s);
                                                }
                                            });

                                }else {
                                    toastMarkt("光缆名重复，请输入正确的光缆名！");
                                }
                                countOptocalname=0;
                            }else {

                                OpticalCable opticalCable=new OpticalCable();//添加到数据库
                                opticalCable.setOpticalcablename(opticalcablename);
                                opticalCable.setCoremodel(opticalcablemodel);//型号
                                opticalCable.setCoreamount(coreamount);//芯数
                                opticalCable.setAddtime(String.valueOf(System.currentTimeMillis()));
                                opticalCable.save();

                                opticalcableList.add(opticalcablename);//添加到list

                                final OpticalCable opticalCableCreate=DataSupport.where("opticalcablename=?",opticalcablename)
                                        .find(OpticalCable.class).get(0);
                                projectModel.volleyAddCable(opticalcablename, coreamount, opticalcablemodel, opticalCableCreate.getAddtime(),
                                        new RequestResultListener() {
                                            @Override
                                            public void onSuccess(JSONObject s) {
                                                opticalCableCreate.setOpticalcableId(s.getString("_id"));
                                                opticalCableCreate.save();
                                            }

                                            @Override
                                            public void onError(String s) {
                                                Log.i(TAG,"createOpticalcable error="+s);
                                            }
                                        });
                            }
                        }else {
                            toastMarkt("光缆名不能为空");
                        }
                    }else {
                        toastMarkt("厂家型号不能为空！");
                    }

                    break;

                case R.id.cancel_optical:
                    opticalCableDialog.dismiss();
                    break;
            }
        }
    };


    private void toastMarkt(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
