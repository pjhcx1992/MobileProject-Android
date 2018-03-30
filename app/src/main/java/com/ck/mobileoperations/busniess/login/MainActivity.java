package com.ck.mobileoperations.busniess.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.login.model.LoginModelImpl;
import com.ck.mobileoperations.busniess.login.vo.UserSend;
import com.ck.mobileoperations.entity.User;
import com.ck.mobileoperations.utils.RequestResultListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    EditText mEditTextPhoneNumber;
    EditText mEditTextCode;
    Button mButtonGetCode;
    TextView mRegister;
    TextView mCancel;
    EditText userNumber;
    EditText orgName;
    EditText userName;

    private LoginModelImpl loginModel;
    private RequestQueue mQueue;


    EventHandler eventHandler;
    private String strPhoneNumber;
    private List<User> usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        userNumber=(EditText)findViewById(R.id.user_number);
        mEditTextCode = (EditText) findViewById(R.id.verification_code);
        mButtonGetCode = (Button) findViewById(R.id.button_send_verification_code);
        orgName=(EditText)findViewById(R.id.orgname);
        userName=(EditText)findViewById(R.id.user_name);
        mRegister = (TextView) findViewById(R.id.send_register);
        mCancel=(TextView)findViewById(R.id.send_cancel);

        mRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mRegister.getPaint().setAntiAlias(true);

        mCancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mCancel.getPaint().setAntiAlias(true);


        mButtonGetCode.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mCancel.setOnClickListener(this);


        eventHandler = new EventHandler() {

            /**
             * 在操作之后被触发
             *
             * @param event  参数1
             * @param result 参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败
             * @param data   事件操作的结果
             */

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = myHandler.obtainMessage(0x00);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                myHandler.sendMessage(message);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
        loginModel=new LoginModelImpl(mQueue);
        mQueue = Volley.newRequestQueue(this);
        loginModel=new LoginModelImpl(mQueue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {
        strPhoneNumber = mEditTextPhoneNumber.getText().toString();
        if (view.getId() == R.id.send_register) {
            String userPassword=userNumber.getText().toString();
            String orgname=orgName.getText().toString();
            String UserName=userName.getText().toString();

            final String phone=strPhoneNumber;
            final String pass=userPassword;
            final String name=UserName;
            String organizationName=orgname;

            if(null!=UserName && userName.length()>0 && orgname.length()>0){
                if(null != userPassword && userPassword.length() >=6){
                    String strCode = mEditTextCode.getText().toString();
                    if (null != strCode && strCode.length() == 4) {
                        Log.d(TAG, mEditTextCode.getText().toString());
                        SMSSDK.submitVerificationCode("86", strPhoneNumber, mEditTextCode.getText().toString());

                    } else {
                        Toast.makeText(this, "验证码长度不正确", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "密码为6~16位字符!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "公司名或用户名不能为空!", Toast.LENGTH_SHORT).show();
            }





        } else if(view.getId()==R.id.send_cancel){

            finish();
        }else if (view.getId() == R.id.button_send_verification_code) {
            strPhoneNumber = mEditTextPhoneNumber.getText().toString();
            if (null == strPhoneNumber || "".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
                Toast.makeText(this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            SMSSDK.getVerificationCode("86", strPhoneNumber);
            mButtonGetCode.setClickable(false);
            //开启线程去更新button的text
            new Thread() {
                @Override
                public void run() {
                    int totalTime = 60;
                    for (int i = 0; i < totalTime; i++) {
                        Message message = myHandler.obtainMessage(0x01);
                        message.arg1 = totalTime - i;
                        myHandler.sendMessage(message);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myHandler.sendEmptyMessage(0x02);
                }
            }.start();
        }
    }
    @SuppressLint("HandlerLeak")
    Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e(TAG, "result : " + result + ", event: " + event + ", data : " + data);
                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                            Toast.makeText(MainActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "get verification code successful.");
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
                            Log.d(TAG, "submit code successful");

                            usersList=new ArrayList<>();
                            UserSend userSend=new UserSend();
//                            userSend.setJob(jobName.getText().toString());
                            userSend.setName(userName.getText().toString());
                            userSend.setPassword(userNumber.getText().toString());
                            userSend.setPhoneNun(mEditTextPhoneNumber.getText().toString());

                            String userPassword=userNumber.getText().toString();
                            String orgname=orgName.getText().toString();
                            String UserName=userName.getText().toString();

                            final String phone=strPhoneNumber;
                            final String pass=userPassword;
                            final String name=UserName;
                            String organizationName=orgname;
                            loginModel.volleyRegister(phone, pass, name, organizationName, new RequestResultListener() {
                                @Override
                                public void onSuccess(com.alibaba.fastjson.JSONObject s) {
                                    Log.i(TAG,"Register success message"+s);
                                    String statusCode=s.getString("statusCode");
                                    if(statusCode.equals("1")){
                                        User user=new User();
                                        user.setUserName(name);
                                        user.setPassword(pass);
                                        user.setPhoneNum(phone);
                                        user.save();

                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        strPhoneNumber = mEditTextPhoneNumber.getText().toString();
                                        intent.putExtra("strPhoneNumber",strPhoneNumber);
                                        startActivity(intent);
                                    }
                                    String desc=s.getString("desc");
                                    toastMessage(desc);
                                }

                                @Override
                                public void onError(String s) {
                                    Log.i(TAG,"Register error message"+s);
                                }
                            });



                        } else {
                            Log.d(TAG, data.toString());
                        }
                    } else { //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            Log.e(TAG, "status: " + status + ", detail: " + des);
                            if(status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(MainActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    mButtonGetCode.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    mButtonGetCode.setText("获取验证码");
                    mButtonGetCode.setClickable(true);
                    break;
            }
        }
    };

    public void toastMessage(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
