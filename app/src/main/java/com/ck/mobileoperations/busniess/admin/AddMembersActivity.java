package com.ck.mobileoperations.busniess.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.login.LoginActivity;
import com.ck.mobileoperations.busniess.login.model.LoginModelImpl;
import com.ck.mobileoperations.entity.Organization;
import com.ck.mobileoperations.entity.Profile;
import com.ck.mobileoperations.utils.Constant;
import com.ck.mobileoperations.utils.RequestResultListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenkai on 2018/3/15.
 */

public class AddMembersActivity extends Activity implements View.OnClickListener {
    private Spinner department,role;
    private TextView orgName;
    private EditText phone,username;
    private String selectorgName;
    private List<String> departmentlist=new ArrayList<>();
    private List<String> rolelist=new ArrayList<>();
    private LoginModelImpl loginModel;
    private RequestQueue mQueue;
    private String addrole,adddepartment;
    private String phonenumber,userNames;
    private Organization organization;
    private Button cancel,send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ck.mobileoperations.R.layout.activity_addmember);
        orgName=(TextView)findViewById(R.id.orgname);
        department=(Spinner)findViewById(R.id.department);
        role=(Spinner)findViewById(R.id.role);
        cancel=(Button)findViewById(R.id.cancel);
        send=(Button)findViewById(R.id.send);
        username=(EditText)findViewById(R.id.user_name);
        phone=(EditText)findViewById(R.id.phone_number);


        init();
        cancel.setOnClickListener(this);
        send.setOnClickListener(this);



    }
    private void init(){
        phonenumber=phone.getText().toString().trim();
        userNames=username.getText().toString().trim();
        mQueue = Volley.newRequestQueue(AddMembersActivity.this);
        loginModel=new LoginModelImpl(mQueue);
        departmentlist.removeAll(departmentlist);
        rolelist.removeAll(rolelist);



        selectorgName=getIntent().getStringExtra("selectorgName");
        orgName.setText(selectorgName);

        organization= DataSupport.where("name=?",selectorgName).find(Organization.class).get(0);

        Profile profile=DataSupport.where("organizationId=? and userId=?",organization.getOrgId(), Constant.user.getUserId())
                .find(Profile.class).get(0);
        if(profile.isSecretary()){

            rolelist.add("管理员");//administrators
            rolelist.add("成员");//members

            //角色
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner,rolelist);
            adapter.setDropDownViewResource(R.layout.myspinner);
            role.setAdapter(adapter);
            role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(role.getSelectedItem().toString().trim().equals("管理员")){
                        addrole="administrators";
                    }else if(role.getSelectedItem().toString().trim().equals("成员")){
                        addrole="members";
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //部门
            departmentlist.add("设计院");
            departmentlist.add("施工");
            departmentlist.add("维护");

            ArrayAdapter<String> departmentadapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner,departmentlist);
            departmentadapter.setDropDownViewResource(R.layout.myspinner);
            department.setAdapter(departmentadapter);
            department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(department.getSelectedItem().toString().trim().equals("设计院")){
                        adddepartment="design";
                    }else if(department.getSelectedItem().toString().trim().equals("施工")){
                        adddepartment="construction";
                    }else if(department.getSelectedItem().toString().trim().equals("维护")){
                        adddepartment="maintain";
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        }else {
           if(profile.getRole().equals("administrators")) {

               rolelist.add("成员");//members
               //角色
               ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner,rolelist);
               adapter.setDropDownViewResource(R.layout.myspinner);
               role.setAdapter(adapter);
               role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if(role.getSelectedItem().toString().trim().equals("管理员")){
                           addrole="administrators";
                       }else if(role.getSelectedItem().toString().trim().equals("成员")){
                           addrole="members";
                       }
                   }
                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {
                   }
               });


               if(profile.getDepartment().equals("design")){
                   departmentlist.add("设计院");
               }else if(profile.getDepartment().equals("construction")){
                   departmentlist.add("施工");
               }else if(profile.getDepartment().equals("maintain")){
                   departmentlist.add("维护");
               }
               ArrayAdapter<String> departmentadapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner,departmentlist);
               departmentadapter.setDropDownViewResource(R.layout.myspinner);
               department.setAdapter(departmentadapter);
               department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if(department.getSelectedItem().toString().trim().equals("设计院")){
                           adddepartment="design";
                       }else if(department.getSelectedItem().toString().trim().equals("施工")){
                           adddepartment="construction";
                       }else if(department.getSelectedItem().toString().trim().equals("维护")){
                           adddepartment="maintain";
                       }
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               });
           }
        }
    }

    private void volleyAddMembers(String phonenum, String username,
                                  String organizationId, String department, String role){

        if(phonenum.length()<11 || username.length()<0 ){
            Toast.makeText(getApplicationContext(),"账号或用户名不正确,请重新输入!",Toast.LENGTH_SHORT).show();
        }else {
            loginModel.addMembers(phonenum, username, organizationId, department, role, new RequestResultListener() {
                @Override
                public void onSuccess(JSONObject s) {
                    Log.i("AddMembersActivity","addmember erros="+s);
                    toastMessage(s.getString("desc"));
                    finish();
                }

                @Override
                public void onError(String s) {
                    Log.i("AddMembersActivity","addmember erros="+s);
                }
            });
        }

    }

    private void toastMessage(String s){
        Toast.makeText(AddMembersActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.send:
                volleyAddMembers(phone.getText().toString().trim(),username.getText().toString().trim(),
                        organization.getOrgId(),adddepartment,addrole);
                break;
        }
    }
}
