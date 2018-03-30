package com.ck.mobileoperations.busniess.login;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.admin.AdministrationActivity;
import com.ck.mobileoperations.busniess.design.DesignRouteActivity;
import com.ck.mobileoperations.busniess.design.SheJiYuanActivity;
import com.ck.mobileoperations.busniess.home.HomePageActivity;
import com.ck.mobileoperations.busniess.login.model.LoginModelImpl;
import com.ck.mobileoperations.busniess.maintain.OpticalSplitterBox;
import com.ck.mobileoperations.entity.Profile;
import com.ck.mobileoperations.entity.User;
import com.ck.mobileoperations.utils.Constant;
import com.ck.mobileoperations.utils.FastJsonRequest;
import com.ck.mobileoperations.utils.RequestResultListener;
import com.alibaba.fastjson.JSONObject;
import com.ck.mobileoperations.utils.UpdateDataUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.litepal.tablemanager.Connector.getWritableDatabase;


/**
 * Created by CK on 2017/12/13.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private static String TAG="LoginActivity";
    private static final int LOCATION_CODE = 1;
    private TextView register;
    private TextView cancelRegister;
    private EditText userNumber;
    private EditText passWord;

    private LoginModelImpl loginModel;
    private RequestQueue mQueue;
    ProgressDialog progressDialog;

    @Bind(R.id.login)
    Button login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind( this ) ;



        /*db.delete("ProName",null,null)*/;

        register=(TextView)findViewById(R.id.register);

        cancelRegister=(TextView)findViewById(R.id.cancel_register);
        userNumber=(EditText) findViewById(R.id.user_number);
        passWord=(EditText)findViewById(R.id.user_password);
        register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        register.getPaint().setAntiAlias(true);
        cancelRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        cancelRegister.getPaint().setAntiAlias(true);

        List<User> user=DataSupport.findAll(User.class);
        if(user.size()>0){
            User users=user.get(0);
            userNumber.setText(users.getPhoneNum());
            passWord.setText(users.getPassword());
        }



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Constant.user=null;
        init();


    }

    private void init(){
        mQueue = Volley.newRequestQueue(LoginActivity.this);
        loginModel=new LoginModelImpl(mQueue);
        String strPhoneNumber=getIntent().getStringExtra("strPhoneNumber");
        if(strPhoneNumber!=null){
            userNumber.setText(strPhoneNumber);
        }
        login.setOnClickListener(this);

    }


   /* void loginClick(){
        if(userNumber.getText().toString().equals("user2")){
            Intent intent=new Intent(LoginActivity.this,HomePageActivity.class);
            startActivity(intent);
        }else if(userNumber.getText().toString().equals("user3")){
            Intent intent=new Intent(LoginActivity.this,OpticalSplitterBox.class);
            startActivity(intent);
        }else{
            String phone=userNumber.getText().toString().trim();
            String pass=passWord.getText().toString().trim();
            loginModel.volleyLogin(phone, pass, new RequestResultListener(){
                @Override
                public void onSuccess(JSONObject s) {
                    Log.i(TAG,"response message"+s.toJSONString());
                    String statusCode=s.getString("statusCode");
                    String desc=s.getString("desc");
                    Log.i(TAG,"statusCode="+statusCode);
                    if(statusCode.equals("1")){
                        Intent intent=new Intent(LoginActivity.this,SheJiYuanActivity.class);
                        startActivity(intent);
                        finish();
                        toastMessage(desc);

                    }else {
                        toastMessage(desc);
                    }

                }

                @Override
                public void onError(String s) {
                    Log.i(TAG,"login_error message"+s);
                }
            });
            *//*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {// 没有权限。
                        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                        } else {
                            // 申请授权。
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);

                        }
                    } else {
                        String phone=userNumber.getText().toString().trim();
                        String pass=passWord.getText().toString().trim();


                        String temp = null;
                        try {
                            temp = "phoneNum=" + URLEncoder.encode(phone,"utf-8") + "&passWord=" +  URLEncoder.encode(pass,"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }

            },500);*//*

        }

    }*/

    @Override
    public void onClick(View v) {
        Constant.user=null;
        if(v.getId()==R.id.login){
            final String phone=userNumber.getText().toString().trim();
            final String pass=passWord.getText().toString().trim();
            if(phone.length()<11){
                toastMessage("账号不能为空");
            }else {
                loginModel.volleyLogin(phone, pass, new RequestResultListener(){
                    @Override
                    public void onSuccess(JSONObject s) {
                        Log.i(TAG,"login_response message"+s.toJSONString());
                        String statusCode=s.getString("statusCode");
                        String desc=s.getString("desc");
                        if(statusCode.equals("1")){
                            //登录成功
                           doHandleSuccess(s,phone,pass);

                            getHomeActivity();

                            toastMessage(desc);
                        }else {
                            toastMessage(desc);
                        }
                    }
                    @Override
                    public void onError(String s) {
                        Log.i(TAG,"login_error message"+s);
                        toastMessage("网络请求错误,登录失败!");
                    }
                });

            }

        }
    }

    private void doHandleSuccess(JSONObject json,String phone,String pass){
        final String userId=json.getString("userId");
        String username=json.getString("userName");

        User temp=new User();
        temp.setUserId(userId);
        temp.setPhoneNum(phone);
        temp.setPassword(pass);
        Constant.user=temp;


        try {
           /* if(json.containsKey("lastLoginTime")){//判断是不是第一次登录
                Date lastLoginTime=json.getDate("lastLoginTime");

            }else {

            }*/
           /* final User user=new User();
            user.setPhoneNum(phone);
            user.setUserId(userId);
            user.setPassword(pass);
            user.setUserName(username);
            user.save();
            Constant.user=user;
*/

           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    update(userId);
                }
            });*/
            update(userId);

        }catch (JSONException e){
            e.printStackTrace();
            toastMessage("数据解析出错!");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void toastMessage(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }


    public void update(final String userId){
        loginModel.getBaseInfo(userId, new RequestResultListener() {
            @Override
            public void onSuccess(JSONObject s) {
                Log.i(TAG,"update data success="+s);
                UpdateDataUtil.deletePointSet();
                JSONObject user=s.getJSONObject("user");
                UpdateDataUtil.updateUser(user);
                JSONArray allOrganization=s.getJSONArray("allOrganization");
                UpdateDataUtil.updateAllOrganization(allOrganization);

                JSONArray allprojrct=s.getJSONArray("allProject");
                UpdateDataUtil.updateAllProject(allprojrct);

                JSONArray allProfile=s.getJSONArray("allProfile");
                UpdateDataUtil.updateAllProfile(allProfile);

                JSONArray allWell=s.getJSONArray("allWell");
                UpdateDataUtil.updateAllWell(allWell);

                JSONArray allPole=s.getJSONArray("allPole");
                UpdateDataUtil.updateAllPole(allPole);

                JSONArray allLightBox=s.getJSONArray("allLightBox");
                UpdateDataUtil.updateAllLightBox(allLightBox);

                JSONArray allUpPoint=s.getJSONArray("allUpPoint");
                UpdateDataUtil.updateAllUpPoint(allUpPoint);

                JSONArray allWallHang=s.getJSONArray("allWallHang");
                UpdateDataUtil.updateAllWallHang(allWallHang);


                JSONArray allJointBox=s.getJSONArray("allJointBox");
                UpdateDataUtil.updateAllJointBox(allJointBox);

                JSONArray allConduit=s.getJSONArray("allConduit");
                UpdateDataUtil.updateAllConduit(allConduit);

                JSONArray allCableSegment=s.getJSONArray("allCableSegment");
                UpdateDataUtil.updateAllCableSegment(allCableSegment);


            }

            @Override
            public void onError(String s) {
                Log.i(TAG,"update data error="+s);
            }
        });
    }
    private void getHomeActivity() {

        final ProgressDialog proDialog = android.app.ProgressDialog.show(LoginActivity.this, "登录成功", "跟新数据中...请稍等!");
        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            public void run(){
                proDialog.dismiss();

                int x=0;
                List<Profile> profileList=DataSupport.where("userId=?",Constant.user.getUserId()).find(Profile.class);
                for(int i=0;i<profileList.size();i++){
                    Profile profile=profileList.get(i);
                    if(profile.isSecretary()){
                      x++;
                    }else {
                        if(profile.getRole().equals("administrators")){
                            x++;
                        }
                    }
                }
                Log.i(TAG,"administrators="+x);
                if(x>0){
                    Intent intent=new Intent(LoginActivity.this,AdministrationActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(LoginActivity.this,SheJiYuanActivity.class);
                    startActivity(intent);
                }
                x=0;

                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        };

        timer.schedule(task, 3500);

    }
    private void getHome() {
        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            public void run(){
                init();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        };
        timer.schedule(task, 1500);
    }
}
