package com.ck.mobileoperations.busniess.maintain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.login.LoginActivity;
import com.ck.mobileoperations.entity.JumpOperation;
import com.ck.mobileoperations.entity.OpticalFiberJumper;

import org.litepal.crud.DataSupport;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenkai on 2018/2/9.
 */

public class OpticalFiberJumperActivity extends Activity implements View.OnClickListener{
    @Bind(R.id.out_image)
    ImageView outImage;
    @Bind(R.id.button_yi) Button btn_one;
    @Bind(R.id.button_er) Button btn_two;
    @Bind(R.id.button_san) Button btn_three;
    @Bind(R.id.button_si) Button btn_four;
    @Bind(R.id.button_wu) Button btn_five;
    @Bind(R.id.button_liu) Button btn_six;
    @Bind(R.id.button_qi) Button btn_seven;
    @Bind(R.id.button_ba) Button btn_eight;
    @Bind(R.id.button_jiu) Button btn_nine;
    @Bind(R.id.button_shi) Button btn_ten;
    @Bind(R.id.button_shiyi) Button btn_elevev;
    @Bind(R.id.button_shier) Button btn_twelve;
    @Bind(R.id.gjjxname) TextView opticalBoxName;//光交接箱的名字
    @Bind(R.id.flange)TextView flangeName;//法兰盘
    @Bind(R.id.location)TextView location;//在法兰盘的位置
    @Bind(R.id.rfid_id) EditText rfID;//rfId
    @Bind(R.id.save_rfid)Button saveRFID;
    @Bind(R.id.jump_list)ListView jumpList;
    private String gjjxName;
    private String rfid;
    private String ordername,locattionss,rfidss;
    private Button nowButton=null;

    private JumpOperationAdapter jumpOperationAdapter;
    private List<String> stringList=new ArrayList<>();
    @Bind(R.id.order_name)TextView orderName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opticalfiberjumper);
        ButterKnife.bind(this);

        outImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();

    }
    private void init(){
        ordername=getIntent().getStringExtra("order");
        gjjxName=getIntent().getStringExtra("gjjxname");
        locattionss=getIntent().getStringExtra("localtion");
        rfidss=getIntent().getStringExtra("rfid");
        opticalBoxName.setText(gjjxName);
        flangeName.setText(ordername);
        orderName.setText(ordername);
        if(locattionss!=null){
            location.setText(locattionss);
        }else {
            location.setText("");
        }
        if(rfidss!=null){
            rfID.setText(rfidss);
        }else {
            rfID.setText("");
        }
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_ten.setOnClickListener(this);
        btn_elevev.setOnClickListener(this);
        btn_twelve.setOnClickListener(this);
        saveRFID.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_yi:
               getMessage(btn_one);
                nowButton=btn_one;
                break;
            case R.id.button_er:
                getMessage(btn_two);
                nowButton=btn_two;
                break;
            case R.id.button_san:
                getMessage(btn_three);
                nowButton=btn_three;
                break;
            case R.id.button_si:
                getMessage(btn_four);
                nowButton=btn_four;
                break;
            case R.id.button_wu:
                getMessage(btn_five);
                nowButton=btn_five;
                break;
            case R.id.button_liu:
                getMessage(btn_six);
                nowButton=btn_six;
                break;
            case R.id.button_qi:
                getMessage(btn_seven);
                nowButton=btn_seven;
                break;
            case R.id.button_ba:
                getMessage(btn_eight);
                nowButton=btn_eight;
                break;
            case R.id.button_jiu:
                getMessage(btn_nine);
                nowButton=btn_nine;
                break;
            case R.id.button_shi:
                getMessage(btn_ten);
                nowButton=btn_ten;
                break;
            case R.id.button_shiyi:
                getMessage(btn_elevev);
                nowButton=btn_elevev;
                break;
            case R.id.button_shier:
                getMessage(btn_twelve);
                nowButton=btn_twelve;
                break;
            case R.id.save_rfid:
                saveRFIDs();
                break;



        }
    }

    private void getMessage(Button buttons){
        stringList.removeAll(stringList);
        opticalBoxName.setText(gjjxName);
        flangeName.setText(ordername);
        location.setText(buttons.getText().toString());

        List<OpticalFiberJumper> opticalFiberJumperList=DataSupport.findAll(OpticalFiberJumper.class);
        if(opticalFiberJumperList.size()>0){
            List<OpticalFiberJumper> opticalFiberJumpers= DataSupport.where("opticalBoxName=? and flangeName=? and location=?",gjjxName,
                    ordername,buttons.getText().toString()).find(OpticalFiberJumper.class);

            if(opticalFiberJumpers.size()>0){
                OpticalFiberJumper opticalFiberJumper=opticalFiberJumpers.get(0);
                rfID.setText(opticalFiberJumper.getRfId());
                rfid=opticalFiberJumper.getRfId();
            }else {
                rfID.setText("");
            }
        }

        /* private String originName,endName;
    private String originFlange,endFlange;
    private String originRfID,endRfID;
    private String originLocation,endLocation;
    private String operationName;*/
        if(rfid!=null){
            List<JumpOperation> jumpOperations=DataSupport.findAll(JumpOperation.class);
            if(jumpOperations.size()>0){
                List<JumpOperation> jumpOperationList=DataSupport
                        .where("originName=? and originFlange=? and originRfID=? and originLocation=? and operationName=?",
                                gjjxName,ordername,rfid,buttons.getText().toString(),"user3")
                        .find(JumpOperation.class);
                if(jumpOperationList.size()>0){
                    for(int i=0;i<jumpOperationList.size();i++){
                        JumpOperation jumpOperation=jumpOperationList.get(i);
                        stringList.add(jumpOperation.getOriginName()+"-"+jumpOperation.getOriginFlange()+"-"+jumpOperation.getOriginLocation()+
                        " 跳到 "+jumpOperation.getEndName()+"-"+jumpOperation.getEndFlange()+"-"+jumpOperation.getEndLocation());
                    }

                }
                List<JumpOperation> jumpOperationList1=DataSupport
                        .where("endName=? and endFlange=? and endRfID=? and endLocation=? and operationName=?",
                                gjjxName,ordername,rfid,buttons.getText().toString(),"user3")
                        .find(JumpOperation.class);
                if(jumpOperationList1.size()>0){
                    for(int a=0;a<jumpOperationList1.size();a++){
                        JumpOperation jumpOperation=jumpOperationList1.get(a);
                        stringList.add(jumpOperation.getOriginName()+"-"+jumpOperation.getOriginFlange()+"-"+jumpOperation.getOriginLocation()+
                                " 跳到"+jumpOperation.getEndName()+"-"+jumpOperation.getEndFlange()+"-"+jumpOperation.getEndLocation());
                    }
                }

                jumpOperationAdapter=new JumpOperationAdapter(getBaseContext(),stringList);
                jumpList.setAdapter(jumpOperationAdapter);
            }
        }


    }
    private void saveRFIDs(){
        if(nowButton!=null) {
            List<OpticalFiberJumper> opticalFiberJumperList=DataSupport.findAll(OpticalFiberJumper.class);
            if(opticalFiberJumperList.size()>0){

                final List<OpticalFiberJumper> opticalFiberJumper = DataSupport.where("opticalBoxName=? and flangeName=? and location=?", gjjxName,
                        ordername, nowButton.getText().toString()).find(OpticalFiberJumper.class);

                if (rfID.getText().toString().trim().length() == 36) {
                    if (opticalFiberJumper .size()>0) {
                        final OpticalFiberJumper jumper=opticalFiberJumper.get(0);
                        new AlertDialog.Builder(this)
                                .setTitle("修改RFID")
                                .setMessage("是否保存修改的RFID信息?")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        jumper.setRfId(rfID.getText().toString());
                                        jumper.save();
                                    }
                                })
                                .setPositiveButton("取消", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("保存RFID")
                                .setMessage("是否保存输入的RFID信息?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OpticalFiberJumper opticalFiberJumper1 = new OpticalFiberJumper();
                                        opticalFiberJumper1.setOpticalBoxName(gjjxName);
                                        opticalFiberJumper1.setFlangeName(ordername);
                                        opticalFiberJumper1.setLocation(nowButton.getText().toString());
                                        opticalFiberJumper1.setRfId(rfID.getText().toString());
                                        opticalFiberJumper1.save();

                                       /* ContentValues contentValues=new ContentValues();
                                        contentValues.put("opticalBoxName",gjjxName);
                                        contentValues.put("flangeName",ordername);
                                        contentValues.put("location",nowButton.getText().toString());
                                        contentValues.put("rfId",rfID.getText().toString());
                                        DataSupport.updateAll(OpticalFiberJumper.class,contentValues);*/
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                } else {
                    Toast.makeText(this, "输入的RFID少于36位，请重新输入!", Toast.LENGTH_SHORT).show();
                }
            }else {

                if (rfID.getText().toString().trim().length() == 36) {
                    new AlertDialog.Builder(this)
                            .setTitle("保存RFID")
                            .setMessage("是否保存输入的RFID信息?")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OpticalFiberJumper opticalFiberJumper1 = new OpticalFiberJumper();
                                    opticalFiberJumper1.setOpticalBoxName(gjjxName);
                                    opticalFiberJumper1.setFlangeName(ordername);
                                    opticalFiberJumper1.setLocation(nowButton.getText().toString());
                                    opticalFiberJumper1.setRfId(rfID.getText().toString());
                                    opticalFiberJumper1.save();
                                }
                            })
                            .setPositiveButton("取消", null)
                            .show();
                }else {
                    Toast.makeText(this, "输入的RFID少于36位，请重新输入!", Toast.LENGTH_SHORT).show();
                }
            }

        }else {
            Toast.makeText(this, "请先选择光芯!", Toast.LENGTH_SHORT).show();
        }
    }
}
