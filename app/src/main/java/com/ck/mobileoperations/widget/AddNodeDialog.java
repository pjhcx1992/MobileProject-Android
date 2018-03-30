package com.ck.mobileoperations.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.entity.GJiaoX;
import com.ck.mobileoperations.entity.GuaQiang;
import com.ck.mobileoperations.entity.JieTouHe;
import com.ck.mobileoperations.entity.PeopleWell;
import com.ck.mobileoperations.entity.PointSet;
import com.ck.mobileoperations.entity.Pole;
import com.ck.mobileoperations.entity.YinShangDian;
import com.ck.mobileoperations.utils.RoundNodeAdapter;
import com.ck.mobileoperations.utils.SearchAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnItemClick;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by chenkai on 2018/1/31.
 */

public class AddNodeDialog extends Dialog {
    public AutoCompleteTextView search;
    private List<String> str=new ArrayList<>();
    private String projectId;
    Activity context;
    public Spinner guandaoLX;
    public Spinner guandaoGG;
    private Button cancelNode,sendNode,fujinNode;
    private View.OnClickListener onClickListener;
    private LatLng latLng;
    public EditText guandaoName,guandaoKS;


    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    public AddNodeDialog(Activity context) {
        super(context);
        this.context=context;
    }

    public AddNodeDialog(Activity context, int theme, View.OnClickListener onClickListener
            , AdapterView.OnItemSelectedListener onItemSelectedListener, LatLng latLng,String projectId) {
        super(context, theme);
        this.context=context;
        this.onClickListener=onClickListener;
        this.onItemSelectedListener=onItemSelectedListener;
        this.latLng=latLng;
        this.projectId=projectId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_addnode_dialog);
        search = (AutoCompleteTextView) findViewById(R.id.search);
        cancelNode=(Button)findViewById(R.id.cancel_node);
        sendNode=(Button)findViewById(R.id.send_node);
        fujinNode=(Button)findViewById(R.id.fujin_node_rsj);
        guandaoName=(EditText)findViewById(R.id.guandao_name);
        guandaoKS=(EditText)findViewById(R.id.guandao_ks);
        guandaoLX=(Spinner)findViewById(R.id.guandao_leixing);
        guandaoGG=(Spinner)findViewById(R.id.guandao_guige);
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

        List<PeopleWell> peopleWells=DataSupport.where("projectId=?",projectId).find(PeopleWell.class);
        if(peopleWells.size()!=0){
            for(int i=0;i<peopleWells.size();i++){

                PeopleWell peopleWell=peopleWells.get(i);
                if(!peopleWell.getLatitude().equals(String.valueOf(latLng.latitude))
                        && !peopleWell.getLongitude().equals(String.valueOf(latLng.longitude))){
                    str.add(peopleWell.getWellname());
                }
            }
        }
        List<Pole> poles=DataSupport.where("projectId=?",projectId).find(Pole.class);
        if(poles.size()!=0){
            for(int i=0;i<poles.size();i++){
                Pole pole=poles.get(i);
                if(!pole.getLatitude().equals(String.valueOf(latLng.latitude))
                        && !pole.getLongitude().equals(String.valueOf(latLng.longitude))){
                    str.add(pole.getPolename());
                }
            }
        }

        List<GJiaoX> gJiaoXList=DataSupport.where("projectId=?",projectId).find(GJiaoX.class);
        if(gJiaoXList.size()!=0){
            for(int i=0;i<gJiaoXList.size();i++){

                GJiaoX pole=gJiaoXList.get(i);
                if(!pole.getLatitude().equals(String.valueOf(latLng.latitude))
                        && !pole.getLongitude().equals(String.valueOf(latLng.longitude))){
                    str.add(pole.getGjxname());
                }
            }
        }
        List<YinShangDian> yinShangDianList=DataSupport.where("projectId=?",projectId).find(YinShangDian.class);
        if(yinShangDianList.size()!=0){
            for(int i=0;i<yinShangDianList.size();i++){

                YinShangDian pole=yinShangDianList.get(i);
                if(!pole.getLatitude().equals(String.valueOf(latLng.latitude))
                        && !pole.getLongitude().equals(String.valueOf(latLng.longitude))){
                    str.add(pole.getYsdname());
                }
            }
        }

        List<GuaQiang> guaQiangList=DataSupport.where("projectId=?",projectId).find(GuaQiang.class);
        if(guaQiangList.size()!=0){
            for(int i=0;i<guaQiangList.size();i++){

                GuaQiang pole=guaQiangList.get(i);
                if(!pole.getLatitude().equals(String.valueOf(latLng.latitude))
                        && !pole.getLongitude().equals(String.valueOf(latLng.longitude))){
                    str.add(pole.getGqname());
                }
            }
        }
        List<JieTouHe> jieTouHeList=DataSupport.where("projectId=?",projectId).find(JieTouHe.class);
        if(jieTouHeList.size()!=0){
            for(int i=0;i<jieTouHeList.size();i++){

                JieTouHe pole=jieTouHeList.get(i);
                if(!pole.getLatitude().equals(String.valueOf(latLng.latitude))
                        && !pole.getLongitude().equals(String.valueOf(latLng.longitude))){
                    str.add(pole.getJthname());
                }
            }
        }

        SearchAdapter<String> adapter=new SearchAdapter<String>(context
                ,R.layout.support_simple_spinner_dropdown_item,str,SearchAdapter.ALL);
        search.setAdapter(adapter);


        cancelNode.setOnClickListener(onClickListener);
        sendNode.setOnClickListener(onClickListener);
        guandaoLX.setOnItemSelectedListener(onItemSelectedListener);
        guandaoGG.setOnItemSelectedListener(onItemSelectedListener);
        this.setCancelable(true);

        fujinNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionAlertDialog();
            }
        });


    }

    protected  void actionAlertDialog(){
        List<String> strings=new ArrayList<>();
        /*
        List<PeopleWell> peopleWells=DataSupport.where("projectId=?",projectId).find(PeopleWell.class);
        if(peopleWells.size()!=0){
            for(int i=0;i<peopleWells.size();i++){

                PeopleWell peopleWell=peopleWells.get(i);
                float distance = AMapUtils.calculateLineDistance(latLng
                        ,new LatLng(Double.valueOf(peopleWell.getLatitude()),Double.valueOf(peopleWell.getLongitude())));
                if(distance<=20 && distance >0){
                    strings.add(peopleWells.get(i).getWellname());
                }
            }
        }

        List<Pole> poles=DataSupport.where("projectId=?",projectId).find(Pole.class);
        if(poles.size()!=0){
            for(int i=0;i<poles.size();i++){
                Pole pole=poles.get(i);
                float distance = AMapUtils.calculateLineDistance(latLng
                        ,new LatLng(Double.valueOf(pole.getLatitude()),Double.valueOf(pole.getLongitude())));
                if(distance<=20 && distance >0){
                    strings.add(pole.getPolename());
                }
            }
        }

        List<GJiaoX> gJiaoXList=DataSupport.where("projectId=?",projectId).find(GJiaoX.class);
        if(gJiaoXList.size()!=0){
            for(int i=0;i<gJiaoXList.size();i++){

                GJiaoX pole=gJiaoXList.get(i);
                float distance = AMapUtils.calculateLineDistance(latLng
                        ,new LatLng(Double.valueOf(pole.getLatitude()),Double.valueOf(pole.getLongitude())));
                if(distance<=20 && distance >0){
                    strings.add(pole.getGjxname());
                }
            }
        }
        List<YinShangDian> yinShangDianList=DataSupport.where("projectId=?",projectId).find(YinShangDian.class);
        if(yinShangDianList.size()!=0){
            for(int i=0;i<yinShangDianList.size();i++){

                YinShangDian pole=yinShangDianList.get(i);
                float distance = AMapUtils.calculateLineDistance(latLng
                        ,new LatLng(Double.valueOf(pole.getLatitude()),Double.valueOf(pole.getLongitude())));
                if(distance<=20 && distance >0){
                    strings.add(pole.getYsdname());
                }
            }
        }

        List<GuaQiang> guaQiangList=DataSupport.where("projectId=?",projectId).find(GuaQiang.class);
        if(guaQiangList.size()!=0){
            for(int i=0;i<guaQiangList.size();i++){

                GuaQiang pole=guaQiangList.get(i);
                float distance = AMapUtils.calculateLineDistance(latLng
                        ,new LatLng(Double.valueOf(pole.getLatitude()),Double.valueOf(pole.getLongitude())));
                if(distance<=20 && distance >0){
                    strings.add(pole.getGqname());
                }
            }
        }
        List<JieTouHe> jieTouHeList=DataSupport.where("projectId=?",projectId).find(JieTouHe.class);
        if(jieTouHeList.size()!=0){
            for(int i=0;i<jieTouHeList.size();i++){

                JieTouHe pole=jieTouHeList.get(i);
                float distance = AMapUtils.calculateLineDistance(latLng
                        ,new LatLng(Double.valueOf(pole.getLatitude()),Double.valueOf(pole.getLongitude())));
                if(distance<=20 && distance >0){
                    strings.add(pole.getJthname());
                }
            }
        }

        if(strings.size()>0){
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
            Toast.makeText(getContext(),"附近没有节点!",Toast.LENGTH_SHORT).show();
        }*/


        List<PointSet>pointSetList= DataSupport.where("projrctId=?",projectId).find(PointSet.class);
        if(pointSetList.size()!=0 && pointSetList!=null){
            for(int i=0;i<pointSetList.size();i++){
//                strings.add(pointSetList.get(i).getMarkname());
                PointSet pointSet=pointSetList.get(i);
                float distance = AMapUtils.calculateLineDistance(latLng,new LatLng(Double.valueOf(pointSet.getLatitude())
                        ,Double.valueOf(pointSet.getLongitude())));
                if(distance<=20 && distance >0){
                    strings.add(pointSetList.get(i).getMarkname());
                }
            }


            if(strings.size()>0){
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
                Toast.makeText(getContext(),"附近没有节点!",Toast.LENGTH_SHORT).show();
            }


        }else {
           Toast.makeText(getContext(),"附近没有节点!",Toast.LENGTH_SHORT).show();
        }


    }
}
