package com.ck.mobileoperations.busniess.design.opticalcable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.entity.OpticalCable;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenkai on 2018/1/15.
 */

public class OpticCabModificationActivity extends Activity {
    @Bind(R.id.opticalcable_name_activity)
    EditText opticalcanbleName;
    @Bind(R.id.optical_cable_model_activity)
    EditText opticalcanbleMode;
    @Bind(R.id.optical_cable_size_activity)
    Spinner opticalcanbleSize;
    @Bind(R.id.send_optical)
    Button sendOptical;
    @Bind(R.id.cancel_optical)
    Button cancelOptical;
    private String size;
    private int countOptocalname=0;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opticalcable_modification);
        ButterKnife.bind(this);
        intit();
        modificationOpticalCable();//保存修改信息

    }
    private void intit(){
        //初始化数据
        String opticalcablename=getIntent().getStringExtra("opticalcablename");
        OpticalCable opticalCable= DataSupport.where("opticalcablename=?",opticalcablename).find(OpticalCable.class).get(0);
        opticalcanbleName.setText(opticalCable.getOpticalcablename());
        opticalcanbleMode.setText(opticalCable.getCoremodel());
        setSpinnerItemSelectedByValue(opticalcanbleSize,opticalCable.getCoreamount());

        opticalcanbleSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size=opticalcanbleSize.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //根据值设置spinner选中值
    public  void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
//                spinner.setSelection(i,true);// 默认选中项
                spinner.setSelection(i);// 默认选中项
                break;
            }
        }
    }

    private void modificationOpticalCable(){
        sendOptical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).setTitle("修改信息")
                .setMessage("是否保存修改信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name=opticalcanbleName.getText().toString().trim();
                        String model=opticalcanbleMode.getText().toString().trim();
                        if(model.length()>0&&model!=" "){
                            if(name.length()>0&&name!=" "){
                                List<OpticalCable> opticalCables=DataSupport.findAll(OpticalCable.class);
                                if(opticalCables.size()>0){
                                    for(int i=0;i<opticalCables.size();i++){
                                        if(opticalCables.get(i).getOpticalcablename().equals(name)){
                                            countOptocalname++;
                                        }
                                    }
                                    if(countOptocalname==1 || countOptocalname==0){  //命名重复 1本身不改变命名 0没有这个命名
                                        String opticalcablename=getIntent().getStringExtra("opticalcablename");
                                        OpticalCable opticalCable= DataSupport.where("opticalcablename=?",opticalcablename).find(OpticalCable.class).get(0);
                                        opticalCable.setOpticalcablename(name);
                                        opticalCable.setCoremodel(model);
                                        opticalCable.setCoreamount(size);
                                        opticalCable.save();
                                    }else {
                                        toastMarkt("光缆名重复，请输入正确的光缆名！");
                                    }
                                    countOptocalname=0;
                                }else if (opticalCables.size()==1){
                                    String opticalcablename=getIntent().getStringExtra("opticalcablename");
                                    OpticalCable opticalCable= DataSupport.where("opticalcablename=?",opticalcablename).find(OpticalCable.class).get(0);
                                    opticalCable.setOpticalcablename(name);
                                    opticalCable.setCoremodel(model);
                                    opticalCable.setCoreamount(size);
                                    opticalCable.save();
                                }
                            }else {
                                toastMarkt("光缆名不能为空");
                            }
                        }else {
                            toastMarkt("厂家型号不能为空！");
                        }


                    }
                }).setNegativeButton("取消",null).show();
            }
        });

        cancelOptical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(getApplicationContext(),OpticalCableActivity.class);
                startActivity(intent);

            }
        });
    }
    private void toastMarkt(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
