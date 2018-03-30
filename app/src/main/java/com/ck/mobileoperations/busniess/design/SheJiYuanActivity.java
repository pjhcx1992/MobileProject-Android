package com.ck.mobileoperations.busniess.design;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.design.model.ProjectModelImpl;
import com.ck.mobileoperations.busniess.design.model.Projtctmodel;
import com.ck.mobileoperations.busniess.design.opticalcable.OpticalCableActivity;
import com.ck.mobileoperations.busniess.login.LoginActivity;
import com.ck.mobileoperations.busniess.login.model.LoginModelImpl;
import com.ck.mobileoperations.entity.Organization;
import com.ck.mobileoperations.entity.ProName;
import com.ck.mobileoperations.entity.Profile;
import com.ck.mobileoperations.utils.Constant;
import com.ck.mobileoperations.utils.RequestResultListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.os.Handler;

/**
 * Created by chenkai on 2018/1/2.
 */

public class SheJiYuanActivity extends Activity {
    private static final String TAG="SheJiYuanActivity";
    private static final int LOCATION_CODE = 1;
    private RouteItemAdapter routeItemAdapter;
    private List<String> routeList;
    private List<String> statusList;
    private List<String> orgnameList;
    private ListView mRecyclerView;
    private int countProname=0;
    private String status="创建中";
    private Spinner orgName;
    private TextView userName;
    @Bind(R.id.create_route)
    Button createRout;
    @Bind(R.id.out_image)
    ImageView out;
    @Bind(R.id.create_opticalcable)
    Button opticalcable;
    private ProjectModelImpl projectModel;
    private RequestQueue mQueue;
    private Organization selectorganization;
    private  ArrayAdapter<String> adapter;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shejiyuan);
        ButterKnife.bind(this);
        orgName=(Spinner)findViewById(R.id.orgname);
        userName=(TextView)findViewById(R.id.user_names);
        mRecyclerView = (ListView) findViewById(R.id.shejiyuan_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        userName.setText(Constant.user.getUserName());
        routeList=new ArrayList<>();
        statusList=new ArrayList<>();
        orgnameList=new ArrayList<>();

        routeList.removeAll(routeList);
        statusList.removeAll(statusList);
        orgnameList.removeAll(orgnameList);

        //初始化的时候根据所在的org更新项目列表
        List<Profile> profileList=DataSupport.where("userId=? and role=?",Constant.user.getUserId(),"members").find(Profile.class);
        System.out.println("profileList size="+profileList.size());
        if(profileList.size()>0){
            for(int i=0;i<profileList.size();i++){
                Profile profile=profileList.get(i);
                Organization organization=DataSupport.where("orgId=?",profile.getOrganizationId()).find(Organization.class).get(0);
                orgnameList.add(organization.getName());
            }
        }

        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner,orgnameList);
        adapter.setDropDownViewResource(R.layout.myspinner);
        adapter.notifyDataSetChanged();
        orgName.setAdapter(adapter);

        orgName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                routeList.removeAll(routeList);
                statusList.removeAll(statusList);

                selectorganization=DataSupport.where("name=?",orgName.getSelectedItem().toString().trim()).
                        find(Organization.class).get(0);


                List<ProName> proNameList= DataSupport.where("orgId=?",selectorganization.getOrgId()).find(ProName.class);
                if(proNameList.size()>0){
                    for(int i=0;i<proNameList.size();i++){
                        routeList.add(proNameList.get(i).getProname());
                        if(proNameList.get(i).getStatus().equals("0")){
                            statusList.add("创建中");
                        }else if(proNameList.get(i).getStatus().equals("1")){
                            statusList.add("审核中");
                        }else if(proNameList.get(i).getStatus().equals("2")){
                            statusList.add("已驳回");
                        }else if(proNameList.get(i).getStatus().equals("3")){
                            statusList.add("已通过");
                        }
                    }
                }

                routeItemAdapter=new RouteItemAdapter(SheJiYuanActivity.this,routeList,statusList,selectorganization.getOrgId());
                routeItemAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(routeItemAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        createRout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateRoute();
            }
        });





        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        opticalcable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),OpticalCableActivity.class);
                startActivity(intent);
            }
        });
        mQueue = Volley.newRequestQueue(SheJiYuanActivity.this);
        projectModel=new ProjectModelImpl(mQueue);
    }

    CreateRouteDialog createRouteDialog;
    private void showCreateRoute(){
        createRouteDialog=new CreateRouteDialog(this,R.style.loading_dialog,monClickListener);
        createRouteDialog.show();
    }
    private String proName;
    View.OnClickListener monClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.queren_create:
                    createRouteDialog.dismiss();

                    proName=createRouteDialog.proName.getText().toString().trim();

                    if(proName!=" " && proName.length()>0){

                        List<ProName> proNameList= DataSupport.where("orgId=?",selectorganization.getOrgId()).find(ProName.class);
                        if(proNameList.size()>0){
                            for(int i=0;i<proNameList.size();i++){
                                if(proNameList.get(i).getProname().equals(proName)){
                                    countProname++;
                                }
                            }
                            if(countProname==0){
                                final ProName proNames=new ProName();
                                proNames.setProname(proName);
                                proNames.setStatus("0");
                                proNames.setCheckstatus("0");
                                proNames.setAddtime(String.valueOf(System.currentTimeMillis()));
                                proNames.setOrgId(selectorganization.getOrgId());
                                proNames.save();
                                proNames.setCreateMember(Constant.user.getUserId());
                                routeList.add(proName);
                                statusList.add(status);

                                final ProName proCreate=DataSupport.where("proname=?",proName).find(ProName.class).get(0);
                                //发送数据到服务器
                                projectModel.volleyProjectCreate(proName, "0", "0", Constant.user.getUserId()
                                        , proCreate.getAddtime(), proCreate.getOrgId(), new RequestResultListener() {
                                    @Override
                                    public void onSuccess(JSONObject s) {
                                        String proId=s.getString("_id");
                                        Log.i("SheJiYUanActivity","project id="+proId);
                                        proCreate.setProjectId(proId);
                                        proCreate.save();
                                    }
                                    @Override
                                    public void onError(String s) {
                                        Log.i("SheJiYUanActivity","projtct_error message"+s);
                                    }
                                });
                                //跳转到设计页面
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (ContextCompat.checkSelfPermission(SheJiYuanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                                != PackageManager.PERMISSION_GRANTED) {// 没有权限。
                                            if (ActivityCompat.shouldShowRequestPermissionRationale(SheJiYuanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                                                ActivityCompat.requestPermissions(SheJiYuanActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                                            } else {
                                                // 申请授权。
                                                ActivityCompat.requestPermissions(SheJiYuanActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);

                                            }
                                        } else {
                                            Intent intent=new Intent(SheJiYuanActivity.this,DesignRouteActivity.class);
                                            intent.putExtra("proname",proName);
                                            intent.putExtra("selectorganizationId",selectorganization.getOrgId());
                                            startActivity(intent);
                                        }
                                    }

                                },500);


                            }else {
                                Toast.makeText(getBaseContext(),"项目名重复，请输入正确的项目名！",Toast.LENGTH_SHORT).show();
                            }
                            countProname=0;

                        }else {
                            final ProName proNames=new ProName();
                            proNames.setProname(proName);
                            proNames.setStatus("0");
                            proNames.setCheckstatus("0");
                            proNames.setAddtime(String.valueOf(System.currentTimeMillis()));
                            proNames.setOrgId(selectorganization.getOrgId());
                            proNames.setCreateMember(Constant.user.getUserId());
                            proNames.save();
                            routeList.add(proName);
                            statusList.add(status);

                            final ProName proCreate=DataSupport.where("proname=?",proName).find(ProName.class).get(0);
                            //发送数据到服务器
                            projectModel.volleyProjectCreate(proName, "0", "0", Constant.user.getUserId()
                                    , proCreate.getAddtime(), proCreate.getOrgId(), new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            Log.i(TAG,"project create success="+s);
                                            String proId=s.getString("_id");
                                            proCreate.setProjectId(proId);
                                            proCreate.save();


                                        }
                                        @Override
                                        public void onError(String s) {
                                            Log.i(TAG,"project create error="+s);
                                        }
                                    });

                            //跳转到设计页面
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ContextCompat.checkSelfPermission(SheJiYuanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                            != PackageManager.PERMISSION_GRANTED) {// 没有权限。
                                        if (ActivityCompat.shouldShowRequestPermissionRationale(SheJiYuanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                                            ActivityCompat.requestPermissions(SheJiYuanActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                                        } else {
                                            // 申请授权。
                                            ActivityCompat.requestPermissions(SheJiYuanActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);

                                        }
                                    } else {
                                        Intent intent=new Intent(SheJiYuanActivity.this,DesignRouteActivity.class);
                                        intent.putExtra("proname",proName);
                                        intent.putExtra("selectorganizationId",selectorganization.getOrgId());
                                        startActivity(intent);

                                    }
                                }

                            },500);
                        }


                    }else {
                        Toast.makeText(SheJiYuanActivity.this,"项目名不能为空!",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.cancel_create:
                    createRouteDialog.dismiss();
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        orgnameList.removeAll(orgnameList);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        orgnameList.removeAll(orgnameList);
        super.onStop();
    }
}
