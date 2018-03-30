package com.ck.mobileoperations.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.entity.OpticalCable;
import com.ck.mobileoperations.entity.PointSet;
import com.ck.mobileoperations.utils.RoundNodeAdapter;
import com.ck.mobileoperations.utils.SearchAdapter;
import com.ck.mobileoperations.utils.cable.CableCache;
import com.ck.mobileoperations.utils.node.NodeCache;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by chenkai on 2018/1/31.
 */

public class SelectCableDialog extends Dialog implements View.OnClickListener{
    public AutoCompleteTextView search;
    private List<String> str=new ArrayList<>();
    Activity context;

    private Button createCable,sendCable,allCable,cancelCable;
    private Button childholeOne,childholeTwo,childholeThree,childholeFour,childholeFive,childholeSix;
    private View.OnClickListener onClickListener;
    private Spinner cableOrigin,childhoel;
    private int hole;


    private List<NodeCache> cacheList;//缓存的上一节点的信息
    private List<CableCache> cableCacheList;
    private List<String> childholes=new ArrayList<>();
    private List<String> opticalcableList;
    private int countOptocalname=0;
    private ArrayAdapter<String> adapterhole;
    public String childholename=null;
    public int cableoriginname=0;
    public int holename=0;

    public SelectCableDialog(Activity context) {
        super(context);
        this.context=context;
    }

    public SelectCableDialog(Activity context, int theme, View.OnClickListener onClickListener, List<NodeCache> cacheList,
                             List<CableCache> cableCacheList) {
        super(context, theme);
        this.context=context;
        this.onClickListener=onClickListener;

        this.cacheList=cacheList;
        this.cableCacheList=cableCacheList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_selectcable_dialog);
        search = (AutoCompleteTextView) findViewById(R.id.cable_name);
        createCable=(Button)findViewById(R.id.create_cable);
        allCable=(Button)findViewById(R.id.all_cable);
        sendCable=(Button)findViewById(R.id.send_cable);
        cancelCable=(Button)findViewById(R.id.cancel_cable);

        cableOrigin=(Spinner)findViewById(R.id.cable_origin);
        childhoel=(Spinner)findViewById(R.id.child_hole);
        childholeOne=(Button)findViewById(R.id.childhole_one);
        childholeTwo=(Button)findViewById(R.id.childhole_two);
        childholeThree=(Button)findViewById(R.id.childhole_three);
        childholeFour=(Button)findViewById(R.id.childhole_four);
        childholeFive=(Button)findViewById(R.id.childhole_five);
        childholeSix=(Button)findViewById(R.id.childhole_six);
         /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWind
        rsjName=(EditText)findViewById(R.id.rsj_name);ow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        sendCable.setOnClickListener(onClickListener);
        cancelCable.setOnClickListener(onClickListener);

        opticalcableList=new ArrayList<>();
        List<OpticalCable> opticalCableList= DataSupport.findAll(OpticalCable.class);
        if(opticalCableList!=null){
            for(int i=0;i<opticalCableList.size();i++){
                opticalcableList.add(opticalCableList.get(i).getOpticalcablename());
                str.add(opticalCableList.get(i).getOpticalcablename());
            }
        }
        SearchAdapter<String> opticalCableadapter=new SearchAdapter<String>(context
                ,R.layout.support_simple_spinner_dropdown_item,str,SearchAdapter.ALL);
        search.setAdapter(opticalCableadapter);

        //创建新的光缆
        createCable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpticalCable();
            }
        });

        //根据上一节点设置光缆段起点
        final List<String> cableNode=new ArrayList<>();
        for(int i=0;i<cacheList.size();i++){
            NodeCache nodeCache=cacheList.get(i);
            cableNode.add(nodeCache.getStartnodename());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,cableNode);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cableOrigin.setAdapter(adapter);

        //根据选择的光缆段起点设置管孔数
        cableOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cableoriginname=position;
                if(adapterhole!=null){
                    adapterhole.clear();
                    adapterhole.notifyDataSetChanged();
                }
                hole=Integer.valueOf(cacheList.get(position).getPipelinehole());
                for(int a=1;a<=hole;a++){
                    childholes.add(String.valueOf(a));
                }
                adapterhole=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,childholes);
                adapterhole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                childhoel.setAdapter(adapterhole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        childhoel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //根起点以及管孔 初始化子管孔的颜色
                holename=position+1;

                gainButtonColor();//管孔选择之后初始化子管孔的颜色

                //子管孔是否被占用 占用则改变颜色
                if(cableCacheList.size()>0){
                    List<String> list=new ArrayList<>();
                    for(int i=0;i<cableCacheList.size();i++){
                        CableCache cableCache=cableCacheList.get(i);

                        System.out.println("aaaaaaaaaaaa1="+cableCache.getCableorigin());
                        System.out.println("aaaaaaaaaaaa2="+cacheList.get(cableoriginname).getStartnodename());
                        System.out.println("aaaaaaaaaaaa3="+cableCache.getHole());
                        System.out.println("aaaaaaaaaaaa4="+String.valueOf(holename));


                        if(cableCache.getCableorigin().equals(cacheList.get(cableoriginname).getStartnodename())
                                && cableCache.getHole().equals(String.valueOf(holename))){
                            String s=cableCache.getChildhole();
                            list.add(s);
                        }
                    }
                    if(list.size()>0){
                        for(int i=0;i<list.size();i++){
                            String x=list.get(i);
                            if(childholeOne.getText().toString().equals(x)){
                                childholeOne.setBackgroundColor(Color.parseColor("#dbdbde"));
                            }else if(childholeTwo.getText().toString().equals(x)){
                                childholeTwo.setBackgroundColor(Color.parseColor("#dbdbde"));
                            }else if(childholeThree.getText().toString().equals(x)){
                                childholeThree.setBackgroundColor(Color.parseColor("#dbdbde"));
                            }else if(childholeFour.getText().toString().equals(x)){
                                childholeFour.setBackgroundColor(Color.parseColor("#dbdbde"));
                            }else if(childholeFive.getText().toString().equals(x)){
                                childholeFive.setBackgroundColor(Color.parseColor("#dbdbde"));
                            }else if(childholeSix.getText().toString().equals(x)){
                                childholeSix.setBackgroundColor(Color.parseColor("#dbdbde"));
                            }
                        }
                        list.removeAll(list);
                    }else {
                        gainButtonColor();
                    }
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        allCable.setOnClickListener(this);
        childholeOne.setOnClickListener(this);
        childholeTwo.setOnClickListener(this);
        childholeThree.setOnClickListener(this);
        childholeFour.setOnClickListener(this);
        childholeFive.setOnClickListener(this);
        childholeSix.setOnClickListener(this);

    }
    public void gainButtonColor(){
        childholeOne.setBackgroundColor(Color.parseColor("#DC143C"));
        childholeTwo.setBackgroundColor(Color.parseColor("#800080"));
        childholeThree.setBackgroundColor(Color.parseColor("#0000FF"));
        childholeFour.setBackgroundColor(Color.parseColor("#da711c"));
        childholeFive.setBackgroundColor(Color.parseColor("#FFFF00"));
        childholeSix.setBackgroundColor(Color.parseColor("#87CEEB"));
    }

    //创建光缆
    OpticalCableDialog opticalCableDialog;

    private void showOpticalCable(){
        opticalCableDialog=new OpticalCableDialog(context,R.style.loading_dialog,monClickListener,onItemSelectedListener);
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

                                    OpticalCable opticalCable=new OpticalCable();
                                    opticalCable.setOpticalcablename(opticalcablename);
                                    opticalCable.setCoremodel(opticalcablemodel);
                                    opticalCable.setCoreamount(coreamount);
                                    opticalCable.save();

                                    opticalcableList.add(opticalcablename);//添加到list
                                }else {
                                    toastMarkt("光缆名重复，请输入正确的光缆名！");
                                }
                                countOptocalname=0;
                            }else {

                                OpticalCable opticalCable=new OpticalCable();//添加到数据库
                                opticalCable.setOpticalcablename(opticalcablename);
                                opticalCable.setCoremodel(opticalcablemodel);
                                opticalCable.setCoreamount(coreamount);
                                opticalCable.save();

                                opticalcableList.add(opticalcablename);//添加到list
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

    protected  void actionAlertDialog(){
        List<String> strings=new ArrayList<>();
        List<OpticalCable> opticalCables=DataSupport.findAll(OpticalCable.class);
        if(opticalCables.size()>0){
            for(int i=0;i<opticalCables.size();i++){
                OpticalCable opticalCable=opticalCables.get(i);
                strings.add(opticalCable.getOpticalcablename());
            }

            AlertDialog.Builder builder;
            final AlertDialog alertDialog;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.layout_myview, (ViewGroup)findViewById(R.id.layout_myview));
            final ListView myListView = (ListView) layout.findViewById(R.id.mylistview);
            RoundNodeAdapter adapter = new RoundNodeAdapter(context, strings);
            myListView.setAdapter(adapter);
            builder = new AlertDialog.Builder(context);
            builder.setView(layout);
            alertDialog = builder.create();
            alertDialog.show();

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    alertDialog.dismiss();
                    search.setText(myListView.getAdapter().getItem(position).toString().trim());
                }
            });
        }else {
            toastMarkt("没有可选择光缆,请先创建新的光缆!");
        }


    }
    private void toastMarkt(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    private void buttonClick(Button button,String buttonname){
        System.out.println("childholename"+childholename);

        List<String> bijiao=new ArrayList<>();
        if(cableCacheList.size()>0){
            for(int i=0;i<cableCacheList.size();i++){
                CableCache cableCache=cableCacheList.get(i);
                if(cableCache.getCableorigin().equals(cacheList.get(cableoriginname).getStartnodename())
                        && cableCache.getHole().equals(String.valueOf(holename))){
                    String s=cableCache.getChildhole();
                    bijiao.add(s);
                }
            }
            if(bijiao.size()>0){
                if(bijiao.contains(buttonname)){
                    toastMarkt("该子孔已占用");
                }else {

                    if(childholename!=null){
                        if(childholename.equals(buttonname)){
                            seetingButtonColor(button,buttonname);
                            childholename=null;
                        }else {
                            toastMarkt("子孔已选择!");
                        }

                    }else {
                        button.setBackgroundColor(Color.parseColor("#dbdbde"));
                        childholename=buttonname;
                    }
                }
            }else {
                if(childholename!=null){
                    if(childholename.equals(buttonname)){
                        seetingButtonColor(button,buttonname);
                        childholename=null;
                    }else {
                        toastMarkt("子孔已选择!");
                    }

                }else {
                    button.setBackgroundColor(Color.parseColor("#dbdbde"));
                    childholename=buttonname;
                }
            }
        }else {
            if(childholename!=null){
                if(childholename.equals(buttonname)){
                    seetingButtonColor(button,buttonname);
                    childholename=null;
                }else {
                    toastMarkt("子孔已选择!");
                }

            }else {
                button.setBackgroundColor(Color.parseColor("#dbdbde"));
                childholename=buttonname;
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.childhole_one:
                String s1=childholeOne.getText().toString().trim();
                buttonClick(childholeOne,s1);
                break;
            case R.id.childhole_two:
                String s2=childholeTwo.getText().toString().trim();
                buttonClick(childholeTwo,s2);
                break;
            case R.id.childhole_three:
                String s3=childholeThree.getText().toString().trim();
                buttonClick(childholeThree,s3);
                break;
            case R.id.childhole_four:
                String s4=childholeFour.getText().toString().trim();
                buttonClick(childholeFour,s4);
                break;
            case R.id.childhole_five:
                String s5=childholeFive.getText().toString().trim();
                buttonClick(childholeFive,s5);
                break;
            case R.id.childhole_six:
                String s6=childholeSix.getText().toString().trim();
                buttonClick(childholeSix,s6);
                break;
            case R.id.all_cable:
                actionAlertDialog();
                break;
        }
    }

    private void seetingButtonColor(Button button, String x){

        if(x.equals("1")){
            button.setBackgroundColor(Color.parseColor("#DC143C"));
        }else if(x.equals("2")){
            button.setBackgroundColor(Color.parseColor("#800080"));
        }else if(x.equals("3")){
            button.setBackgroundColor(Color.parseColor("#0000FF"));
        }else if(x.equals("4")){
            button.setBackgroundColor(Color.parseColor("#da711c"));
        }else if(x.equals("5")){
            button.setBackgroundColor(Color.parseColor("#FFFF00"));
        }else if(x.equals("6")){
            button.setBackgroundColor(Color.parseColor("#87CEEB"));
        }
    }


}
