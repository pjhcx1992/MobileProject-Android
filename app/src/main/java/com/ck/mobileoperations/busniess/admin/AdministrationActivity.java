package com.ck.mobileoperations.busniess.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.home.ProjectItemAdapter;
import com.ck.mobileoperations.entity.Organization;
import com.ck.mobileoperations.entity.ProName;
import com.ck.mobileoperations.entity.Profile;
import com.ck.mobileoperations.utils.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Created by chenkai on 2018/3/15.
 */

public class AdministrationActivity extends Activity {

    private final  String  TAG="AdministrationActivity";
    private LinearLayout mLinearLayout;
    private String selectorgName;
    private List<String> orgname=new ArrayList<>();
     private TextView showusername;
    private Spinner orgName;

    private ListView mRecyclerView;
    private ProjectItemAdapter projectItemAdapter;
    private List<String> pronameList;
    private List<String> pronamecheck;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        showusername=(TextView)findViewById(R.id.show_username);
        orgName=(Spinner)findViewById(R.id.orgname);

        mRecyclerView = (ListView) findViewById(R.id.projects_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        ImageView out=(ImageView)findViewById(R.id.out_image);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pronameList=new ArrayList<>();
        pronamecheck=new ArrayList<>();
        inintent();


    }
    private void inintent() {
        orgname.removeAll(orgname);
        //公司
        final List<Profile>profileList= DataSupport.where("userId=? and role=?"
                ,Constant.user.getUserId(),"administrators").find(Profile.class);
        for(int i=0;i<profileList.size();i++){
           Profile profile=profileList.get(i);
            Organization organization=DataSupport.where("orgId=?",profile.getOrganizationId()).find(Organization.class).get(0);
            orgname.add(organization.getName());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner,orgname);
        adapter.setDropDownViewResource(R.layout.myspinner);
        orgName.setAdapter(adapter);
        orgName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pronameList.removeAll(profileList);
                pronamecheck.removeAll(pronamecheck);

                selectorgName=orgName.getSelectedItem().toString().trim();
                Organization organization=DataSupport.where("name=?",selectorgName).find(Organization.class).get(0);
                Profile profile=DataSupport.where("organizationId=? and userId=?",organization.getOrgId(),Constant.user.getUserId())
                        .find(Profile.class).get(0);
                if(profile.getDepartment().equals("design")){
                    List<ProName> proNameList=DataSupport.where("orgId=?",organization.getOrgId()).find(ProName.class);
                    if(proNameList.size()>0){
                        for(int i=0;i<proNameList.size();i++){
                            ProName proName=proNameList.get(i);
                            pronameList.add(proName.getProname());
                            pronamecheck.add(proName.getStatus());

                        }
                    }
                    projectItemAdapter=new ProjectItemAdapter(AdministrationActivity.this,pronameList,pronamecheck,organization.getOrgId());
                    mRecyclerView.setAdapter(projectItemAdapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        showusername.setText(Constant.user.getUserName());

        mLinearLayout= (LinearLayout) findViewById(R.id.linear);
       final List<String> strings=new ArrayList<>();
       strings.add("资源概况");
       strings.add("资源分布查询");
        strings.add("资源定位");
        strings.add("资源统计");
        strings.add("账号管理");

        //开始添加数据
        for(int x=0; x<5; x++){
            //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
            View view= LayoutInflater.from(this).inflate(R.layout.item_text,mLinearLayout,false);
            //实例化TextView控件
            final TextView tv= (TextView) view.findViewById(R.id.textView);
            //给TextView添加文字
            tv.setText(strings.get(x));
            //把行布局放到linear里

           tv.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(tv.getText().toString().equals("账号管理")){
                       Intent intent=new Intent(AdministrationActivity.this,AddMembersActivity.class);
                       intent.putExtra("selectorgName",selectorgName);
                       startActivity(intent);
                   }
               }
           });
            mLinearLayout.addView(view);
        }
    }


    private void toastMessage(String s){
        Toast.makeText(AdministrationActivity.this,s,Toast.LENGTH_SHORT).show();
    }


}
