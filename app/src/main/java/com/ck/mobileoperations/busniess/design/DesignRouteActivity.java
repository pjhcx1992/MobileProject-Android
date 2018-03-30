package com.ck.mobileoperations.busniess.design;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.GestureUtils;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;

import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.amap.api.trace.TraceStatusListener;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.design.model.ProjectModelImpl;
import com.ck.mobileoperations.entity.CableResource;
import com.ck.mobileoperations.entity.GJiaoX;
import com.ck.mobileoperations.entity.GuaQiang;
import com.ck.mobileoperations.entity.JieTouHe;
import com.ck.mobileoperations.entity.OpticalCable;
import com.ck.mobileoperations.entity.PeopleWell;
import com.ck.mobileoperations.entity.Pipeline;
import com.ck.mobileoperations.entity.PointSet;
import com.ck.mobileoperations.entity.Pole;
import com.ck.mobileoperations.entity.ProName;
import com.ck.mobileoperations.entity.YinShangDian;
import com.ck.mobileoperations.utils.Constant;
import com.ck.mobileoperations.utils.OrgRequestResultListener;
import com.ck.mobileoperations.utils.RequestResultListener;
import com.ck.mobileoperations.utils.SwipeListLayout;
import com.ck.mobileoperations.utils.UpdateDataUtil;
import com.ck.mobileoperations.utils.cable.CableCache;
import com.ck.mobileoperations.utils.node.NodeCache;
import com.ck.mobileoperations.widget.AddNodeDialog;
import com.ck.mobileoperations.widget.CreateMarkDialog;
import com.ck.mobileoperations.widget.DianGanDialog;
import com.ck.mobileoperations.widget.GuaQiangDialog;
import com.ck.mobileoperations.widget.GuangJiaoXiangDialog;
import com.ck.mobileoperations.widget.JieTouHeDialog;
import com.ck.mobileoperations.widget.RenShouJingDialog;
import com.ck.mobileoperations.widget.SelectCableDialog;
import com.ck.mobileoperations.widget.YinShangDianDialog;
import com.google.android.gms.ads.internal.overlay.zzo;

import org.litepal.crud.DataSupport;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import butterknife.Bind;
import butterknife.ButterKnife;

import static com.ck.mobileoperations.R.drawable.diangan_mark;
import static com.ck.mobileoperations.R.drawable.gjjx_mark;
import static com.ck.mobileoperations.R.drawable.gq_mark;
import static com.ck.mobileoperations.R.drawable.guaqiang;
import static com.ck.mobileoperations.R.drawable.jth_mark;
import static com.ck.mobileoperations.R.drawable.marks;
import static com.ck.mobileoperations.R.drawable.position;
import static com.ck.mobileoperations.R.drawable.rsj_mark;
import static com.ck.mobileoperations.R.drawable.yinshangdian;
import static com.ck.mobileoperations.R.drawable.ysd_mark;
import static org.litepal.tablemanager.Connector.getWritableDatabase;


/**
 * Created by CK on 2017/12/18.
 */

public class DesignRouteActivity extends Activity implements AMap.OnMapClickListener,
        AMap.OnMapLongClickListener,AMap.OnCameraChangeListener,AMap.OnMapTouchListener,AMap.InfoWindowAdapter
        ,TraceStatusListener{
    private static final String TAG="DesignRouteActivity";
    private MapView mMapView = null;
    private AMap aMap;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private double x;
    private double y;
    private AMapLocation aMapLocation;
    private Marker curShowWindowMarker;
    private String proname;
    private List<NodeCache> nodeCacheList=new ArrayList<>();
    public List<CableCache> cableCacheList=new ArrayList<>();
    private List<CableCache> cableCaches=new ArrayList<>();
    private ProjectModelImpl projectModel;
    private RequestQueue mQueue;
    private Polyline polylines;
    private List<Polyline> polylineList=new ArrayList<>();
    private String selectorganizationId;
    private String MarkName;
    private List<String> conduitlistIds=new ArrayList();
    private List<String> cableSegmentlistIds=new ArrayList();
    private List<String> removeConduit=new ArrayList<>();
    private List<String> removeCableSegment=new ArrayList<>();
    private List<NodeCache> addConduitArray=new ArrayList<>();
    private List<CableCache> addCableSegmentArray=new ArrayList<>();

    private List<Map<String,String>> addConduit=new ArrayList<>();
    private List<Map<String,String>> addCableSegment=new ArrayList<>();

    private List<Map<String,String>> mapnull=new ArrayList<>();
    private List<String>  testmull=new ArrayList<>();
    private Marker markerStart;
    @Bind(R.id.set_the_tag)
    Button setTag;
    @Bind(R.id.cancel_the_tag)
    Button cancelTag;
    @Bind(R.id.cancel_plan)
    TextView  cancelPlan;
    @Bind(R.id.save_plan)
    TextView savePlan;

    private ProName project;
    CreateMarkDialog createMarkDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_design_route);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        init();
        initLocation();
        startLocation();//开始定位
        mQueue = Volley.newRequestQueue(DesignRouteActivity.this);
        projectModel=new ProjectModelImpl(mQueue);

    }

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private void init(){

        if(aMap==null){
            aMap = mMapView.getMap();
            setUpMap();
        }
        aMap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE(卫星地图模式),MAP_TYPE_NIGHT,
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        cancelPlan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        cancelPlan.getPaint().setAntiAlias(true);
        cancelPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        savePlan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        savePlan.getPaint().setAntiAlias(true);
        savePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送数据到服务器


                projectModel.volleyProjectStatus(project.getProjectId(), "1", new RequestResultListener() {
                    @Override
                    public void onSuccess(JSONObject s) {
                        Log.i(TAG,"project update status success "+s);
                        project.setStatus("1");
                        project.save();

                        Intent intent=new Intent(getApplicationContext(),SheJiYuanActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String s) {
                        Log.i(TAG,"project update status error "+s);
                    }
                });



            }
        });

        /*
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));*/

        //设置以前的标记
        SetTheTag();
//        startTrace(); //轨迹纠偏

        //上一节点连线
      markLigature();

    }
    private void SetTheTag(){
        //TODO (4) 设置数据库保存的标记到地图
        proname=getIntent().getStringExtra("proname");
        selectorganizationId=getIntent().getStringExtra("selectorganizationId");
        project=DataSupport.where("proname=? and orgId=?",proname,selectorganizationId).find(ProName.class).get(0);


        List<PeopleWell> peopleWellList= DataSupport.where("orgId=?",project.getOrgId()).find(PeopleWell.class);
        if(peopleWellList!=null){
            for(int i=0;i<peopleWellList.size();i++){
                PeopleWell peopleWell=peopleWellList.get(i);
//                LatLng latLng=new LatLng(Double.parseDouble(peopleWell.getLatitude().),Double.parseDouble(peopleWell.getLongitude()));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(peopleWell.getLatitude()),Double.valueOf(peopleWell.getLongitude())));
                markerOptions.title("人手井"+peopleWell.getWellname());
                markerOptions.draggable(true);
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), rsj_mark));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);
            }
        }

        List<Pole> poleList=DataSupport.where("orgId=?",project.getOrgId()).find(Pole.class);
        if(poleList!=null){
            for(int i=0;i<poleList.size();i++){
                Pole pole=poleList.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(pole.getLatitude()),Double.valueOf(pole.getLongitude())));
                markerOptions.title("电杆"+pole.getPolename());
                markerOptions.draggable(true);
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), diangan_mark));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            }
        }
        List<GJiaoX> gJiaoXList=DataSupport.where("orgId=?",project.getOrgId()).find(GJiaoX.class);
        if(gJiaoXList!=null){
            for(int i=0;i<gJiaoXList.size();i++){
                GJiaoX gJiaoX=gJiaoXList.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(gJiaoX.getLatitude()),Double.valueOf(gJiaoX.getLongitude())));
                markerOptions.title("光交接箱"+gJiaoX.getGjxname());
                markerOptions.draggable(true);
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), gjjx_mark));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            }
        }
        List<YinShangDian> yinShangDianList=DataSupport.where("orgId=?",project.getOrgId()).find(YinShangDian.class);
        if(yinShangDianList!=null){
            for(int i=0;i<yinShangDianList.size();i++){
                YinShangDian yinShangDian=yinShangDianList.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(yinShangDian.getLatitude()),Double.valueOf(yinShangDian.getLongitude())));
                markerOptions.title("引上点"+yinShangDian.getYsdname());
                markerOptions.draggable(true);
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), ysd_mark));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);
            }
        }
        List<GuaQiang> guaQiangList=DataSupport.where("orgId=?",project.getOrgId()).find(GuaQiang.class);
        if(guaQiangList!=null){
            for(int i=0;i<guaQiangList.size();i++){
                GuaQiang guaQiang=guaQiangList.get(i);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(guaQiang.getLatitude()),Double.valueOf(guaQiang.getLongitude())));
                markerOptions.title("guaqiang"+guaQiang.getGqname());
                markerOptions.draggable(true);
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), gq_mark));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            }
        }

        List<JieTouHe> jieTouHeList=DataSupport.where("orgId=?",project.getOrgId()).find(JieTouHe.class);
        if(jieTouHeList!=null){
            for(int i=0;i<jieTouHeList.size();i++){
                JieTouHe jieTouHe=jieTouHeList.get(i);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.valueOf(jieTouHe.getLatitude()),Double.valueOf(jieTouHe.getLongitude())));
                markerOptions.title("jietouhe"+jieTouHe.getJthname());
                markerOptions.draggable(true);
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), jth_mark));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);
            }
        }

    }

    //标记连线（管道）
    private void markLigature(){

        //移除所有的线
        if(polylineList.size()>0){
            for(int i=0;i<polylineList.size();i++){
                Polyline polyline=polylineList.get(i);
                polyline.remove();
            }
        }

       List<Pipeline> pipelineList=DataSupport.where("projectId=? and orgId=?"
               ,project.getProjectId(),project.getOrgId()).find(Pipeline.class);

       if(pipelineList.size()>0){
           for(int i=0;i<pipelineList.size();i++){
               Pipeline pipeline=pipelineList.get(i);

               PolylineOptions polylineOptions=new PolylineOptions();
               polylineOptions.add(
                       new LatLng(Double.valueOf(pipeline.getStartLatitude()),Double.valueOf(pipeline.getStartLongitude())),
                       new LatLng(Double.valueOf(pipeline.getEndLatitude()),Double.valueOf(pipeline.getEndLongitude()))
               ).color(R.color.colorPrimaryDark).width(6.0f);
               polylines=aMap.addPolyline(polylineOptions);
               polylineList.add(polylines);
           }

       }
    }

    //amap添加一些事件监听器
    private void setUpMap() {
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
        UiSettings uiSettings;
        uiSettings=aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
    }

    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
    private String getGPSStatusString(int statusCode){
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    //定位返回监听
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");
                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    /*lat = amapLocation.getLatitude();
                    lon = amapLocation.getLongitude();*/
                    //显示定位到的经纬度到地图
                    positioningShows( location);

                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("****************").append("\n");
                //解析定位结果，
                String result = sb.toString();
            }
        }
    };

    //开始定位
    private void startLocation(){
        //高精度
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(false);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差

        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(30000);

        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    private Marker marker;
    //显示定位到的经纬度到地图
   private void  positioningShows( final AMapLocation location){
       aMapLocation=location;
       aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 19));

       if(marker==null){
           MarkerOptions markerOptions = new MarkerOptions();
           markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
           markerOptions.title("当前位置");
           markerOptions.draggable(true);
           markerOptions.visible(true);
           BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.position));
           markerOptions.icon(bitmapDescriptor);
           marker=aMap.addMarker(markerOptions);
       }else {
           marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
       }

       //定位(标记)点没有移动之前设置标记
       setTag.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v) {
               showCreateUserDialog(v,new LatLng(location.getLatitude(), location.getLongitude()));
           }
       });


       // 定义 Marker拖拽的监听
       AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {

           // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
           // 这个位置可能与拖动的之前的marker位置不一样。
           // marker 被拖动的marker对象。
           @Override
           public void onMarkerDragStart(Marker arg0) {
               if(!arg0.getTitle().equals("当前位置")){
                   //开始移动标记点的时候移除所有的线
                   if(polylineList!=null){
                       for(int i=0;i<polylineList.size();i++){
                           Polyline polyline=polylineList.get(i);
                           polyline.remove();
                       }
                   }

               }

           }
           // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
           // 这个位置可能与拖动的之前的marker位置不一样。
           // marker 被拖动的marker对象。
           @Override
           public void onMarkerDragEnd(final Marker arg0) {

               //TODO (3)标记移动结束之后更新，新的经纬度信息到数据库
               final LatLng latLng=arg0.getPosition();
               final LatLng startlatlng=marker.getPosition();

               String s=arg0.getTitle().trim();
               if(s.indexOf("人手井")!=-1){
                   s=s.replace("人手井","");

                   final PeopleWell peopleWell=DataSupport.where("wellname=? and projectId=?"
                           ,s,project.getProjectId()).find(PeopleWell.class).get(0);

                   updatePointSet(s,arg0);
                   projectModel.volleyAnnotationUpdate("WELL", peopleWell.getAnnotationId(), peopleWell.getSpecifications(),
                           String.valueOf(arg0.getPosition().latitude), String.valueOf(arg0.getPosition().longitude), "", "", "",
                           mapnull,mapnull,testmull,testmull,
                           new RequestResultListener() {
                               @Override
                               public void onSuccess(JSONObject s) {
                                   Log.i(TAG,"WELL update success="+s);
                                   peopleWell.setLatitude(Double.toString(arg0.getPosition().latitude));
                                   peopleWell.setLongitude(Double.toString(arg0.getPosition().longitude));
                                   peopleWell.save();


                                   //更改管道和光缆资源表
                                   List<Pipeline> startpipelineList=DataSupport.where("startnodename=? and projectId=?"
                                           ,peopleWell.getWellname(),project.getProjectId()).find(Pipeline.class);
                                   if(startpipelineList.size()>0){
                                       for(int i=0;i<startpipelineList.size();i++){
                                           Pipeline pipeline=startpipelineList.get(i);
                                           pipeline.setStartLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setStartLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setStartPointType(peopleWell.getSpecifications());
                                           pipeline.save();
                                       }
                                   }

                                   List<Pipeline> endpipelineList=DataSupport.where("endnodename=? and projectId=?"
                                           ,peopleWell.getWellname(),project.getProjectId()).find(Pipeline.class);
                                   if(endpipelineList.size()>0){
                                       for(int i=0;i<endpipelineList.size();i++){
                                           Pipeline pipeline=endpipelineList.get(i);
                                           pipeline.setEndLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setEndLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setEndPointType(peopleWell.getSpecifications());
                                           pipeline.save();
                                       }
                                   }

                                   //移动结束之后重新绘制连线
                                   markLigature();

                               }
                               @Override
                               public void onError(String s) {
                                   Log.i(TAG,"WELL update error="+s);
                               }
                           });

               }else if (s.indexOf("电杆")!=-1){
                   s=s.replace("电杆","");
                  final Pole pole=DataSupport.where("polename=? and projectId=?",s,project.getProjectId()).find(Pole.class).get(0);

                   updatePointSet(s,arg0);


                   projectModel.volleyAnnotationUpdate("POLE", pole.getAnnotationId(), "",
                           String.valueOf(arg0.getPosition().latitude), String.valueOf(arg0.getPosition().longitude),
                           pole.getPoleheight(), pole.getFencha(), "",
                           mapnull,mapnull,testmull,testmull,
                           new RequestResultListener() {
                               @Override
                               public void onSuccess(JSONObject s) {
                                   Log.i(TAG,"POLE update success="+s);
                                   pole.setLatitude(Double.toString(arg0.getPosition().latitude));
                                   pole.setLongitude(Double.toString(latLng.longitude));
                                   pole.save();

                                   //更改管道和光缆资源表
                                   List<Pipeline> startpipelineList=DataSupport.where("startnodename=? and projectId=?"
                                           ,pole.getPolename(),project.getProjectId()).find(Pipeline.class);
                                   if(startpipelineList.size()>0){
                                       for(int i=0;i<startpipelineList.size();i++){
                                           Pipeline pipeline=startpipelineList.get(i);
                                           pipeline.setStartLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setStartLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setStartPointType(pole.getFencha());
                                           pipeline.save();
                                       }
                                   }

                                   List<Pipeline> endpipelineList=DataSupport.where("endnodename=? and projectId=?"
                                           ,pole.getPolename(),project.getProjectId()).find(Pipeline.class);
                                   if(endpipelineList.size()>0){
                                       for(int i=0;i<endpipelineList.size();i++){
                                           Pipeline pipeline=endpipelineList.get(i);
                                           pipeline.setEndLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setEndLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setStartPointType(pole.getFencha());
                                           pipeline.save();
                                       }
                                   }
                                   //移动结束之后重新绘制连线
                                   markLigature();
                               }

                               @Override
                               public void onError(String s) {
                                   Log.i(TAG,"POLE update error="+s);
                               }
                           });

               }else if(s.indexOf("光交接箱")!=-1){
                   s=s.replace("光交接箱","");
                final GJiaoX gJiaoX=DataSupport.where("gjxname=? and projectId=?",s,project.getProjectId()).find(GJiaoX.class).get(0);

                   updatePointSet(s,arg0);
                   projectModel.volleyAnnotationUpdate("LIGHTBOX", gJiaoX.getAnnotationId(), gJiaoX.getFqdjibie(),
                           String.valueOf(arg0.getPosition().latitude), String.valueOf(arg0.getPosition().longitude),
                           "", "", gJiaoX.getGjxlb(),
                           mapnull,mapnull,testmull,testmull,
                           new RequestResultListener() {
                               @Override
                               public void onSuccess(JSONObject s) {
                                   Log.i(TAG,"LIGHTBOX update success="+s);
                                   gJiaoX.setLatitude(Double.toString(latLng.latitude));
                                   gJiaoX.setLongitude(Double.toString(latLng.longitude));
                                   gJiaoX.save();

                                   //更改管道和光缆资源表
                                   List<Pipeline> startpipelineList=DataSupport.where("startnodename=? and projectId=?"
                                           ,gJiaoX.getGjxname(),project.getProjectId()).find(Pipeline.class);
                                   if(startpipelineList.size()>0){
                                       for(int i=0;i<startpipelineList.size();i++){
                                           Pipeline pipeline=startpipelineList.get(i);
                                           pipeline.setStartLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setStartLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setStartPointType(gJiaoX.getFqdjibie());
                                           pipeline.save();
                                       }
                                   }

                                   List<Pipeline> endpipelineList=DataSupport.where("endnodename=? and projectId=?"
                                           ,gJiaoX.getGjxname(),project.getProjectId()).find(Pipeline.class);
                                   if(endpipelineList.size()>0){
                                       for(int i=0;i<endpipelineList.size();i++){
                                           Pipeline pipeline=endpipelineList.get(i);
                                           pipeline.setEndLongitude(gJiaoX.getLongitude());
                                           pipeline.setEndLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setEndLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.save();
                                       }
                                   }
                                   //移动结束之后重新绘制连线
                                   markLigature();
                               }

                               @Override
                               public void onError(String s) {
                                   Log.i(TAG,"LIGHTBOX update error="+s);
                               }
                           });

               }else if(s.indexOf("引上点")!=-1){
                   s=s.replace("引上点","");
                   final YinShangDian yinShangDian=DataSupport.where("ysdname=? and projectId=?",s,project.getProjectId()).find(YinShangDian.class).get(0);
                   updatePointSet(s,arg0);

                   projectModel.volleyAnnotationUpdate("UPPOINT", yinShangDian.getAnnotationId(), "",
                           String.valueOf(arg0.getPosition().latitude), String.valueOf(arg0.getPosition().longitude),
                           "", "", "",
                           mapnull,mapnull,testmull,testmull,
                           new RequestResultListener() {
                               @Override
                               public void onSuccess(JSONObject s) {
                                   Log.i(TAG,"UPPOINT update success");
                                   yinShangDian.setLongitude(Double.toString(latLng.longitude));
                                   yinShangDian.setLatitude(Double.toString(latLng.latitude));
                                   yinShangDian.save();

                                   //更改管道和光缆资源表
                                   List<Pipeline> startpipelineList=DataSupport.where("startnodename=? and projectId=?"
                                           ,yinShangDian.getYsdname(),project.getProjectId()).find(Pipeline.class);
                                   if(startpipelineList.size()>0){
                                       for(int i=0;i<startpipelineList.size();i++){
                                           Pipeline pipeline=startpipelineList.get(i);
                                           pipeline.setStartLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setStartLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setStartPointType("");
                                           pipeline.save();
                                       }
                                   }

                                   List<Pipeline> endpipelineList=DataSupport.where("endnodename=? and projectId=?"
                                           ,yinShangDian.getYsdname(),project.getProjectId()).find(Pipeline.class);
                                   if(endpipelineList.size()>0){
                                       for(int i=0;i<endpipelineList.size();i++){
                                           Pipeline pipeline=endpipelineList.get(i);
                                           pipeline.setEndLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setEndLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setEndPointType("");
                                           pipeline.save();
                                       }
                                   }
                                   //移动结束之后重新绘制连线
                                   markLigature();
                               }

                               @Override
                               public void onError(String s) {
                                   Log.i(TAG,"UPPOINT update error="+s);
                               }
                           });

               }else if(s.indexOf("guaqiang")!=-1){
                   s=s.replace("guaqiang","");
                   final GuaQiang guaQiang=DataSupport.where("gqname=? and projectId=?",s,project.getProjectId()).find(GuaQiang.class).get(0);
                   updatePointSet(s,arg0);

                   projectModel.volleyAnnotationUpdate("WALLHANG", guaQiang.getAnnotationId(), "",
                           String.valueOf(arg0.getPosition().latitude), String.valueOf(arg0.getPosition().longitude),
                           "", "", "",
                           mapnull,mapnull,testmull,testmull,
                           new RequestResultListener() {
                               @Override
                               public void onSuccess(JSONObject s) {
                                   Log.i(TAG,"WALLHANG update success");
                                   guaQiang.setLongitude(Double.toString(latLng.longitude));
                                   guaQiang.setLatitude(Double.toString(latLng.latitude));
                                   guaQiang.save();
                                   //更改管道和光缆资源表
                                   List<Pipeline> startpipelineList=DataSupport.where("startnodename=? and projectId=?"
                                           ,guaQiang.getGqname(),project.getProjectId()).find(Pipeline.class);
                                   if(startpipelineList.size()>0){
                                       for(int i=0;i<startpipelineList.size();i++){
                                           Pipeline pipeline=startpipelineList.get(i);
                                           pipeline.setStartLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setStartLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setStartPointType("");
                                           pipeline.save();
                                       }
                                   }

                                   List<Pipeline> endpipelineList=DataSupport.where("endnodename=? and projectId=?"
                                           ,guaQiang.getGqname(),project.getProjectId()).find(Pipeline.class);
                                   if(endpipelineList.size()>0){
                                       for(int i=0;i<endpipelineList.size();i++){
                                           Pipeline pipeline=endpipelineList.get(i);
                                           pipeline.setEndLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setEndLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setEndPointType("");
                                           pipeline.save();
                                       }
                                   }
                                   //移动结束之后重新绘制连线
                                   markLigature();
                               }

                               @Override
                               public void onError(String s) {
                                   Log.i(TAG,"WALLHANG update error");
                               }
                           });

               }else if(s.indexOf("jietouhe")!=-1){
                   s=s.replace("jietouhe","");
                   final JieTouHe jieTouHe=DataSupport.where("jthname=? and projectId=?",s,project.getProjectId()).find(JieTouHe.class).get(0);
                   updatePointSet(s,arg0);
                   projectModel.volleyAnnotationUpdate("JOINTBOX", jieTouHe.getAnnotationId(), "",
                           String.valueOf(arg0.getPosition().latitude), String.valueOf(arg0.getPosition().longitude),"", "", "",
                           mapnull,mapnull,testmull,testmull,
                           new RequestResultListener() {
                               @Override
                               public void onSuccess(JSONObject s) {
                                   Log.i(TAG,"JOINTBOX update success");
                                   jieTouHe.setLongitude(Double.toString(latLng.longitude));
                                   jieTouHe.setLatitude(Double.toString(latLng.latitude));
                                   jieTouHe.save();
                                   //更改管道和光缆资源表
                                   List<Pipeline> startpipelineList=DataSupport.where("startnodename=? and projectId=?"
                                           ,jieTouHe.getJthname(),project.getProjectId()).find(Pipeline.class);
                                   if(startpipelineList.size()>0){
                                       for(int i=0;i<startpipelineList.size();i++){
                                           Pipeline pipeline=startpipelineList.get(i);
                                           pipeline.setStartLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setStartLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setStartPointType("");
                                           pipeline.save();
                                       }
                                   }

                                   List<Pipeline> endpipelineList=DataSupport.where("endnodename=? and projectId=?"
                                           ,jieTouHe.getJthname(),project.getProjectId()).find(Pipeline.class);
                                   if(endpipelineList.size()>0){
                                       for(int i=0;i<endpipelineList.size();i++){
                                           Pipeline pipeline=endpipelineList.get(i);
                                           pipeline.setEndLongitude(Double.toString(arg0.getPosition().longitude));
                                           pipeline.setEndLatitude(Double.toString(arg0.getPosition().latitude));
                                           pipeline.setEndPointType("");
                                           pipeline.save();
                                       }
                                   }
                                   //移动结束之后重新绘制连线
                                   markLigature();

                               }

                               @Override
                               public void onError(String s) {
                                   Log.i(TAG,"JOINTBOX update error");

                               }
                           });
               }
               //定位点（标记）没有移动之后设置标记
               setTag.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(final View v) {
                       float distance = AMapUtils.calculateLineDistance(latLng,startlatlng);
                       if(distance>30){
                           toastMarkt("离定位距离超过30米无法进行此操作，请到范围内进行操作!");
                       }else {
                           showCreateUserDialog(v,new LatLng(latLng.latitude, latLng.longitude));
                       }

                   }
               });


           }

           // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
           // 这个位置可能与拖动的之前的marker位置不一样。
           // marker 被拖动的marker对象。
           @Override
           public void onMarkerDrag(Marker arg0) {
               // TODO Auto-generated method stub
               System.out.println("guochengzhong==="+arg0.getPosition().latitude);
           }
       };


       aMap.setOnMarkerDragListener(markerDragListener);// 绑定marker拖拽事件
       aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式

       AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
           // marker 对象被点击时回调的接口
           // 返回 true 则表示接口已响应事件，否则返回false
           @Override
           public boolean onMarkerClick(final Marker marker) {

               curShowWindowMarker=marker;
               cancelTag.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v){
                       new AlertDialog.Builder(v.getContext())
                               .setTitle("删除标记")
                               .setMessage("是否确定删除该标记")
                               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       //TODo 移除标记之后 数据库移除数据 移除绘线
                                       marker.remove();

                                       //移除所有的线
                                       if(polylineList.size()>0){
                                           for(int i=0;i<polylineList.size();i++){
                                               Polyline polyline=polylineList.get(i);
                                               polyline.remove();
                                           }
                                       }
                                       //数据库移除数据
                                       final LatLng latLng=marker.getPosition();
                                       String string=marker.getTitle().trim();
                                       if(string.indexOf("人手井")!=-1){
                                           string=string.replace("人手井","");
                                           MarkName=string;
                                           final PeopleWell peopleWell=DataSupport.where("wellname=? and projectId=?"
                                                   ,string,project.getProjectId()).find(PeopleWell.class).get(0);

                                           dataCollection(string);

                                           //服务器删除
                                           projectModel.volleyAnnotationDelete(peopleWell.getAnnotationId(), "WELL",conduitlistIds,cableSegmentlistIds, new RequestResultListener() {
                                               @Override
                                               public void onSuccess(JSONObject s) {
                                                   Log.i(TAG,"WELL delete success"+s);
                                                   if(peopleWell.isSaved()){
                                                       peopleWell.delete();
                                                   }
                                                   deleteMark(MarkName);//管道表删除关联管道，光缆资源表删除关联光缆资源
                                               }

                                               @Override
                                               public void onError(String s) {
                                                   Log.i(TAG,"WELL delete error"+s);
                                               }
                                           });






                                       }else if (string.indexOf("电杆")!=-1){
                                           string=string.replace("电杆","");

                                           final Pole pole=DataSupport.where("polename=? and projectId=?",string,project.getProjectId())
                                                   .find(Pole.class).get(0);
                                           MarkName=string;
                                           dataCollection(string);
                                           projectModel.volleyAnnotationDelete(pole.getAnnotationId(), "POLE",conduitlistIds,cableSegmentlistIds,
                                                   new RequestResultListener() {
                                                       @Override
                                                       public void onSuccess(JSONObject s) {
                                                           Log.i(TAG,"POLE delete success="+s);
                                                           if(pole.isSaved()){
                                                               pole.delete();
                                                           }
                                                           deleteMark(MarkName);
                                                       }

                                                       @Override
                                                       public void onError(String s) {
                                                           Log.i(TAG,"POLE delete error="+s);
                                                       }
                                                   });




                                       }else if(string.indexOf("光交接箱")!=-1){
                                           string=string.replace("光交接箱","");
                                           final GJiaoX gJiaoX=DataSupport.where("gjxname=? and projectId=",string,project.getProjectId())
                                                   .find(GJiaoX.class).get(0);
                                           MarkName=string;
                                           dataCollection(string);

                                           projectModel.volleyAnnotationDelete(gJiaoX.getAnnotationId(), "LIGHTBOX",conduitlistIds,cableSegmentlistIds,
                                                   new RequestResultListener() {
                                                       @Override
                                                       public void onSuccess(JSONObject s) {
                                                           Log.i(TAG,"LIGHTBOX delete success="+s);
                                                           if(gJiaoX.isSaved()){
                                                               gJiaoX.delete();
                                                           }
                                                           deleteMark(MarkName);
                                                       }

                                                       @Override
                                                       public void onError(String s) {
                                                           Log.i(TAG,"LIGHTBOX delete error="+s);
                                                       }
                                                   });




                                       }else if(string.indexOf("引上点")!=-1){
                                           string=string.replace("引上点","");
                                           MarkName=string;
                                           dataCollection(string);
                                           final YinShangDian yinShangDian=DataSupport.where("ysdname=? and projectId=?",string,project.getProjectId())
                                                   .find(YinShangDian.class).get(0);
                                           projectModel.volleyAnnotationDelete(yinShangDian.getAnnotationId(), "UPPOINT",conduitlistIds,cableSegmentlistIds,
                                                   new RequestResultListener() {
                                                       @Override
                                                       public void onSuccess(JSONObject s) {
                                                           Log.i(TAG,"UPPOINT delete success="+s);
                                                           if(yinShangDian.isSaved()){
                                                               yinShangDian.delete();
                                                           }
                                                           deleteMark(MarkName);
                                                       }

                                                       @Override
                                                       public void onError(String s) {
                                                           Log.i(TAG,"UPPOINT delete erroe="+s);
                                                       }
                                                   });




                                       }else if(string.indexOf("guaqiang")!=-1){
                                           string=string.replace("guaqiang","");
                                           final GuaQiang guaQiang=DataSupport.where("gqname=? and projectId=?",string,project.getProjectId())
                                                   .find(GuaQiang.class).get(0);
                                           MarkName=string;
                                           dataCollection(string);
                                           projectModel.volleyAnnotationDelete(guaQiang.getAnnotationId(), "WALLHANG",conduitlistIds,cableSegmentlistIds,
                                                   new RequestResultListener() {
                                                       @Override
                                                       public void onSuccess(JSONObject s) {
                                                           Log.i(TAG,"WALLHANG delete success="+s);
                                                           if(guaQiang.isSaved()){
                                                               guaQiang.delete();
                                                           }
                                                           deleteMark(MarkName);
                                                       }

                                                       @Override
                                                       public void onError(String s) {
                                                           Log.i(TAG,"WALLHANG delete erroe="+s);
                                                       }
                                                   });




                                       }else if(string.indexOf("jietouhe")!=-1){
                                           string=string.replace("jietouhe","");
                                           final JieTouHe jieTouHe=DataSupport.where("jthname=? and projectId=?",string,project.getProjectId())
                                                   .find(JieTouHe.class).get(0);
                                           MarkName=string;
                                           dataCollection(string);

                                           projectModel.volleyAnnotationDelete(jieTouHe.getAnnotationId(), "JOINTBOX",conduitlistIds,cableSegmentlistIds,
                                                   new RequestResultListener() {
                                                       @Override
                                                       public void onSuccess(JSONObject s) {
                                                           Log.i(TAG,"JOINTBOX delete success="+s);
                                                           if(jieTouHe.isSaved()){
                                                               jieTouHe.delete();
                                                           }
                                                           deleteMark(MarkName);
                                                       }

                                                       @Override
                                                       public void onError(String s) {
                                                           Log.i(TAG,"JOINTBOX delete error="+s);
                                                       }
                                                   }
                                           );



                                       }
                                   }
                               })
                               .setNegativeButton("取消",null)
                               .show();

                       //重新绘制线
                       markLigature();
                   }
               });
               return false;
           }

       };

      // 绑定 Marker 被点击事件
       aMap.setOnMarkerClickListener(markerClickListener);
       //TODO 标记连线之后线的点击事件（管道）
       AMap.OnPolylineClickListener polylineClickListener=new AMap.OnPolylineClickListener() {
           @Override
           public void onPolylineClick(Polyline polyline) {
               //polyline.remove();
           }
       };
       aMap.setOnPolylineClickListener(polylineClickListener);
   }

   private void dataCollection(String string){
       conduitlistIds.removeAll(conduitlistIds);
       cableSegmentlistIds.removeAll(cableSegmentlistIds);

       List<Pipeline> pipelineList=DataSupport.where("orgId=? and projectId=?"
               ,project.getOrgId(),project.getProjectId())
               .find(Pipeline.class);
       if(pipelineList.size()>0){
           for(int i=0;i<pipelineList.size();i++){
               Pipeline pipeline=pipelineList.get(i);
               if(pipeline.getStartnodename().equals(string) || pipeline.getEndnodename().equals(string)){
                   conduitlistIds.add(pipeline.getConduitId());
               }
           }
       }


       List<CableResource> cableResourceList=DataSupport.where("orgId=? and projectId=?",
               project.getOrgId(),project.getProjectId()).find(CableResource.class);

       if(cableResourceList.size()>0){
           for(int i=0;i<cableResourceList.size();i++){
               CableResource cableResource=cableResourceList.get(i);
               if(cableResource.getOriginname().equals(string) || cableResource.getEndname().equals(string)){
                   cableSegmentlistIds.add(cableResource.getCableSegmentId()) ;
               }
           }
       }

   }
    private void deleteMark(String string){
       //删除PointSet
       PointSet pointSet=DataSupport.where("markname=? and projrctId=?"
               ,string,project.getProjectId()).find(PointSet.class).get(0);
       if(pointSet.isSaved()){
           pointSet.delete();
       }

       //删除管道表
       List<Pipeline> pipelineList=DataSupport.where("orgId=? and projectId=?"
               ,project.getOrgId(),project.getProjectId())
               .find(Pipeline.class);
       if(pipelineList.size()>0){
           for(int i=0;i<pipelineList.size();i++){
               Pipeline pipeline=pipelineList.get(i);
               if(pipeline.getStartnodename().equals(string) || pipeline.getEndnodename().equals(string)){
                   if(pipeline.isSaved()){
                       pipeline.delete();
                   }

               }
           }
       }

       //删除资源表
        List<CableResource> cableResourceList=DataSupport.where("orgId=? and projectId=?",
                project.getOrgId(),project.getProjectId()).find(CableResource.class);
       if(cableResourceList.size()>0){
           for(int i=0;i<cableResourceList.size();i++){
               CableResource cableResource=cableResourceList.get(i);
               if(cableResource.getOriginname().equals(string) || cableResource.getEndname().equals(string)){
                   if(cableResource.isSaved()){
                       cableResource.delete();
                   }
               }
           }
       }
   }
    View infoWindow=null;


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        destroyLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //对正在移动地图事件回调
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }
    //对移动地图结束事件回调
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }
    private String names;
    //对单击地图事件回调
    @Override
    public void onMapClick(LatLng latLng) {
        //TODO 点击地图其他对方的时候隐藏标记的 window
        if(curShowWindowMarker!=null){
           /* String name=curShowWindowMarker.getTitle();
            if(name.indexOf("电杆")!=-1){
                EditText dgname=(EditText)findViewById(R.id.diangan_name_window);
                names=name.replace("电杆","");
                Pole pole=DataSupport.where("polename=?",names).find(Pole.class).get(0);
                String s=dgname.getText().toString().trim();
                pole.setPolename(s);
                pole.save();
            }*/
            curShowWindowMarker.hideInfoWindow();
        }
    }
    //对长按地图事件回调
    @Override
    public void onMapLongClick(LatLng latLng) {


    }
    //对触摸地图事件回调
    @Override
    public void onTouch(MotionEvent motionEvent) {

    }

    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    //坐标点
    private LatLng latLngs;
    //点击设置标记弹出选择标记
    private void showCreateUserDialog(View view,LatLng latLng){
        latLngs=latLng;
       createMarkDialog=new CreateMarkDialog(this,R.style.loading_dialog,onClickListener);
       createMarkDialog.show();
    }
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ren_shou_jing:
                    createMarkDialog.dismiss();
                    RenShouJing(v,latLngs);
                    break;
                case R.id.dian_gan:
                    createMarkDialog.dismiss();
                    DianGan(latLngs);
                    break;
                case R.id.guang_jiao_xiang:
                    createMarkDialog.dismiss();
                    GuangJiaoXiang(latLngs);
                    break;
                case R.id.yin_shang_dian:
                    createMarkDialog.dismiss();
                    YinShangQiang(latLngs);
                    break;
                case R.id.gua_qiang:
                    createMarkDialog.dismiss();
                    Guaqiang(latLngs);
                    break;
                case R.id.qi_ta:
                    createMarkDialog.dismiss();
                    JieTouHe(latLngs);
                    break;
            }
        }
    };

    RenShouJingDialog renShouJingDialog;
    private void RenShouJing( View view,LatLng latLng){
        nodeCacheList.removeAll(nodeCacheList);
        cableCacheList.removeAll(cableCacheList);
        cableCaches.removeAll(cableCaches);
        StringBuffer sb = new StringBuffer();
        sb.append(latLng.longitude);
        String jingdu = sb.toString();

        StringBuffer zz=new StringBuffer();
        zz.append(latLng.latitude);
        String weidu = zz.toString();
        renShouJingDialog=new RenShouJingDialog(this,R.style.loading_dialog,rsjOnClickListener,GJJXonItemSelectedListener,
                jingdu,weidu);
        renShouJingDialog.show();
    }

    private String rsjsize,rsjxj;
    private int countrsj=0;
    AdapterView.OnItemSelectedListener GJJXonItemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            rsjsize=renShouJingDialog.rsjSize.getSelectedItem().toString().trim();
           /* rsjxj=renShouJingDialog.rsjxj.getSelectedItem().toString().trim();*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener rsjOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_node_rsj:
                    AddNodeDialogShow(latLngs,"rsj",project.getProjectId());
                    break;
                case R.id.add_cable_rsj:
                    SelectCableDialogShow(nodeCacheList,"rsj");
                    break;
                case R.id.send_mark:
                    renShouJingDialog.dismiss();
                    if(renShouJingDialog.rsjName.getText().toString().trim().length()<1){
                        Toast.makeText(getBaseContext(),"人手井名称不能为空!",Toast.LENGTH_SHORT).show();
                    }else {
                        if(renShouJingDialog.rsjName.getText().toString().trim().contains("#")){
                            //TODO 还有判断条件比如 命名唯一等（后续添加）
                            MarkerOptions markerOptions=new MarkerOptions();
                            markerOptions.position(latLngs);
                            markerOptions.title("人手井"+renShouJingDialog.rsjName.getText().toString().trim());
                            markerOptions.draggable(true);
                            markerOptions.visible(true);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), rsj_mark));
                            markerOptions.icon(bitmapDescriptor);
                            aMap.addMarker(markerOptions);


                            //保存井的信息
                            final PeopleWell peopleWell=new PeopleWell();
                            peopleWell.setCategory(1);
                            peopleWell.setLatLng(latLngs);
                            peopleWell.setLatitude(Double.toString(latLngs.latitude));
                            peopleWell.setLongitude(Double.toString(latLngs.longitude));
                            peopleWell.setWellname(renShouJingDialog.rsjName.getText().toString().trim());
                            peopleWell.setTotheproject(proname);
                            peopleWell.setSpecifications(rsjsize);

                            peopleWell.setAddtime(String.valueOf(System.currentTimeMillis()));
                            peopleWell.setProjectId(project.getProjectId());
                            peopleWell.setOrgId(project.getOrgId());
                            peopleWell.save();

                            //volley-1 保存到POintSet表（所有标记表）
                            CreateMark(latLngs,proname,System.currentTimeMillis(),renShouJingDialog.rsjName.getText().toString().trim()
                                    ,"WELL",project.getProjectId(),project.getOrgId());

                            //结束节点（当前节点）
                            final PeopleWell peopleWellVolley=DataSupport.where("wellname=? and projectId=? and orgId=?"
                                    ,renShouJingDialog.rsjName.getText().toString().trim(),project.getProjectId(),project.getOrgId())
                                    .find(PeopleWell.class).get(0);
                           List<Map<String ,String>> conduit=new ArrayList<>();

                            //保存上一节点的信息
                            List<String> stringList=renShouJingDialog.rsjList;
                            for(int i=0;i<nodeCacheList.size();i++){
                                NodeCache nodeCache=nodeCacheList.get(i);
                                if(stringList.contains(nodeCache.getStartnodename())){

                                    //根据起点的名字在标记表获取信息
                                    PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                            nodeCache.getStartnodename(),project.getProjectId(),project.getOrgId())
                                            .find(PointSet.class).get(0);//开始节点（上一节点）

                                    Pipeline pipeline=new Pipeline();
                                    pipeline.setStartnodename(nodeCache.getStartnodename());
                                    pipeline.setEndnodename(renShouJingDialog.rsjName.getText().toString().trim());
                                    pipeline.setPipelinehole(nodeCache.getPipelinehole());
                                    pipeline.setPipelinename(nodeCache.getPipelinename());
                                    pipeline.setPipelinetype(nodeCache.getPipelinetype());
                                    pipeline.setPipelinespecifica(nodeCache.getPipelinespecifica());

                                    pipeline.setProjectname(proname);//管道 保存项目的名程（初始化连线）
                                    pipeline.setProjectId(project.getProjectId());
                                    pipeline.setOrgId(project.getOrgId());
                                    pipeline.setAddtime(peopleWellVolley.getAddtime());
                                    pipeline.setStartPointType(pointSet.getPointType());
                                    pipeline.setStartLatitude(pointSet.getLatitude());
                                    pipeline.setStartLongitude(pointSet.getLongitude());
                                    pipeline.setEndPointType("WELL");
                                    pipeline.setEndLatitude(peopleWellVolley.getLatitude());
                                    pipeline.setEndLongitude(peopleWellVolley.getLongitude());
                                    pipeline.save();


                                    Map<String,String> stringMap=new HashMap<>();
                                    stringMap.put("name",nodeCache.getPipelinename());//管道名
                                    stringMap.put("stand",nodeCache.getPipelinespecifica());//管道规格
                                    stringMap.put("type",nodeCache.getPipelinetype());//管道类型
                                    stringMap.put("length","20");//管道长度
                                    stringMap.put("holeNumber",nodeCache.getPipelinehole());//管道孔数
                                    stringMap.put("startPointName",nodeCache.getStartnodename());
                                    stringMap.put("startPointType",pointSet.getPointType());
                                    stringMap.put("startLatitude",pointSet.getLatitude());
                                    stringMap.put("startLongitude",pointSet.getLongitude());
                                    stringMap.put("startPointId",pointSet.getMarkId());
                                    stringMap.put("endPointName",peopleWellVolley.getWellname());
                                    stringMap.put("endPointType","WELL");
                                    stringMap.put("endLatitude",peopleWellVolley.getLatitude());
                                    stringMap.put("endLongitude",peopleWellVolley.getLongitude());
                                    stringMap.put("addTime",peopleWellVolley.getAddtime());
                                    stringMap.put("projectId",project.getProjectId());
                                    stringMap.put("orgId",project.getOrgId());

                                    conduit.add(stringMap);

                                }
                            }

                            List<Map<String ,String>> cableSegment=new ArrayList<>();

                            //保存光缆资源
                            List<CableCache> SaveCable=renShouJingDialog.cableCacheList;
                            if(renShouJingDialog.rsjList.size()>0){
                                if(SaveCable.size()>0){
                                    for(int i=0;i<SaveCable.size();i++){
                                        CableCache cableCache=SaveCable.get(i);

                                        //根据起点的名字获取管道的名字在管道表中
                                        Pipeline pipeline=DataSupport.where("projectId=? and startnodename=? and endnodename=?",
                                                project.getProjectId(),cableCache.getCableorigin(),renShouJingDialog.rsjName.getText().toString().trim())
                                                .find(Pipeline.class).get(0);

                                        //根据起点的名字在标记表获取信息
                                        PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                                cableCache.getCableorigin(),project.getProjectId(),project.getOrgId())
                                                .find(PointSet.class).get(0);//开始节点（上一节点）



                                        PeopleWell peopleWellEnd=DataSupport.where("wellname=? and projectId=?"
                                                ,renShouJingDialog.rsjName.getText().toString().trim(),project.getProjectId())
                                                .find(PeopleWell.class).get(0);//结束节点（当前节点）

                                        CableResource cableResource=new CableResource();
                                        cableResource.setCablename(cableCache.getCablename());
                                        cableResource.setOriginname(cableCache.getCableorigin());
                                        cableResource.setEndname(renShouJingDialog.rsjName.getText().toString().trim());
                                        cableResource.setHole(cableCache.getHole());
                                        cableResource.setChildhole(cableCache.getChildhole());

                                        cableResource.setProjectId(project.getProjectId());
                                        cableResource.setOrgId(project.getOrgId());
                                        cableResource.setConduitName(pipeline.getPipelinename());
                                        cableResource.setStartPointName(cableCache.getCableorigin());
                                        cableResource.setStartPointType(pointSet.getPointType());
                                        cableResource.setStartLatitude(pointSet.getLatitude());
                                        cableResource.setStartLongitude(pointSet.getLongitude());
                                        cableResource.setEndPointName(peopleWellEnd.getWellname());
                                        cableResource.setEndPointType("WELL");
                                        cableResource.setEndLatitude(peopleWellEnd.getLatitude());
                                        cableResource.setEndLongitude(peopleWellEnd.getLongitude());
                                        cableResource.setAddTime(peopleWellEnd.getLongitude());
                                        cableResource.save();

                                        Map<String,String> map=new HashMap<>();
                                        map.put("cableName",cableCache.getCablename());
                                        map.put("conduitName",pipeline.getPipelinename());
                                        map.put("useHole",cableCache.getHole());
                                        map.put("useSubHole",cableCache.getChildhole());
                                        map.put("length","20");
                                        map.put("startPointName",cableCache.getCableorigin());
                                        map.put("startPointType",pointSet.getPointType());
                                        map.put("startLatitude",pointSet.getLatitude());
                                        map.put("startLongitude",pointSet.getLongitude());
                                        map.put("startPointId",pointSet.getMarkId());
                                        map.put("endPointName",peopleWellEnd.getWellname());
                                        map.put("endPointType","WELL");
                                        map.put("endLatitude",peopleWellEnd.getLatitude());
                                        map.put("endLongitude",peopleWellEnd.getLongitude());
                                        map.put("addTime",peopleWellEnd.getAddtime());
                                        map.put("projectId",project.getProjectId());
                                        map.put("orgId",project.getOrgId());
                                        cableSegment.add(map);
                                    }
                                }
                            }

                            projectModel.volleyAddMarks("WELL", peopleWellVolley.getWellname(), peopleWellVolley.getSpecifications(),
                                    peopleWellVolley.getLatitude(), peopleWellVolley.getLongitude(), project.getProjectId(),
                                    proname, peopleWellVolley.getAddtime(), "",
                                    "", "",selectorganizationId,conduit, cableSegment,
                                    new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            Log.i(TAG,"addmarks message="+s);
                                            peopleWellVolley.setAnnotationId(s.getString("annotationId"));
                                            peopleWellVolley.save();

                                            UpdateDataUtil.updateConduitAndCableSegment(s);
                                        }

                                        @Override
                                        public void onError(String s) {
                                            Log.i(TAG,"addmarks error message="+s);
                                        }
                                    });


                            nodeCacheList.removeAll(nodeCacheList);
                            cableCacheList.removeAll(cableCacheList);
                            conduit.removeAll(conduit);
                            cableSegment.removeAll(cableSegment);

                            nodePolyline(renShouJingDialog.rsjName.getText().toString().trim());

                        }else {
                           toastMarkt("命名不规范");
                        }
                    }

                    break;
                case R.id.cancel_mark:
                    renShouJingDialog.dismiss();
                    break;
            }
        }
    };
    SelectCableDialog selectCableDialog;
    private String types;
    private void SelectCableDialogShow(List<NodeCache> nodeCacheList,String WellType){
        types=WellType;
        if(types.equals("rsj")){
            if(renShouJingDialog.rsjList.size()<=0){
                toastMarkt("上一节点未选择，无法选择光缆,请先选择上一节点!");
            }else {
                selectCableDialog=new SelectCableDialog(this,R.style.loading_dialog,CableOnClickListener,nodeCacheList, renShouJingDialog.cableCacheList);
                selectCableDialog.show();
            }
        }else if(types.equals("diangan")){

            if(dianGanDialog.dinaganList.size()<=0){
                toastMarkt("上一节点未选择，无法选择光缆,请先选择上一节点!");
            }else {
                selectCableDialog=new SelectCableDialog(this,R.style.loading_dialog,CableOnClickListener,nodeCacheList, dianGanDialog.cableCacheList);
                selectCableDialog.show();
            }
        }else if (types.equals("gjjx")){
            if(guangJiaoXiangDialog.gjjxList.size()<=0){
                toastMarkt("上一节点未选择，无法选择光缆,请先选择上一节点!");

            }else {
                selectCableDialog=new SelectCableDialog(this,R.style.loading_dialog,CableOnClickListener,nodeCacheList, guangJiaoXiangDialog.cableCacheList);
                selectCableDialog.show();
            }

        }else if(types.equals("ysd")){
            if(yinShangDianDialog.ysdxList.size()<=0){
                toastMarkt("上一节点未选择，无法选择光缆,请先选择上一节点!");
            }else {
                selectCableDialog=new SelectCableDialog(this,R.style.loading_dialog,CableOnClickListener,nodeCacheList, yinShangDianDialog.cableCacheList);
                selectCableDialog.show();
            }
        }else if(types.equals("guaqiang")){
            if(guaQiangDialog.gqxList.size()<=0){
                toastMarkt("上一节点未选择，无法选择光缆,请先选择上一节点!");
            }else {
                selectCableDialog=new SelectCableDialog(this,R.style.loading_dialog,CableOnClickListener,nodeCacheList, guaQiangDialog.cableCacheList);
                selectCableDialog.show();
            }
        }else if(types.equals("jth")){
            if(jieTouHeDialog.jthList.size()<=0){
                toastMarkt("上一节点未选择，无法选择光缆,请先选择上一节点!");
            }else {
                selectCableDialog=new SelectCableDialog(this,R.style.loading_dialog,CableOnClickListener,nodeCacheList, jieTouHeDialog.cableCacheList);
                selectCableDialog.show();
            }
        }else if(types.equals(("WellWindow"))){
            if(nodelist.size()<=0){
                toastMarkt("上一节点未选择，无法选择光缆,请先选择上一节点!");
            }else {
                selectCableDialog=new SelectCableDialog(this,R.style.loading_dialog,CableOnClickListener,nodeCacheList, cableCacheList);
                selectCableDialog.show();
            }
        }

    }

    View.OnClickListener CableOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.send_cable:
                    selectCableDialog.dismiss();
                    String cablename=selectCableDialog.search.getText().toString().trim();//光缆名

                    List<OpticalCable> opticalCableList= DataSupport.findAll(OpticalCable.class);
                    if(opticalCableList.size()>0){
                        List<String> strings=new ArrayList<>();
                        for(int i=0;i<opticalCableList.size();i++){
                            strings.add(opticalCableList.get(i).getOpticalcablename());
                        }
                        if(strings.contains(cablename)){
                            if(cablename!=null && cablename.length()>0){
                                if(selectCableDialog.childholename!=null) {

                                    CableCache cableCache = new CableCache();
                                    cableCache.setCablename(cablename);
                                    cableCache.setChildhole(selectCableDialog.childholename);//子孔
                                    cableCache.setCableorigin(nodeCacheList.get(selectCableDialog.cableoriginname).getStartnodename());
                                    cableCache.setHole(String.valueOf(selectCableDialog.holename));  //管孔
                                    cableCacheList.add(cableCache);
                                    addCableSegmentArray.add(cableCache);

                                    if (types.equals("rsj")) {
                                        renShouJingDialog.cableCacheList.add(cableCache);
                                        renShouJingDialog.cablersjList.add(cablename);
                                        renShouJingDialog.cablelistView.invalidateViews();
                                    } else if (types.equals("diangan")) {
                                        dianGanDialog.cableCacheList.add(cableCache);
                                        dianGanDialog.cabledianganList.add(cablename);
                                        dianGanDialog.cablelistView.invalidateViews();
                                    }else if(types.equals("gjjx")){
                                        guangJiaoXiangDialog.cableCacheList.add(cableCache);
                                        guangJiaoXiangDialog.cablegjjxList.add(cablename);
                                        guangJiaoXiangDialog.cablelistView.invalidateViews();
                                    }else  if (types.equals("rsj")) {
                                        yinShangDianDialog.cableCacheList.add(cableCache);
                                        yinShangDianDialog.cableysdList.add(cablename);
                                        yinShangDianDialog.cablelistView.invalidateViews();
                                    }else if(types.equals("guaqiang")){
                                        guaQiangDialog.cableCacheList.add(cableCache);
                                        guaQiangDialog.cableguaqiangList.add(cablename);
                                        guaQiangDialog.cablelistView.invalidateViews();
                                    }else if(types.equals("jth")){
                                        jieTouHeDialog.cableCacheList.add(cableCache);
                                        jieTouHeDialog.cablejthList.add(cablename);
                                        jieTouHeDialog.cablelistView.invalidateViews();
                                    }else if(types.equals("WellWindow")){
                                        cablelist.add(cablename);
                                        WindowCableListView.invalidateViews();
                                    }
                                }else {
                                    toastMarkt("子孔未选择!");
                                }

                            }else {
                                toastMarkt("未选择光缆!");
                            }
                        }else {
                            toastMarkt("光缆不存在，请创建或选择正确的光缆!");
                        }
                    }else {
                        toastMarkt("光缆不存在，请创建或选择正确的光缆!");
                    }


                    break;
                case R.id.cancel_cable:
                    selectCableDialog.dismiss();
                    break;
            }
        }
    };

    AddNodeDialog addNodeDialog;
    private String wellType;
    private void AddNodeDialogShow(LatLng lng,String WellType,String projectId){
        wellType=WellType;
        addNodeDialog=new AddNodeDialog(this,R.style.loading_dialog,NodeOnClickListener,NodeOnItemSelectedListener,lng,projectId);
        addNodeDialog.show();
    }
    private String guandaoLX,guandaoGG,nodename;
    private String guandaoKS;
    private String guandaoName;
    AdapterView.OnItemSelectedListener NodeOnItemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            guandaoLX=addNodeDialog.guandaoLX.getSelectedItem().toString().trim();
            guandaoGG=addNodeDialog.guandaoGG.getSelectedItem().toString().trim();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    View.OnClickListener NodeOnClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.send_node:
                    addNodeDialog.dismiss();
                    nodename=addNodeDialog.search.getText().toString().trim();
                    guandaoName=addNodeDialog.guandaoName.getText().toString().trim();
                    guandaoKS=addNodeDialog.guandaoKS.getText().toString().trim();

                    List<String> strings = new ArrayList<>();
                    //根据projectId 找出当前项目的所有标记
                    List<PointSet> pointSetList=DataSupport.where("projrctId=?",project.getProjectId()).find(PointSet.class);
                    if(pointSetList.size()>0){
                        for(int i=0;i<pointSetList.size();i++){
                            PointSet pointSet=pointSetList.get(i);
                            strings.add(pointSet.getMarkname());
                        }
                        if(strings.contains(nodename)){

                            List<Pipeline> pipelineList = DataSupport.where("projectId=?",project.getProjectId()).find(Pipeline.class);
                            List<String> pipelinename = new ArrayList<>();
                            if (pipelineList.size() > 0) {
                                for (int a = 0; a < pipelineList.size(); a++) {
                                    Pipeline pipeline1 = pipelineList.get(a);
                                    pipelinename.add(pipeline1.getPipelinename());
                                }
                                if(pipelinename.contains(guandaoName)){

                                    toastMarkt("管道名重复，请输入正确的管道名");
                                }else {
                                    if(wellType.equals("rsj")){
                                        if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0){
                                            if(renShouJingDialog.rsjList.contains(nodename)){
                                                toastMarkt("此节点已添加，请选择正确的节点!");
                                            }else {

                                                renShouJingDialog.rsjList.add(nodename);
                                                renShouJingDialog.listView.invalidateViews();
                                                SaceNodeCache(nodename,guandaoName,guandaoGG,guandaoLX,guandaoKS);
                                            }

                                        }else {
                                            toastMarkt("管道名称或管道孔数不能为空!");
                                        }
                                    }else if(wellType.equals("diangan")){
                                        if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                            if(dianGanDialog.dinaganList.contains(nodename)){
                                                toastMarkt("此节点已添加，请选择正确的节点!");
                                            }else {
                                                dianGanDialog.dinaganList.add(nodename);
                                                dianGanDialog.listView.invalidateViews();
                                                SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                            }

                                        }else {
                                            toastMarkt("管道名称或管道孔数不能为空!");
                                        }
                                    }else if(wellType.equals("gjjx")){
                                        if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                            if(guangJiaoXiangDialog.gjjxList.contains(nodename)){
                                                toastMarkt("此节点已添加，请选择正确的节点!");
                                            }else {
                                                guangJiaoXiangDialog.gjjxList.add(nodename);
                                                guangJiaoXiangDialog.listView.invalidateViews();
                                                SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                            }

                                        }else {
                                            toastMarkt("管道名称或管道孔数不能为空!");
                                        }
                                    }else if(wellType.equals("ysd")){
                                        if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                            if(yinShangDianDialog.ysdxList.contains(nodename)){
                                                toastMarkt("此节点已添加，请选择正确的节点!");
                                            }else {
                                                yinShangDianDialog.ysdxList.add(nodename);
                                                yinShangDianDialog.listView.invalidateViews();
                                                SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                            }

                                        }else {
                                            toastMarkt("管道名称或管道孔数不能为空!");
                                        }
                                    }else if(wellType.equals("guaqiang")){
                                        if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                            if(guaQiangDialog.gqxList.contains(nodename)){
                                                toastMarkt("此节点已添加，请选择正确的节点!");
                                            }else {
                                                guaQiangDialog.gqxList.add(nodename);
                                                guaQiangDialog.listView.invalidateViews();
                                                SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                            }

                                        }else {
                                            toastMarkt("管道名称或管道孔数不能为空!");
                                        }
                                    }else if(wellType.equals("jth")){
                                        if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                            if(jieTouHeDialog.jthList.contains(nodename)){
                                                toastMarkt("此节点已添加，请选择正确的节点!");
                                            }else {
                                                jieTouHeDialog.jthList.add(nodename);
                                                jieTouHeDialog.listView.invalidateViews();
                                                SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                            }
                                        }else {
                                            toastMarkt("管道名称或管道孔数不能为空!");
                                        }
                                    }else if(wellType.equals("WellWindow")){
                                        if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                            if(nodelist.contains(nodename)){
                                                toastMarkt("此节点已添加，请选择正确的节点!");
                                            }else {
                                                nodelist.add(nodename);
                                                WindowListView.invalidateViews();
                                                SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                            }
                                        }else {
                                            toastMarkt("管道名称或管道孔数不能为空!");
                                        }
                                    }
                                }
                                pipelinename.removeAll(pipelinename);
                            }else {
                                if(wellType.equals("rsj")){
                                    if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0){
                                        if(renShouJingDialog.rsjList.contains(nodename)){
                                            toastMarkt("此节点已添加，请选择正确的节点!");
                                        }else {
                                            renShouJingDialog.rsjList.add(nodename);
                                            renShouJingDialog.listView.invalidateViews();
                                            SaceNodeCache(nodename,guandaoName,guandaoGG,guandaoLX,guandaoKS);
                                        }

                                    }else {
                                        toastMarkt("管道名称或管道孔数不能为空!");
                                    }
                                }else if(wellType.equals("diangan")){
                                    if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                        if(dianGanDialog.dinaganList.contains(nodename)){
                                            toastMarkt("此节点已添加，请选择正确的节点!");
                                        }else {
                                            dianGanDialog.dinaganList.add(nodename);
                                            dianGanDialog.listView.invalidateViews();
                                            SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                        }

                                    }else {
                                        toastMarkt("管道名称或管道孔数不能为空!");
                                    }
                                }else if(wellType.equals("gjjx")){
                                    if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                        if(guangJiaoXiangDialog.gjjxList.contains(nodename)){
                                            toastMarkt("此节点已添加，请选择正确的节点!");
                                        }else {
                                            guangJiaoXiangDialog.gjjxList.add(nodename);
                                            guangJiaoXiangDialog.listView.invalidateViews();
                                            SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                        }

                                    }else {
                                        toastMarkt("管道名称或管道孔数不能为空!");
                                    }
                                }else if(wellType.equals("ysd")){
                                    if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                        if(yinShangDianDialog.ysdxList.contains(nodename)){
                                            toastMarkt("此节点已添加，请选择正确的节点!");
                                        }else {
                                            yinShangDianDialog.ysdxList.add(nodename);
                                            yinShangDianDialog.listView.invalidateViews();
                                            SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                        }

                                    }else {
                                        toastMarkt("管道名称或管道孔数不能为空!");
                                    }
                                }else if(wellType.equals("guaqiang")){
                                    if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                        if(guaQiangDialog.gqxList.contains(nodename)){
                                            toastMarkt("此节点已添加，请选择正确的节点!");
                                        }else {
                                            guaQiangDialog.gqxList.add(nodename);
                                            guaQiangDialog.listView.invalidateViews();
                                            SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                        }

                                    }else {
                                        toastMarkt("管道名称或管道孔数不能为空!");
                                    }
                                }else if(wellType.equals("jth")){
                                    if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                        if(jieTouHeDialog.jthList.contains(nodename)){
                                            toastMarkt("此节点已添加，请选择正确的节点!");
                                        }else {
                                            jieTouHeDialog.jthList.add(nodename);
                                            jieTouHeDialog.listView.invalidateViews();
                                            SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                        }
                                    }else {
                                        toastMarkt("管道名称或管道孔数不能为空!");
                                    }
                                }else if(wellType.equals("WellWindow")){
                                    if(guandaoName!=null && guandaoName.length()>0 && guandaoKS!=null && guandaoKS.length()>0) {
                                        if(nodelist.contains(nodename)){
                                            toastMarkt("此节点已添加，请选择正确的节点!");
                                        }else {
                                            nodelist.add(nodename);
                                            WindowListView.invalidateViews();
                                            SaceNodeCache(nodename, guandaoName, guandaoGG, guandaoLX, guandaoKS);
                                        }
                                    }else {
                                        toastMarkt("管道名称或管道孔数不能为空!");
                                    }
                                }
                            }


                        }else {
                            toastMarkt("未选择节点或节点不存在,请选择正确的节点!");
                        }

                    }else {
                        toastMarkt("未选择节点或节点不存在,请选择正确的节点!");
                    }


                    break;
                case R.id.cancel_node:
                    addNodeDialog.dismiss();
                    break;

            }
        }
    };
    private void SaceNodeCache(String nodename,String guandaoName,
                               String guandaoGG,String guandaoLX,String guandaoKS){
        NodeCache nodeCache=new NodeCache();
        nodeCache.setStartnodename(nodename);
        nodeCache.setPipelinename(guandaoName);
        nodeCache.setPipelinespecifica(guandaoGG);
        nodeCache.setPipelinetype(guandaoLX);
        nodeCache.setPipelinehole(guandaoKS);
        nodeCacheList.add(nodeCache);

        addConduitArray.add(nodeCache);//新添加的
    }

    DianGanDialog dianGanDialog;
    private void DianGan(LatLng latLng){
        nodeCacheList.removeAll(nodeCacheList);
        cableCacheList.removeAll(cableCacheList);
        cableCaches.removeAll(cableCaches);

        StringBuffer sb = new StringBuffer();
        sb.append(latLng.longitude);
        String jingdu = sb.toString();

        StringBuffer zz=new StringBuffer();
        zz.append(latLng.latitude);
        String weidu = zz.toString();

        dianGanDialog=new DianGanDialog(this,R.style.loading_dialog,dianganOnClickListener,dgonItemSelectedListener,jingdu,weidu);
        dianGanDialog.show();
    }
    private String dgheight;
    private String fencha;
    AdapterView.OnItemSelectedListener dgonItemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            dgheight=dianGanDialog.dianganheight.getSelectedItem().toString().trim();
            fencha=dianGanDialog.fencha.getSelectedItem().toString().trim();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener dianganOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_node_diangan:
                    AddNodeDialogShow(latLngs,"diangan",project.getProjectId());
                    break;
                case R.id.add_cable_diangan:
                    SelectCableDialogShow(nodeCacheList,"diangan");
                    break;
                case R.id.diangan_send_mark:
                    dianGanDialog.dismiss();
                    if(dianGanDialog.dianganName.getText().toString().trim().length()<1){
                        Toast.makeText(getBaseContext(),"电杆名不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        if(dianGanDialog.dianganName.getText().toString().trim().contains("P")){

                            MarkerOptions markerOptions=new MarkerOptions();
                            markerOptions.position(latLngs);
                            markerOptions.title("电杆"+dianGanDialog.dianganName.getText().toString().trim());
                            markerOptions.draggable(true);
                            markerOptions.visible(true);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.diangan_mark));
                            markerOptions.icon(bitmapDescriptor);
                            aMap.addMarker(markerOptions);

                            Pole pole=new Pole();
                            pole.setLatitude(Double.toString(latLngs.latitude));
                            pole.setLongitude(Double.toString(latLngs.longitude));
                            pole.setPolename(dianGanDialog.dianganName.getText().toString().trim());
                            pole.setTotheproject(proname);
                            pole.setPoleheight(dgheight);
                            pole.setFencha(fencha);

                            pole.setAddtime(String.valueOf(System.currentTimeMillis()));
                            pole.setProjectId(project.getProjectId());
                            pole.setOrgId(project.getOrgId());
                            pole.save();

                            //volley-1 保存到POintSet表（所有标记表）
                            CreateMark(latLngs,proname,System.currentTimeMillis(),dianGanDialog.dianganName.getText().toString().trim()
                                    ,"POLE",project.getProjectId(),project.getOrgId());

                            //结束节点（当前节点）
                            final Pole poleVolley=DataSupport.where("polename=? and projectId=? and orgId=?"
                                    ,dianGanDialog.dianganName.getText().toString(),project.getProjectId(),project.getOrgId())
                                    .find(Pole.class).get(0);
                            List<Map<String ,String>> conduit=new ArrayList<>();

                            List<String> stringList=dianGanDialog.dinaganList;
                            for(int i=0;i<nodeCacheList.size();i++){
                                NodeCache nodeCache=nodeCacheList.get(i);
                                if(stringList.contains(nodeCache.getStartnodename())){

                                    //根据起点的名字在标记表获取信息
                                    PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                            nodeCache.getStartnodename(),project.getProjectId(),project.getOrgId())
                                            .find(PointSet.class).get(0);//开始节点（上一节点）

                                    Pipeline pipeline=new Pipeline();
                                    pipeline.setStartnodename(nodeCache.getStartnodename());
                                    pipeline.setEndnodename(dianGanDialog.dianganName.getText().toString().trim());
                                    pipeline.setPipelinehole(nodeCache.getPipelinehole());
                                    pipeline.setPipelinename(nodeCache.getPipelinename());
                                    pipeline.setPipelinetype(nodeCache.getPipelinetype());
                                    pipeline.setPipelinespecifica(nodeCache.getPipelinespecifica());

                                    pipeline.setProjectname(proname);//管道 保存项目的名程（初始化连线）
                                    pipeline.setProjectId(project.getProjectId());
                                    pipeline.setOrgId(project.getOrgId());
                                    pipeline.setStartPointType(pointSet.getPointType());
                                    pipeline.setStartLatitude(pointSet.getLatitude());
                                    pipeline.setStartLongitude(pointSet.getLongitude());
                                    pipeline.setAddtime(poleVolley.getAddtime());
                                    pipeline.setEndPointType("POLE");
                                    pipeline.setEndLatitude(poleVolley.getLatitude());
                                    pipeline.setEndLongitude(poleVolley.getLongitude());
                                    pipeline.save();

                                    Map<String,String> stringMap=new HashMap<>();
                                    stringMap.put("name",nodeCache.getPipelinename());//管道名
                                    stringMap.put("stand",nodeCache.getPipelinespecifica());//管道规格
                                    stringMap.put("type",nodeCache.getPipelinetype());//管道类型
                                    stringMap.put("length","20");//管道长度
                                    stringMap.put("holeNumber",nodeCache.getPipelinehole());//管道孔数
                                    stringMap.put("startPointName",nodeCache.getStartnodename());
                                    stringMap.put("startPointType",pointSet.getPointType());
                                    stringMap.put("startLatitude",pointSet.getLatitude());
                                    stringMap.put("startLongitude",pointSet.getLongitude());
                                    stringMap.put("startPointId",pointSet.getMarkId());
                                    stringMap.put("endPointName",poleVolley.getPolename());
                                    stringMap.put("endPointType","POLE");
                                    stringMap.put("endLatitude",poleVolley.getLatitude());
                                    stringMap.put("endLongitude",poleVolley.getLongitude());
                                    stringMap.put("addTime",poleVolley.getAddtime());
                                    stringMap.put("projectId",project.getProjectId());
                                    stringMap.put("orgId",project.getOrgId());
                                    conduit.add(stringMap);
                                }
                            }

                            List<Map<String ,String>> cableSegment=new ArrayList<>();

                            //保存光缆资源
                            List<CableCache> SaveCable=dianGanDialog.cableCacheList;
                            if(dianGanDialog.dinaganList.size()>0){
                                if(SaveCable.size()>0){
                                    for(int i=0;i<SaveCable.size();i++){
                                        CableCache cableCache=SaveCable.get(i);

                                        //根据起点的名字获取管道的名字在管道表中
                                        Pipeline pipeline=DataSupport.where("projectId=? and startnodename=? and endnodename=?",
                                                project.getProjectId(),cableCache.getCableorigin(),dianGanDialog.dianganName.getText().toString().trim())
                                                .find(Pipeline.class).get(0);

                                        //根据起点的名字在标记表获取信息
                                        PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                                cableCache.getCableorigin(),project.getProjectId(),project.getOrgId())
                                                .find(PointSet.class).get(0);//开始节点（上一节点）


                                        Pole poleEnd=DataSupport.where("polename=? and projectId=?"
                                                ,dianGanDialog.dianganName.getText().toString().trim(),project.getProjectId())
                                                .find(Pole.class).get(0);//结束节点（当前节点）

                                        CableResource cableResource=new CableResource();
                                        cableResource.setCablename(cableCache.getCablename());
                                        cableResource.setOriginname(cableCache.getCableorigin());
                                        cableResource.setEndname(dianGanDialog.dianganName.getText().toString().trim());
                                        cableResource.setHole(cableCache.getHole());
                                        cableResource.setChildhole(cableCache.getChildhole());

                                        cableResource.setProjectId(project.getProjectId());
                                        cableResource.setOrgId(project.getOrgId());
                                        cableResource.setConduitName(pipeline.getPipelinename());
                                        cableResource.setStartPointName(cableCache.getCableorigin());
                                        cableResource.setStartPointType(pointSet.getPointType());
                                        cableResource.setStartLatitude(pointSet.getLatitude());
                                        cableResource.setStartLongitude(pointSet.getLongitude());
                                        cableResource.setEndPointName(poleEnd.getPolename());
                                        cableResource.setEndPointType("POLE");
                                        cableResource.setEndLatitude(poleEnd.getLatitude());
                                        cableResource.setEndLongitude(poleEnd.getLongitude());
                                        cableResource.setAddTime(poleEnd.getLongitude());
                                        cableResource.save();

                                        Map<String,String> map=new HashMap<>();
                                        map.put("cableName",cableCache.getCablename());
                                        map.put("conduitName",pipeline.getPipelinename());
                                        map.put("useHole",cableCache.getHole());
                                        map.put("useSubHole",cableCache.getChildhole());
                                        map.put("length","20");
                                        map.put("startPointName",cableCache.getCableorigin());
                                        map.put("startPointType",pointSet.getPointType());
                                        map.put("startLatitude",pointSet.getLatitude());
                                        map.put("startLongitude",pointSet.getLongitude());
                                        map.put("startPointId",pointSet.getMarkId());
                                        map.put("endPointName",poleEnd.getPolename());
                                        map.put("endPointType","POLE");
                                        map.put("endLatitude",poleEnd.getLatitude());
                                        map.put("endLongitude",poleEnd.getLongitude());
                                        map.put("addTime",poleEnd.getAddtime());
                                        map.put("projectId",project.getProjectId());
                                        map.put("orgId",project.getOrgId());
                                        cableSegment.add(map);

                                    }
                                }
                            }


                            projectModel.volleyAddMarks("POLE", poleVolley.getPolename(), "", poleVolley.getLatitude(),
                                    poleVolley.getLongitude(), poleVolley.getProjectId(), proname,
                                    poleVolley.getAddtime(), poleVolley.getPoleheight(), poleVolley.getFencha(), "", poleVolley.getOrgId(),
                                    conduit,cableSegment, new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            Log.i(TAG,"POLE message"+s);
                                            poleVolley.setAnnotationId(s.getString("annotationId"));
                                            poleVolley.save();
                                            UpdateDataUtil.updateConduitAndCableSegment(s);
                                        }

                                        @Override
                                        public void onError(String s) {
                                            Log.i(TAG,"POLE error message"+s);
                                        }
                                    });



                            nodeCacheList.removeAll(nodeCacheList);
                            cableCacheList.removeAll(cableCacheList);
                            conduit.removeAll(conduit);
                            cableSegment.removeAll(cableSegment);

                            nodePolyline(dianGanDialog.dianganName.getText().toString().trim());
                        }else {
                            Toast.makeText(getBaseContext(),"命名不规范",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.diangan_cancel_mark:
                    dianGanDialog.dismiss();
                    break;

            }
        }
    };

    GuangJiaoXiangDialog guangJiaoXiangDialog;
    private void GuangJiaoXiang(LatLng latLng){
        nodeCacheList.removeAll(nodeCacheList);
        cableCacheList.removeAll(cableCacheList);
        cableCaches.removeAll(cableCaches);
        StringBuffer sb = new StringBuffer();
        sb.append(latLng.longitude);
        String jingdu = sb.toString();

        StringBuffer zz=new StringBuffer();
        zz.append(latLng.latitude);
        String weidu = zz.toString();
        guangJiaoXiangDialog=new GuangJiaoXiangDialog(this,R.style.loading_dialog,gjjxOnClickListener,onItemSelectedListener,jingdu,weidu);
        guangJiaoXiangDialog.show();

    }
    private String gjjxjibie;
    private String gjjxleixing;
    AdapterView.OnItemSelectedListener onItemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            gjjxjibie=guangJiaoXiangDialog.gjjxjibie.getSelectedItem().toString().trim();
            gjjxleixing=guangJiaoXiangDialog.gjjxleixing.getSelectedItem().toString().trim();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    View.OnClickListener gjjxOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_node_guangjiaoxiang:
                    AddNodeDialogShow(latLngs,"gjjx",project.getProjectId());
                    break;
                case R.id.add_cable_gjjx:
                    SelectCableDialogShow(nodeCacheList,"gjjx");
                    break;
                case R.id.gjjx_send_mark:
                    guangJiaoXiangDialog.dismiss();
                    if(guangJiaoXiangDialog.gjjxName.getText().toString().trim().length()<1){
                        Toast.makeText(getBaseContext(),"光交接箱名不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        if(guangJiaoXiangDialog.gjjxName.getText().toString().trim().contains("GJ")){
                            MarkerOptions markerOptions=new MarkerOptions();
                            markerOptions.position(latLngs);
                            markerOptions.title("光交接箱"+guangJiaoXiangDialog.gjjxName.getText().toString().trim());
                            markerOptions.draggable(true);
                            markerOptions.visible(true);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), gjjx_mark));
                            markerOptions.icon(bitmapDescriptor);
                            aMap.addMarker(markerOptions);

                            GJiaoX gJiaoX=new GJiaoX();
                            gJiaoX.setTotheproject(proname);
                            gJiaoX.setGjxname(guangJiaoXiangDialog.gjjxName.getText().toString().trim());
                            gJiaoX.setLatitude(Double.toString(latLngs.latitude));
                            gJiaoX.setLongitude(Double.toString(latLngs.longitude));
                            gJiaoX.setFqdjibie(gjjxjibie);
                            gJiaoX.setGjxlb(gjjxleixing);

                            gJiaoX.setAddtime(String.valueOf(System.currentTimeMillis()));
                            gJiaoX.setProjectId(project.getProjectId());
                            gJiaoX.setOrgId(project.getOrgId());
                            gJiaoX.save();

                            //volley-1 保存到POintSet表（所有标记表）
                            CreateMark(latLngs,proname,System.currentTimeMillis(),guangJiaoXiangDialog.gjjxName.getText().toString().trim()
                                    ,"LIGHTBOX",project.getProjectId(),project.getOrgId());

                            //结束节点（当前节点）
                            final GJiaoX gJiaoXlVolley=DataSupport.where("gjxname=? and projectId=? and orgId=?"
                                    ,guangJiaoXiangDialog.gjjxName.getText().toString().trim(),project.getProjectId(),project.getOrgId())
                                    .find(GJiaoX.class).get(0);
                            List<Map<String ,String>> conduit=new ArrayList<>();
                            List<String> stringList=guangJiaoXiangDialog.gjjxList;
                            for(int i=0;i<nodeCacheList.size();i++){
                                NodeCache nodeCache=nodeCacheList.get(i);
                                if(stringList.contains(nodeCache.getStartnodename())){

                                    //根据起点的名字在标记表获取信息
                                    PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                            nodeCache.getStartnodename(),project.getProjectId(),project.getOrgId())
                                            .find(PointSet.class).get(0);//开始节点（上一节点）

                                    Pipeline pipeline=new Pipeline();
                                    pipeline.setStartnodename(nodeCache.getStartnodename());
                                    pipeline.setEndnodename(guangJiaoXiangDialog.gjjxName.getText().toString().trim());
                                    pipeline.setPipelinehole(nodeCache.getPipelinehole());
                                    pipeline.setPipelinename(nodeCache.getPipelinename());
                                    pipeline.setPipelinetype(nodeCache.getPipelinetype());
                                    pipeline.setPipelinespecifica(nodeCache.getPipelinespecifica());

                                    pipeline.setProjectname(proname);//管道 保存项目的名程（初始化连线）
                                    pipeline.setProjectId(project.getProjectId());
                                    pipeline.setOrgId(project.getOrgId());
                                    pipeline.setStartPointType(pointSet.getPointType());
                                    pipeline.setStartLatitude(pointSet.getLatitude());
                                    pipeline.setStartLongitude(pointSet.getLongitude());
                                    pipeline.setAddtime(gJiaoXlVolley.getAddtime());
                                    pipeline.setEndPointType("LIGHTBOX");
                                    pipeline.setEndLatitude(gJiaoXlVolley.getLatitude());
                                    pipeline.setEndLongitude(gJiaoXlVolley.getLongitude());
                                    pipeline.save();

                                    Map<String,String> stringMap=new HashMap<>();
                                    stringMap.put("name",nodeCache.getPipelinename());//管道名
                                    stringMap.put("stand",nodeCache.getPipelinespecifica());//管道规格
                                    stringMap.put("type",nodeCache.getPipelinetype());//管道类型
                                    stringMap.put("length","20");//管道长度
                                    stringMap.put("holeNumber",nodeCache.getPipelinehole());//管道孔数
                                    stringMap.put("startPointName",nodeCache.getStartnodename());
                                    stringMap.put("startPointType",pointSet.getPointType());
                                    stringMap.put("startLatitude",pointSet.getLatitude());
                                    stringMap.put("startPointId",pointSet.getMarkId());
                                    stringMap.put("startLongitude",pointSet.getLongitude());
                                    stringMap.put("endPointName",gJiaoXlVolley.getGjxname());
                                    stringMap.put("endPointType","LIGHTBOX");
                                    stringMap.put("endLatitude",gJiaoXlVolley.getLatitude());
                                    stringMap.put("endLongitude",gJiaoXlVolley.getLongitude());
                                    stringMap.put("addTime",gJiaoXlVolley.getAddtime());
                                    stringMap.put("projectId",project.getProjectId());
                                    stringMap.put("orgId",project.getOrgId());
                                    conduit.add(stringMap);


                                }
                            }
                            List<Map<String ,String>> cableSegment=new ArrayList<>();
                            //保存光缆资源
                            List<CableCache> SaveCable=guangJiaoXiangDialog.cableCacheList;
                            if(guangJiaoXiangDialog.gjjxList.size()>0){
                                if(SaveCable.size()>0){
                                    for(int i=0;i<SaveCable.size();i++){
                                        CableCache cableCache=SaveCable.get(i);

                                        //根据起点的名字获取管道的名字在管道表中
                                        Pipeline pipeline=DataSupport.where("projectId=? and startnodename=? and endnodename=?",
                                                project.getProjectId(),cableCache.getCableorigin(),guangJiaoXiangDialog.gjjxName.getText().toString().trim())
                                                .find(Pipeline.class).get(0);

                                        //根据起点的名字在标记表获取信息
                                        PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                                cableCache.getCableorigin(),project.getProjectId(),project.getOrgId())
                                                .find(PointSet.class).get(0);//开始节点（上一节点）


                                        GJiaoX gJiaoXEnd=DataSupport.where("polename=? and projectId=?"
                                                ,guangJiaoXiangDialog.gjjxName.getText().toString().trim(),project.getProjectId())
                                                .find(GJiaoX.class).get(0);//结束节点（当前节点）

                                        CableResource cableResource=new CableResource();
                                        cableResource.setCablename(cableCache.getCablename());
                                        cableResource.setOriginname(cableCache.getCableorigin());
                                        cableResource.setEndname(guangJiaoXiangDialog.gjjxName.getText().toString().trim());
                                        cableResource.setHole(cableCache.getHole());
                                        cableResource.setChildhole(cableCache.getChildhole());

                                        cableResource.setProjectId(project.getProjectId());
                                        cableResource.setOrgId(project.getOrgId());
                                        cableResource.setConduitName(pipeline.getPipelinename());
                                        cableResource.setStartPointName(cableCache.getCableorigin());
                                        cableResource.setStartPointType(pointSet.getPointType());
                                        cableResource.setStartLatitude(pointSet.getLatitude());
                                        cableResource.setStartLongitude(pointSet.getLongitude());
                                        cableResource.setEndPointName(gJiaoXEnd.getGjxname());
                                        cableResource.setEndPointType("LIGHTBOX");
                                        cableResource.setEndLatitude(gJiaoXEnd.getLatitude());
                                        cableResource.setEndLongitude(gJiaoXEnd.getLongitude());
                                        cableResource.setAddTime(gJiaoXEnd.getLongitude());
                                        cableResource.save();

                                        Map<String,String> map=new HashMap<>();
                                        map.put("cableName",cableCache.getCablename());
                                        map.put("conduitName",pipeline.getPipelinename());
                                        map.put("useHole",cableCache.getHole());
                                        map.put("useSubHole",cableCache.getChildhole());
                                        map.put("length","20");
                                        map.put("startPointName",cableCache.getCableorigin());
                                        map.put("startPointType",pointSet.getPointType());
                                        map.put("startLatitude",pointSet.getLatitude());
                                        map.put("startPointId",pointSet.getMarkId());
                                        map.put("startLongitude",pointSet.getLongitude());
                                        map.put("endPointName",gJiaoXEnd.getGjxname());
                                        map.put("endPointType","LIGHTBOX");
                                        map.put("endLatitude",gJiaoXEnd.getLatitude());
                                        map.put("endLongitude",gJiaoXEnd.getLongitude());
                                        map.put("addTime",gJiaoXEnd.getAddtime());
                                        map.put("projectId",project.getProjectId());
                                        map.put("orgId",project.getOrgId());
                                        cableSegment.add(map);

                                    }
                                }
                            }

                            projectModel.volleyAddMarks("LIGHTBOX", gJiaoXlVolley.getGjxname(), gJiaoXlVolley.getFqdjibie(), gJiaoXlVolley.getLatitude(),
                                    gJiaoXlVolley.getLongitude(),
                                    gJiaoXlVolley.getProjectId(), proname,
                                    gJiaoXlVolley.getAddtime(), "", "", gJiaoXlVolley.getGjxlb(), gJiaoXlVolley.getOrgId(),conduit,cableSegment,
                                    new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            Log.i(TAG,"LIGHTBOX message"+s);
                                            gJiaoXlVolley.setAnnotationId(s.getString("annotationId"));
                                            gJiaoXlVolley.save();
                                            UpdateDataUtil.updateConduitAndCableSegment(s);
                                        }

                                        @Override
                                        public void onError(String s) {
                                            Log.i(TAG,"LIGHTBOX error message"+s);
                                        }
                                    });

                            nodeCacheList.removeAll(nodeCacheList);
                            cableCacheList.removeAll(cableCacheList);
                            conduit.removeAll(conduit);
                            cableSegment.removeAll(cableSegment);
                            nodePolyline(guangJiaoXiangDialog.gjjxName.getText().toString().trim());

                        }else {
                           toastMarkt("命名不规范");
                        }
                    }

                    break;
                case R.id.gjjx_cancel_mark:
                    guangJiaoXiangDialog.dismiss();
                    break;
            }

        }
    };

    YinShangDianDialog yinShangDianDialog;
    private void YinShangQiang(LatLng latLng){
        nodeCacheList.removeAll(nodeCacheList);
        cableCacheList.removeAll(cableCacheList);
        cableCaches.removeAll(cableCaches);
        StringBuffer sb = new StringBuffer();
        sb.append(latLng.longitude);
        String jingdu = sb.toString();

        StringBuffer zz=new StringBuffer();
        zz.append(latLng.latitude);
        String weidu = zz.toString();
        yinShangDianDialog=new YinShangDianDialog(this,R.style.loading_dialog,ysdOnClickListener,jingdu,weidu);
        yinShangDianDialog.show();

    }
    View.OnClickListener ysdOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_node_ysd:
                    AddNodeDialogShow(latLngs,"ysd",project.getProjectId());
                    break;
                case R.id.add_cable_ysd:
                    SelectCableDialogShow(nodeCacheList,"ysd");
                    break;
                case R.id.ysd_send_mark:
                    yinShangDianDialog.dismiss();
                    if(yinShangDianDialog.ysdname.getText().toString().trim().length()<1){
                        toastMarkt("引上点名不能为空");
                    }else {
                        if(yinShangDianDialog.ysdname.getText().toString().trim().contains("引上")){
                            MarkerOptions markerOptions=new MarkerOptions();
                            markerOptions.position(latLngs);
                            markerOptions.title("引上点"+yinShangDianDialog.ysdname.getText().toString().trim());
                            markerOptions.draggable(true);
                            markerOptions.visible(true);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), ysd_mark));
                            markerOptions.icon(bitmapDescriptor);
                            aMap.addMarker(markerOptions);

                            YinShangDian yinShangDian=new YinShangDian();
                            yinShangDian.setYsdname(yinShangDianDialog.ysdname.getText().toString().trim());
                            yinShangDian.setTotheproject(proname);
                            yinShangDian.setLatitude(Double.toString(latLngs.latitude));
                            yinShangDian.setLongitude(Double.toString(latLngs.longitude));

                            yinShangDian.setAddtime(String.valueOf(System.currentTimeMillis()));
                            yinShangDian.setProjectId(project.getProjectId());
                            yinShangDian.setOrgId(project.getOrgId());
                            yinShangDian.save();

                            //volley-1 保存到POintSet表（所有标记表）
                            CreateMark(latLngs,proname,System.currentTimeMillis(),yinShangDianDialog.ysdname.getText().toString().trim(),
                                    "UPPOINT",project.getProjectId(),project.getOrgId());

                            //结束节点（当前节点）
                            final YinShangDian yinShangDianVolley=DataSupport.where("ysdname=? and projectId=?"
                                    ,yinShangDianDialog.ysdname.getText().toString().trim(),project.getProjectId())
                                    .find(YinShangDian.class).get(0);

                            List<Map<String ,String>> conduit=new ArrayList<>();
                            List<String> stringList=yinShangDianDialog.ysdxList;
                            for(int i=0;i<nodeCacheList.size();i++){
                                NodeCache nodeCache=nodeCacheList.get(i);
                                if(stringList.contains(nodeCache.getStartnodename())){
                                    //根据起点的名字在标记表获取信息
                                    PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                            nodeCache.getStartnodename(),project.getProjectId(),project.getOrgId())
                                            .find(PointSet.class).get(0);//开始节点（上一节点）

                                    Pipeline pipeline=new Pipeline();
                                    pipeline.setStartnodename(nodeCache.getStartnodename());
                                    pipeline.setEndnodename(yinShangDianDialog.ysdname.getText().toString().trim());
                                    pipeline.setPipelinehole(nodeCache.getPipelinehole());
                                    pipeline.setPipelinename(nodeCache.getPipelinename());
                                    pipeline.setPipelinetype(nodeCache.getPipelinetype());
                                    pipeline.setPipelinespecifica(nodeCache.getPipelinespecifica());

                                    pipeline.setProjectname(proname);//管道 保存项目的名程（初始化连线）
                                    pipeline.setProjectId(project.getProjectId());
                                    pipeline.setOrgId(project.getOrgId());
                                    pipeline.setStartPointType(pointSet.getPointType());
                                    pipeline.setStartLatitude(pointSet.getLatitude());
                                    pipeline.setStartLongitude(pointSet.getLongitude());
                                    pipeline.setAddtime(yinShangDianVolley.getAddtime());
                                    pipeline.setEndPointType("UPPOINT");
                                    pipeline.setEndLatitude(yinShangDianVolley.getLatitude());
                                    pipeline.setEndLongitude(yinShangDianVolley.getLongitude());
                                    pipeline.save();

                                    Map<String,String> stringMap=new HashMap<>();
                                    stringMap.put("name",nodeCache.getPipelinename());//管道名
                                    stringMap.put("stand",nodeCache.getPipelinespecifica());//管道规格
                                    stringMap.put("type",nodeCache.getPipelinetype());//管道类型
                                    stringMap.put("length","20");//管道长度
                                    stringMap.put("holeNumber",nodeCache.getPipelinehole());//管道孔数
                                    stringMap.put("startPointName",nodeCache.getStartnodename());
                                    stringMap.put("startPointType",pointSet.getPointType());
                                    stringMap.put("startLatitude",pointSet.getLatitude());
                                    stringMap.put("startPointId",pointSet.getMarkId());
                                    stringMap.put("startLongitude",pointSet.getLongitude());
                                    stringMap.put("endPointName",yinShangDianVolley.getYsdname());
                                    stringMap.put("endPointType","UPPOINT");
                                    stringMap.put("endLatitude",yinShangDianVolley.getLatitude());
                                    stringMap.put("endLongitude",yinShangDianVolley.getLongitude());
                                    stringMap.put("addTime",yinShangDianVolley.getAddtime());
                                    stringMap.put("projectId",project.getProjectId());
                                    stringMap.put("orgId",project.getOrgId());
                                    conduit.add(stringMap);
                                }
                            }
                            List<Map<String ,String>> cableSegment=new ArrayList<>();
                            //保存光缆资源
                            List<CableCache> SaveCable=yinShangDianDialog.cableCacheList;
                            if(yinShangDianDialog.ysdxList.size()>0){
                                if(SaveCable.size()>0){
                                    for(int i=0;i<SaveCable.size();i++){
                                        CableCache cableCache=SaveCable.get(i);
                                        //根据起点的名字获取管道的名字在管道表中
                                        Pipeline pipeline=DataSupport.where("projectId=? and startnodename=? and endnodename=?",
                                                project.getProjectId(),cableCache.getCableorigin(),yinShangDianDialog.ysdname.getText().toString().trim())
                                                .find(Pipeline.class).get(0);

                                        //根据起点的名字在标记表获取信息
                                        PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                                cableCache.getCableorigin(),project.getProjectId(),project.getOrgId())
                                                .find(PointSet.class).get(0);//开始节点（上一节点）


                                        YinShangDian yinShangDianEnd=DataSupport.where("polename=? and projectId=?"
                                                ,yinShangDianDialog.ysdname.getText().toString().trim(),project.getProjectId())
                                                .find(YinShangDian.class).get(0);//结束节点（当前节点）

                                        CableResource cableResource=new CableResource();
                                        cableResource.setCablename(cableCache.getCablename());
                                        cableResource.setOriginname(cableCache.getCableorigin());
                                        cableResource.setEndname(yinShangDianDialog.ysdname.getText().toString().trim());
                                        cableResource.setHole(cableCache.getHole());
                                        cableResource.setChildhole(cableCache.getChildhole());
                                        cableResource.setProjectId(project.getProjectId());
                                        cableResource.setOrgId(project.getOrgId());
                                        cableResource.setConduitName(pipeline.getPipelinename());
                                        cableResource.setStartPointName(cableCache.getCableorigin());
                                        cableResource.setStartPointType(pointSet.getPointType());
                                        cableResource.setStartLatitude(pointSet.getLatitude());
                                        cableResource.setStartLongitude(pointSet.getLongitude());
                                        cableResource.setEndPointName(yinShangDianEnd.getYsdname());
                                        cableResource.setEndPointType("UPPOINT");
                                        cableResource.setEndLatitude(yinShangDianEnd.getLatitude());
                                        cableResource.setEndLongitude(yinShangDianEnd.getLongitude());
                                        cableResource.setAddTime(yinShangDianEnd.getLongitude());
                                        cableResource.save();

                                        Map<String,String> map=new HashMap<>();
                                        map.put("cableName",cableCache.getCablename());
                                        map.put("conduitName",pipeline.getPipelinename());
                                        map.put("useHole",cableCache.getHole());
                                        map.put("useSubHole",cableCache.getChildhole());
                                        map.put("length","20");
                                        map.put("startPointName",cableCache.getCableorigin());
                                        map.put("startPointType",pointSet.getPointType());
                                        map.put("startLatitude",pointSet.getLatitude());
                                        map.put("startPointId",pointSet.getMarkId());
                                        map.put("startLongitude",pointSet.getLongitude());
                                        map.put("endPointName",yinShangDianEnd.getYsdname());
                                        map.put("endPointType","UPPOINT");
                                        map.put("endLatitude",yinShangDianEnd.getLatitude());
                                        map.put("endLongitude",yinShangDianEnd.getLongitude());
                                        map.put("addTime",yinShangDianEnd.getAddtime());
                                        map.put("projectId",project.getProjectId());
                                        map.put("orgId",project.getOrgId());
                                        cableSegment.add(map);
                                    }
                                }
                            }





                            projectModel.volleyAddMarks("UPPOINT", yinShangDianVolley.getYsdname(), "",
                                    yinShangDianVolley.getLatitude(), yinShangDianVolley.getLongitude(),
                                    yinShangDianVolley.getProjectId(), proname,
                                    yinShangDianVolley.getAddtime(), "", "", "", yinShangDianVolley.getOrgId(),conduit,cableSegment,
                                    new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            Log.i(TAG,"UPPOINT message="+s);
                                            yinShangDianVolley.setAnnotationId(s.getString("annotationId"));
                                            yinShangDianVolley.save();
                                            UpdateDataUtil.updateConduitAndCableSegment(s);
                                        }
                                        @Override
                                        public void onError(String s) {
                                            Log.i(TAG,"UPPOINT error message="+s);
                                        }
                                    });

                            nodeCacheList.removeAll(nodeCacheList);
                            cableCacheList.removeAll(cableCacheList);
                            conduit.removeAll(conduit);
                            cableSegment.removeAll(cableSegment);
                            nodePolyline(yinShangDianDialog.ysdname.getText().toString().trim());

                        }else{
                            toastMarkt("命名不规范");
                        }
                    }
                    break;
                case R.id.ysd_cancel_mark:
                    yinShangDianDialog.dismiss();
                    break;
            }

        }
    };

    GuaQiangDialog guaQiangDialog;
    private void Guaqiang(LatLng latLng){
        nodeCacheList.removeAll(nodeCacheList);
        cableCacheList.removeAll(cableCacheList);
        cableCaches.removeAll(cableCaches);
        StringBuffer sb = new StringBuffer();
        sb.append(latLng.longitude);
        String jingdu = sb.toString();

        StringBuffer zz=new StringBuffer();
        zz.append(latLng.latitude);
        String weidu = zz.toString();
        guaQiangDialog=new GuaQiangDialog(this,R.style.loading_dialog,gqOnClickListener,jingdu,weidu);
        guaQiangDialog.show();

    }
    View.OnClickListener gqOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_node_guaqiang:
                    AddNodeDialogShow(latLngs,"guaqiang",project.getProjectId());
                    break;
                case R.id.add_cable_guaqiang:
                    SelectCableDialogShow(nodeCacheList,"guaqiang");
                    break;
                case R.id.gq_send_mark:
                    guaQiangDialog.dismiss();
                    if(guaQiangDialog.gqname.getText().toString().trim().length()<1){
                        toastMarkt("挂墙名不能为空");
                    }else {
                        /*if(guaQiangDialog.gqname.getText().toString().trim().contains(proname) &&
                                guaQiangDialog.gqname.getText().toString().trim().contains("挂墙"))  //命名先这样 到时候规范*/
                        if(guaQiangDialog.gqname.getText().toString().trim().contains("挂墙")){
                            MarkerOptions markerOptions=new MarkerOptions();
                            markerOptions.position(latLngs);
                            markerOptions.title("guaqiang"+guaQiangDialog.gqname.getText().toString().trim());
                            markerOptions.draggable(true);
                            markerOptions.visible(true);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), gq_mark));
                            markerOptions.icon(bitmapDescriptor);
                            aMap.addMarker(markerOptions);

                            GuaQiang guaQiang=new GuaQiang();
                            guaQiang.setGqname(guaQiangDialog.gqname.getText().toString().trim());
                            guaQiang.setLatitude(Double.toString(latLngs.latitude));
                            guaQiang.setLongitude(Double.toString(latLngs.longitude));

                            guaQiang.setAddtime(String.valueOf(System.currentTimeMillis()));
                            guaQiang.setProjectId(project.getProjectId());
                            guaQiang.setOrgId(project.getOrgId());
                            guaQiang.save();
                            //volley-1 保存到POintSet表（所有标记表）
                            CreateMark(latLngs,proname,System.currentTimeMillis(),guaQiangDialog.gqname.getText().toString().trim(),"WALLHANG"
                                    ,project.getProjectId(),project.getOrgId());

                            final GuaQiang guaQiangVolley=DataSupport.where("gqname=? and orgId=? and projectId=?"
                                    ,guaQiangDialog.gqname.getText().toString().trim(),project.getOrgId(),project.getProjectId())
                                    .find(GuaQiang.class).get(0);//结束节点（当前节点）
                            List<Map<String ,String>> conduit=new ArrayList<>();

                            List<String> stringList=guaQiangDialog.gqxList;
                            for(int i=0;i<nodeCacheList.size();i++){
                                NodeCache nodeCache=nodeCacheList.get(i);
                                if(stringList.contains(nodeCache.getStartnodename())){

                                    //根据起点的名字在标记表获取信息
                                    PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                            nodeCache.getStartnodename(),project.getProjectId(),project.getOrgId())
                                            .find(PointSet.class).get(0);//开始节点（上一节点）

                                    Pipeline pipeline=new Pipeline();
                                    pipeline.setStartnodename(nodeCache.getStartnodename());
                                    pipeline.setEndnodename(guaQiangDialog.gqname.getText().toString().trim());
                                    pipeline.setPipelinehole(nodeCache.getPipelinehole());
                                    pipeline.setPipelinename(nodeCache.getPipelinename());
                                    pipeline.setPipelinetype(nodeCache.getPipelinetype());
                                    pipeline.setPipelinespecifica(nodeCache.getPipelinespecifica());
                                    pipeline.setProjectname(proname);//管道 保存项目的名程（初始化连线）
                                    pipeline.setProjectId(project.getProjectId());
                                    pipeline.setOrgId(project.getOrgId());
                                    pipeline.setAddtime(guaQiangVolley.getAddtime());
                                    pipeline.setStartPointType(pointSet.getPointType());
                                    pipeline.setStartLatitude(pointSet.getLatitude());
                                    pipeline.setStartLongitude(pointSet.getLongitude());
                                    pipeline.setEndPointType("WALLHANG");
                                    pipeline.setEndLatitude(guaQiangVolley.getLatitude());
                                    pipeline.setEndLongitude(guaQiangVolley.getLongitude());
                                    pipeline.save();


                                    Map<String,String> stringMap=new HashMap<>();
                                    stringMap.put("name",nodeCache.getPipelinename());//管道名
                                    stringMap.put("stand",nodeCache.getPipelinespecifica());//管道规格
                                    stringMap.put("type",nodeCache.getPipelinetype());//管道类型
                                    stringMap.put("length","20");//管道长度
                                    stringMap.put("holeNumber",nodeCache.getPipelinehole());//管道孔数
                                    stringMap.put("startPointName",nodeCache.getStartnodename());
                                    stringMap.put("startPointType",pointSet.getPointType());
                                    stringMap.put("startLatitude",pointSet.getLatitude());
                                    stringMap.put("startLongitude",pointSet.getLongitude());
                                    stringMap.put("startPointId",pointSet.getMarkId());
                                    stringMap.put("endPointName",guaQiangVolley.getGqname());
                                    stringMap.put("endPointType","WALLHANG");
                                    stringMap.put("endLatitude",guaQiangVolley.getLatitude());
                                    stringMap.put("endLongitude",guaQiangVolley.getLongitude());
                                    stringMap.put("addTime",guaQiangVolley.getAddtime());
                                    stringMap.put("projectId",project.getProjectId());
                                    stringMap.put("orgId",project.getOrgId());
                                    conduit.add(stringMap);
                                }
                            }

                            List<Map<String ,String>> cableSegment=new ArrayList<>();
                            //保存光缆资源
                            List<CableCache> SaveCable=guaQiangDialog.cableCacheList;
                            if(guaQiangDialog.gqxList.size()>0){
                                if(SaveCable.size()>0){
                                    for(int i=0;i<SaveCable.size();i++){
                                        CableCache cableCache=SaveCable.get(i);

                                        //根据起点的名字获取管道的名字在管道表中
                                        Pipeline pipeline=DataSupport.where("projectId=? and startnodename=? and endnodename=?",
                                                project.getProjectId(),cableCache.getCableorigin(),guaQiangDialog.gqname.getText().toString().trim())
                                                .find(Pipeline.class).get(0);

                                        //根据起点的名字在标记表获取信息
                                        PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                                cableCache.getCableorigin(),project.getProjectId(),project.getOrgId())
                                                .find(PointSet.class).get(0);//开始节点（上一节点）



                                        GuaQiang guaQiangEnd=DataSupport.where("gqname=? and projectId=?"
                                                ,guaQiangDialog.gqname.getText().toString().trim(),project.getProjectId())
                                                .find(GuaQiang.class).get(0);//结束节点（当前节点）

                                        CableResource cableResource=new CableResource();
                                        cableResource.setCablename(cableCache.getCablename());
                                        cableResource.setOriginname(cableCache.getCableorigin());
                                        cableResource.setEndname(guaQiangDialog.gqname.getText().toString().trim());
                                        cableResource.setHole(cableCache.getHole());
                                        cableResource.setChildhole(cableCache.getChildhole());

                                        cableResource.setProjectId(project.getProjectId());
                                        cableResource.setOrgId(project.getOrgId());
                                        cableResource.setConduitName(pipeline.getPipelinename());
                                        cableResource.setStartPointName(cableCache.getCableorigin());
                                        cableResource.setStartPointType(pointSet.getPointType());
                                        cableResource.setStartLatitude(pointSet.getLatitude());
                                        cableResource.setStartLongitude(pointSet.getLongitude());
                                        cableResource.setEndPointName(guaQiangEnd.getGqname());
                                        cableResource.setEndPointType("WALLHANG");
                                        cableResource.setEndLatitude(guaQiangEnd.getLatitude());
                                        cableResource.setEndLongitude(guaQiangEnd.getLongitude());
                                        cableResource.setAddTime(guaQiangEnd.getLongitude());
                                        cableResource.save();

                                        Map<String,String> map=new HashMap<>();
                                        map.put("cableName",cableCache.getCablename());
                                        map.put("conduitName",pipeline.getPipelinename());
                                        map.put("useHole",cableCache.getHole());
                                        map.put("useSubHole",cableCache.getChildhole());
                                        map.put("length","20");
                                        map.put("startPointName",cableCache.getCableorigin());
                                        map.put("startPointType",pointSet.getPointType());
                                        map.put("startLatitude",pointSet.getLatitude());
                                        map.put("startLongitude",pointSet.getLongitude());
                                        map.put("startPointId",pointSet.getMarkId());
                                        map.put("endPointName",guaQiangEnd.getGqname());
                                        map.put("endPointType","WALLHANG");
                                        map.put("endLatitude",guaQiangEnd.getLatitude());
                                        map.put("endLongitude",guaQiangEnd.getLongitude());
                                        map.put("addTime",guaQiangEnd.getAddtime());
                                        map.put("projectId",project.getProjectId());
                                        map.put("orgId",project.getOrgId());
                                        cableSegment.add(map);
                                    }
                                }
                            }


                            projectModel.volleyAddMarks("WALLHANG", guaQiangVolley.getGqname(), "",
                                    guaQiangVolley.getLatitude(), guaQiangVolley.getLongitude(),
                                    guaQiangVolley.getAddtime(), proname,
                                    guaQiangVolley.getAddtime(), "", "", "", guaQiangVolley.getOrgId(),conduit,cableSegment,
                                    new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            Log.i(TAG,"WALLHANG message="+s);
                                            guaQiangVolley.setAnnotationId(s.getString("annotationId"));
                                            guaQiangVolley.save();
                                            UpdateDataUtil.updateConduitAndCableSegment(s);
                                        }
                                        @Override
                                        public void onError(String s) {
                                            Log.i(TAG,"WALLHANG error message="+s);
                                        }
                                    });
                            nodeCacheList.removeAll(nodeCacheList);
                            cableCacheList.removeAll(cableCacheList);
                            conduit.removeAll(conduit);
                            cableSegment.removeAll(cableSegment);
                            nodePolyline(guaQiangDialog.gqname.getText().toString().trim());

                        }else {
                            toastMarkt("命名不规范");
                        }
                    }

                    break;
                case R.id.gq_cancel_mark:
                    guaQiangDialog.dismiss();
                    break;
            }

        }
    };
    //TODO (1)
    JieTouHeDialog jieTouHeDialog;
    private void JieTouHe(LatLng latLng){
        nodeCacheList.removeAll(nodeCacheList);
        cableCacheList.removeAll(cableCacheList);
        cableCaches.removeAll(cableCaches);
        StringBuffer sb = new StringBuffer();
        sb.append(latLng.longitude);
        String jingdu = sb.toString();

        StringBuffer zz=new StringBuffer();
        zz.append(latLng.latitude);
        String weidu = zz.toString();
        jieTouHeDialog =new JieTouHeDialog(this,R.style.loading_dialog,jthOnClickListener,jingdu,weidu);
        jieTouHeDialog.show();
    }
    View.OnClickListener jthOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_node_jth:
                    AddNodeDialogShow(latLngs,"jth",project.getProjectId());
                    break;
                case R.id.add_cable_jth:
                    SelectCableDialogShow(nodeCacheList,"jth");
                    break;
                case R.id.jth_send_mark:
                    jieTouHeDialog.dismiss();
                    if(jieTouHeDialog.jthname.getText().toString().trim().length()<1){
                        toastMarkt("接头盒名不能为空");
                    }else {
                        if(jieTouHeDialog.jthname.getText().toString().trim().contains("接头盒")){

                            MarkerOptions markerOptions=new MarkerOptions();
                            markerOptions.position(latLngs);
                            markerOptions.title("jietouhe"+jieTouHeDialog.jthname.getText().toString().trim());
                            markerOptions.draggable(true);
                            markerOptions.visible(true);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), jth_mark));
                            markerOptions.icon(bitmapDescriptor);
                            aMap.addMarker(markerOptions);

                            JieTouHe jieTouHe=new JieTouHe();
                            jieTouHe.setJthname(jieTouHeDialog.jthname.getText().toString().trim());
                            jieTouHe.setLatitude(Double.toString(latLngs.latitude));
                            jieTouHe.setLongitude(Double.toString(latLngs.longitude));
                            jieTouHe.setTotheproject(proname);

                            jieTouHe.setAddtime(String.valueOf(System.currentTimeMillis()));
                            jieTouHe.setProjectId(project.getProjectId());
                            jieTouHe.setOrgId(project.getOrgId());
                            jieTouHe.save();

                            //volley-1 保存到POintSet表（所有标记表）
                            CreateMark(latLngs,proname,System.currentTimeMillis(),jieTouHeDialog.jthname.getText().toString().trim()
                                    ,"JOINTBOX",project.getProjectId(),project.getOrgId());


                            //结束节点（当前节点）
                            final JieTouHe jieTouHeDianVolley=DataSupport.where("jthname=? and projectId=? and orgId=?"
                                    ,jieTouHeDialog.jthname.getText().toString().trim(),project.getProjectId(),project.getOrgId())
                                    .find(JieTouHe.class).get(0);

                            List<Map<String ,String>> conduit=new ArrayList<>();

                            List<String> stringList=jieTouHeDialog.jthList;
                            for(int i=0;i<nodeCacheList.size();i++){
                                NodeCache nodeCache=nodeCacheList.get(i);
                                if(stringList.contains(nodeCache.getStartnodename())){

                                    //根据起点的名字在标记表获取信息
                                    PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                            nodeCache.getStartnodename(),project.getProjectId(),project.getOrgId())
                                            .find(PointSet.class).get(0);//开始节点（上一节点）

                                    Pipeline pipeline=new Pipeline();
                                    pipeline.setStartnodename(nodeCache.getStartnodename());
                                    pipeline.setEndnodename(jieTouHeDialog.jthname.getText().toString().trim());
                                    pipeline.setPipelinehole(nodeCache.getPipelinehole());
                                    pipeline.setPipelinename(nodeCache.getPipelinename());
                                    pipeline.setPipelinetype(nodeCache.getPipelinetype());
                                    pipeline.setPipelinespecifica(nodeCache.getPipelinespecifica());

                                    pipeline.setProjectname(proname);//管道 保存项目的名程（初始化连线）
                                    pipeline.setProjectId(project.getProjectId());
                                    pipeline.setOrgId(project.getOrgId());
                                    pipeline.setAddtime(jieTouHeDianVolley.getAddtime());
                                    pipeline.setStartPointType(pointSet.getPointType());
                                    pipeline.setStartLatitude(pointSet.getLatitude());
                                    pipeline.setStartLongitude(pointSet.getLongitude());
                                    pipeline.setEndPointType("JOINTBOX");
                                    pipeline.setEndLatitude(jieTouHeDianVolley.getLatitude());
                                    pipeline.setEndLongitude(jieTouHeDianVolley.getLongitude());
                                    pipeline.save();


                                    Map<String,String> stringMap=new HashMap<>();
                                    stringMap.put("name",nodeCache.getPipelinename());//管道名
                                    stringMap.put("stand",nodeCache.getPipelinespecifica());//管道规格
                                    stringMap.put("type",nodeCache.getPipelinetype());//管道类型
                                    stringMap.put("length","20");//管道长度
                                    stringMap.put("holeNumber",nodeCache.getPipelinehole());//管道孔数
                                    stringMap.put("startPointName",nodeCache.getStartnodename());
                                    stringMap.put("startPointType",pointSet.getPointType());
                                    stringMap.put("startLatitude",pointSet.getLatitude());
                                    stringMap.put("startLongitude",pointSet.getLongitude());
                                    stringMap.put("startPointId",pointSet.getMarkId());
                                    stringMap.put("endPointName",jieTouHeDianVolley.getJthname());
                                    stringMap.put("endPointType","JOINTBOX");
                                    stringMap.put("endLatitude",jieTouHeDianVolley.getLatitude());
                                    stringMap.put("endLongitude",jieTouHeDianVolley.getLongitude());
                                    stringMap.put("addTime",jieTouHeDianVolley.getAddtime());
                                    stringMap.put("projectId",project.getProjectId());
                                    stringMap.put("orgId",project.getOrgId());
                                    conduit.add(stringMap);
                                }
                            }

                            List<Map<String ,String>> cableSegment=new ArrayList<>();
                            //保存光缆资源
                            List<CableCache> SaveCable=jieTouHeDialog.cableCacheList;
                            if(jieTouHeDialog.jthList.size()>0){
                                if(SaveCable.size()>0){
                                    for(int i=0;i<SaveCable.size();i++){
                                        CableCache cableCache=SaveCable.get(i);
                                        //根据起点的名字获取管道的名字在管道表中
                                        Pipeline pipeline=DataSupport.where("projectId=? and startnodename=? and endnodename=?",
                                                project.getProjectId(),cableCache.getCableorigin(),jieTouHeDialog.jthname.getText().toString().trim())
                                                .find(Pipeline.class).get(0);

                                        //根据起点的名字在标记表获取信息
                                        PointSet pointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                                                cableCache.getCableorigin(),project.getProjectId(),project.getOrgId())
                                                .find(PointSet.class).get(0);//开始节点（上一节点）

                                        //结束节点（当前节点）
                                        JieTouHe jieTouHeEnd=DataSupport.where("jthname=? and projectId=?"
                                                ,jieTouHeDialog.jthname.getText().toString().trim(),project.getProjectId())
                                                .find(JieTouHe.class).get(0);
                                        CableResource cableResource=new CableResource();
                                        cableResource.setCablename(cableCache.getCablename());
                                        cableResource.setOriginname(cableCache.getCableorigin());
                                        cableResource.setEndname(jieTouHeDialog.jthname.getText().toString().trim());
                                        cableResource.setHole(cableCache.getHole());
                                        cableResource.setChildhole(cableCache.getChildhole());

                                        cableResource.setProjectId(project.getProjectId());
                                        cableResource.setOrgId(project.getOrgId());
                                        cableResource.setConduitName(pipeline.getPipelinename());
                                        cableResource.setStartPointName(cableCache.getCableorigin());
                                        cableResource.setStartPointType(pointSet.getPointType());
                                        cableResource.setStartLatitude(pointSet.getLatitude());
                                        cableResource.setStartLongitude(pointSet.getLongitude());
                                        cableResource.setEndPointName(jieTouHeEnd.getJthname());
                                        cableResource.setEndPointType("JOINTBOX");
                                        cableResource.setEndLatitude(jieTouHeEnd.getLatitude());
                                        cableResource.setEndLongitude(jieTouHeEnd.getLongitude());
                                        cableResource.setAddTime(jieTouHeEnd.getLongitude());
                                        cableResource.save();

                                        Map<String,String> map=new HashMap<>();
                                        map.put("cableName",cableCache.getCablename());
                                        map.put("conduitName",pipeline.getPipelinename());
                                        map.put("useHole",cableCache.getHole());
                                        map.put("useSubHole",cableCache.getChildhole());
                                        map.put("length","20");
                                        map.put("startPointName",cableCache.getCableorigin());
                                        map.put("startPointType",pointSet.getPointType());
                                        map.put("startLatitude",pointSet.getLatitude());
                                        map.put("startLongitude",pointSet.getLongitude());
                                        map.put("startPointId",pointSet.getMarkId());
                                        map.put("endPointName",jieTouHeEnd.getJthname());
                                        map.put("endPointType","JOINTBOX");
                                        map.put("endLatitude",jieTouHeEnd.getLatitude());
                                        map.put("endLongitude",jieTouHeEnd.getLongitude());
                                        map.put("addTime",jieTouHeEnd.getAddtime());
                                        map.put("projectId",project.getProjectId());
                                        map.put("orgId",project.getOrgId());
                                        cableSegment.add(map);
                                    }
                                }
                            }



                            projectModel.volleyAddMarks("JOINTBOX", jieTouHeDianVolley.getJthname(), "",
                                    jieTouHeDianVolley.getLatitude(), jieTouHeDianVolley.getLongitude(),
                                    jieTouHeDianVolley.getProjectId(), proname,
                                    jieTouHeDianVolley.getAddtime(), "", "", "", jieTouHeDianVolley.getOrgId(),conduit,cableSegment,
                                    new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            Log.i(TAG,"JOINTBOX message="+s);
                                            jieTouHeDianVolley.setAnnotationId(s.getString("annotationId"));
                                            jieTouHeDianVolley.save();
                                            UpdateDataUtil.updateConduitAndCableSegment(s);
                                        }
                                        @Override
                                        public void onError(String s) {
                                            Log.i(TAG,"JOINTBOX error message="+s);
                                        }
                                    });
                            nodeCacheList.removeAll(nodeCacheList);
                            cableCacheList.removeAll(cableCacheList);
                            conduit.removeAll(conduit);
                            cableSegment.removeAll(cableSegment);

                            nodePolyline(jieTouHeDialog.jthname.getText().toString().trim());
                        }else {
                            toastMarkt("命名不规范");
                        }
                    }

                    break;
                case R.id.jth_cancel_mark:
                    jieTouHeDialog.dismiss();
                    break;
            }
        }
    };

    private List<String> nodelist = new ArrayList<String>();
    private List<String> cablelist=new ArrayList<>();
    private Set<SwipeListLayout> sets = new HashSet();
    private Set<SwipeListLayout> cablesets=new HashSet<>();
    //自定义的
    @Override
    public View getInfoWindow(Marker marker) {

        //Todo (2)显示自定义的window

        if(infoWindow==null){
            String s=marker.getTitle().trim();
            if(s.indexOf("人手井")!=-1){//包含人手井
                infoWindow=getLayoutInflater().inflate(R.layout.create_renshoujing_infowindow,null);
            }else if(s.indexOf("当前位置")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_dangqianweizhi_infowindow,null);
            }else if(s.indexOf("电杆")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_diangan_infowindow,null);
            }else if(s.indexOf("光交接箱")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_guangjiaoxiang_infowindow,null);
            }else if(s.indexOf("引上点")!=-1){
                infoWindow= getLayoutInflater().inflate(R.layout.create_yinshangdian_infowindow,null);
            }else if(s.indexOf("guaqiang")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_guaqiang_infowindow,null);
            }else if(s.indexOf("jietouhe")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_jietouhe_window,null);
            }
        }else {
            String s=marker.getTitle().trim();
            if(s.indexOf("人手井")!=-1){//包含人手井
                infoWindow=getLayoutInflater().inflate(R.layout.create_renshoujing_infowindow,null);
            }else if(s.indexOf("当前位置")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_dangqianweizhi_infowindow,null);
            }else if(s.indexOf("电杆")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_diangan_infowindow,null);
            }else if(s.indexOf("光交接箱")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_guangjiaoxiang_infowindow,null);
            }else if(s.indexOf("引上点")!=-1){
                infoWindow= getLayoutInflater().inflate(R.layout.create_yinshangdian_infowindow,null);
            }else if(s.indexOf("guaqiang")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_guaqiang_infowindow,null);
            }else if(s.indexOf("jietouhe")!=-1){
                infoWindow=getLayoutInflater().inflate(R.layout.create_jietouhe_window,null);
            }
        }

        render(marker,infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private ListView WindowListView;
    private ListView WindowCableListView;
    private Button addNode,addCable;
    private Button cancelNode,sendNode;
    private String windowname;
    public void render(final Marker marker, View view) {

        nodelist.removeAll(nodelist);
        cablelist.removeAll(cablelist);

        sets.removeAll(sets);
        cablesets.removeAll(cablesets);

        nodeCacheList.removeAll(nodeCacheList);
        cableCacheList.removeAll(cableCacheList);

        cableCaches.removeAll(cableCaches);
        addConduitArray.removeAll(addConduitArray);
        addCableSegmentArray.removeAll(addCableSegmentArray);
        removeConduit.removeAll(removeConduit);
        removeCableSegment.removeAll(removeCableSegment);
        addConduit.removeAll(addConduit);
        addCableSegment.removeAll(addCableSegment);

        //如果想修改自定义Infow中内容，请通过view找到它并修改
        String s=marker.getTitle().trim();
        if(s.indexOf("人手井")!=-1){
            //获取到人手井name
            String x=marker.getTitle().trim();
            x=x.replace("人手井","").trim();
            windowname=x;
            final PeopleWell peopleWell=DataSupport.where("wellname=? and projectId=?",x,project.getProjectId())
                    .find(PeopleWell.class).get(0);

            //初始化上一节点的list数据
            initMakerNodeList(windowname);

            //初始化已当前标记为结束点的光缆段，list数据
            initMakerCableList(peopleWell.getWellname());

            TextView rsjname=(TextView) view.findViewById(R.id.rsj_name_window);
            TextView jingdu=(TextView) view.findViewById(R.id.jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.weidu_window);
            final Spinner guige=(Spinner)view.findViewById(R.id.rsj_size_window);
            WindowListView=(ListView)view.findViewById(R.id.rsj_node_name_list_window);
            WindowCableListView=(ListView)view.findViewById(R.id.rsj_cable_name_list_window);
            addNode=(Button)view.findViewById(R.id.add_node_rsj_window);
            addCable=(Button)view.findViewById(R.id.add_cable_rsj_window);
            cancelNode=(Button)view.findViewById(R.id.cancel_node_window);
            sendNode=(Button)view.findViewById(R.id.send_node_window);
            rsjname.setText(peopleWell.getWellname());
            jingdu.setText(peopleWell.getLongitude());
            weidu.setText(peopleWell.getLatitude());


            //listview(上一节点) 的数据设置以及删除
            ListAdapter listAdapter= new ListAdapter();
            listAdapter.notifyDataSetChanged();
            WindowListView.setAdapter(listAdapter);
            WindowListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (sets.size() > 0) {
                                for (SwipeListLayout s : sets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    sets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            CableListAdapter cableListAdapter=new CableListAdapter();
            cableListAdapter.notifyDataSetChanged();
            WindowCableListView.setAdapter(cableListAdapter);
            WindowCableListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (cablesets.size() > 0) {
                                for (SwipeListLayout s : cablesets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    cablesets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });


            addNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNodeDialogShow(marker.getPosition(),"WellWindow",project.getProjectId());
                }
            });
            addCable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectCableDialogShow(nodeCacheList,"WellWindow");
                }
            });

            setSpinnerItemSelectedByValue(guige,peopleWell.getSpecifications());
            guige.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    peopleWell.setSpecifications(guige.getSelectedItem().toString().trim());
                    peopleWell.save();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sendNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodeAndcableCancle(peopleWell.getWellname());
                    updateAnnotation(peopleWell.getWellname(),"WELL");
                    markLigature();
                }

            });
            cancelNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodelist.removeAll(nodelist);
                    cablelist.removeAll(cablelist);
                    nodeCacheList.removeAll(nodeCacheList);
                    cableCacheList.removeAll(cableCacheList);
                    if(curShowWindowMarker!=null){
                        curShowWindowMarker.hideInfoWindow();
                    }
                }
            });


        }else if(s.indexOf("当前位置")!=-1){
            /*TextView qu=(TextView)view.findViewById(R.id.dqwz_qu_window);
            TextView dizhi=(TextView)view.findViewById(R.id.dqwz_dizhi_window);*/
            TextView jingdu=(TextView)view.findViewById(R.id.dqwz_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.dqwz_weidu_window);
            jingdu.setText(Double.toString(marker.getPosition().longitude));
            weidu.setText(Double.toString(marker.getPosition().latitude));
           /* dizhi.setText("杭州市西湖区文三路马塍路");
            qu.setText("西湖区");*/
        }else if(s.indexOf("电杆")!=-1){

            String x=marker.getTitle().trim();
            x=x.replace("电杆","").trim();
            windowname=x;
            final Pole pole=DataSupport.where("polename=?",x).find(Pole.class).get(0);

            //初始化上一节点的list数据
            initMakerNodeList(pole.getPolename());
            //初始化已当前标记为结束点的光缆段，list数据
            initMakerCableList(pole.getPolename());
            final TextView dgname=(TextView) view.findViewById(R.id.diangan_name_window);
            TextView dgjingdu=(TextView) view.findViewById(R.id.diangan_jingdu_window);
            TextView dgweidu=(TextView)view.findViewById(R.id.diangan_weidu_window);
            final Spinner dgheight=(Spinner)view.findViewById(R.id.diangan_height_window);
            final Spinner dgfencha=(Spinner)view.findViewById(R.id.diangan_fencha_window);
            WindowListView=(ListView)view.findViewById(R.id.diangan_node_name_list_window);
            WindowCableListView=(ListView)view.findViewById(R.id.diangan_cable_name_list_window);
            addNode=(Button)view.findViewById(R.id.add_node_diangan_window);
            addCable=(Button)view.findViewById(R.id.add_cable_diangan_window);
            cancelNode=(Button)view.findViewById(R.id.cancel_node_window);
            sendNode=(Button)view.findViewById(R.id.send_node_window);

            setSpinnerItemSelectedByValue(dgheight,pole.getPoleheight());
            setSpinnerItemSelectedByValue(dgfencha,pole.getFencha());

            dgname.setText(pole.getPolename());
            dgjingdu.setText(pole.getLongitude());
            dgweidu.setText(pole.getLatitude());


            dgheight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pole.setPoleheight(dgheight.getSelectedItem().toString().trim());
                    pole.save();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
             });
            dgfencha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pole.setFencha(dgfencha.getSelectedItem().toString().trim());
                    pole.save();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //listview(上一节点) 的数据设置以及删除
            ListAdapter listAdapter= new ListAdapter();
            listAdapter.notifyDataSetChanged();
            WindowListView.setAdapter(listAdapter);
            WindowListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (sets.size() > 0) {
                                for (SwipeListLayout s : sets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    sets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            CableListAdapter cableListAdapter=new CableListAdapter();
            cableListAdapter.notifyDataSetChanged();
            WindowCableListView.setAdapter(cableListAdapter);
            WindowCableListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (cablesets.size() > 0) {
                                for (SwipeListLayout s : cablesets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    cablesets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            addNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNodeDialogShow(marker.getPosition(),"WellWindow",project.getProjectId());
                }
            });
            addCable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectCableDialogShow(nodeCacheList,"WellWindow");
                }
            });
            sendNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodeAndcableCancle(pole.getPolename());
                    updateAnnotation(pole.getPolename(),"POLE");
                    markLigature();
                }
            });
            cancelNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodelist.removeAll(nodelist);
                    cablelist.removeAll(cablelist);
                    nodeCacheList.removeAll(nodeCacheList);
                    cableCacheList.removeAll(cableCacheList);
                    if(curShowWindowMarker!=null){
                        curShowWindowMarker.hideInfoWindow();
                    }
                }
            });



        }else if(s.indexOf("光交接箱")!=-1){
            String x=marker.getTitle().trim();
            x=x.replace("光交接箱","");
            windowname=x;
            final GJiaoX gJiaoX=DataSupport.where("gjxname=?",x).find(GJiaoX.class).get(0);
            //初始化上一节点的list数据
            initMakerNodeList(gJiaoX.getGjxname());
            //初始化已当前标记为结束点的光缆段，list数据
            initMakerCableList(gJiaoX.getGjxname());
            TextView gjjxname=(TextView)view.findViewById(R.id.gjjx_name_window);
            final Spinner gjjxjb=(Spinner)view.findViewById(R.id.gjjx_jibie_window);
            final Spinner gjjxlx=(Spinner)view.findViewById(R.id.gjjx_leixing_window);
            TextView jingdiu=(TextView)view.findViewById(R.id.gjjx_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.gjjx_weidu_window);
            WindowListView=(ListView)view.findViewById(R.id.gjjx_node_name_list_window);
            WindowCableListView=(ListView)view.findViewById(R.id.gjjx_cable_name_list_window);
            addNode=(Button)view.findViewById(R.id.add_node_gjjx_window);
            addCable=(Button)view.findViewById(R.id.add_cable_gjjx_window);
            cancelNode=(Button)view.findViewById(R.id.cancel_node_window);
            sendNode=(Button)view.findViewById(R.id.send_node_window);
            gjjxname.setText(gJiaoX.getGjxname());
            setSpinnerItemSelectedByValue(gjjxjb,gJiaoX.getFqdjibie());
            setSpinnerItemSelectedByValue(gjjxlx,gJiaoX.getGjxlb());
            jingdiu.setText(gJiaoX.getLongitude());
            weidu.setText(gJiaoX.getLatitude());

            //listview(上一节点) 的数据设置以及删除
            ListAdapter listAdapter= new ListAdapter();
            listAdapter.notifyDataSetChanged();
            WindowListView.setAdapter(listAdapter);
            WindowListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (sets.size() > 0) {
                                for (SwipeListLayout s : sets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    sets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            CableListAdapter cableListAdapter=new CableListAdapter();
            cableListAdapter.notifyDataSetChanged();
            WindowCableListView.setAdapter(cableListAdapter);
            WindowCableListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (cablesets.size() > 0) {
                                for (SwipeListLayout s : cablesets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    cablesets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            addNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNodeDialogShow(marker.getPosition(),"WellWindow",project.getProjectId());
                }
            });
            addCable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectCableDialogShow(nodeCacheList,"WellWindow");
                }
            });
            sendNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodeAndcableCancle(gJiaoX.getGjxname());
                    updateAnnotation(gJiaoX.getGjxname(),"LIGHTBOX");
                    markLigature();
                }
            });
            cancelNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodelist.removeAll(nodelist);
                    cablelist.removeAll(cablelist);
                    nodeCacheList.removeAll(nodeCacheList);
                    cableCacheList.removeAll(cableCacheList);
                    if(curShowWindowMarker!=null){
                        curShowWindowMarker.hideInfoWindow();
                    }
                }
            });
            gjjxjb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    gJiaoX.setFqdjibie(gjjxjb.getSelectedItem().toString().trim());
                    gJiaoX.save();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            gjjxlx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    gJiaoX.setGjxlb(gjjxlx.getSelectedItem().toString().trim());
                    gJiaoX.save();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else if(s.indexOf("引上点")!=-1){
            String x=marker.getTitle().trim();
            x=x.replace("引上点","");
            windowname=x;
            final YinShangDian yinShangDian=DataSupport.where("ysdname=?",x).find(YinShangDian.class).get(0);
            //初始化上一节点的list数据
            initMakerNodeList(yinShangDian.getYsdname());
            //初始化已当前标记为结束点的光缆段，list数据
            initMakerCableList(yinShangDian.getYsdname());
            TextView ysdname=(TextView) view.findViewById(R.id.ysd_name_window);
            TextView jingdu=(TextView)view.findViewById(R.id.ysd_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.ysd_weidu_window);

            WindowListView=(ListView)view.findViewById(R.id.ysd_node_name_list_window);
            WindowCableListView=(ListView)view.findViewById(R.id.ysd_cable_name_list_window);
            addNode=(Button)view.findViewById(R.id.add_node_ysd_window);
            addCable=(Button)view.findViewById(R.id.add_cable_ysd_window);
            cancelNode=(Button)view.findViewById(R.id.cancel_node_window);
            sendNode=(Button)view.findViewById(R.id.send_node_window);

            ysdname.setText(yinShangDian.getYsdname());
            jingdu.setText(yinShangDian.getLongitude());
            weidu.setText(yinShangDian.getLatitude());
            //listview(上一节点) 的数据设置以及删除
            ListAdapter listAdapter= new ListAdapter();
            listAdapter.notifyDataSetChanged();
            WindowListView.setAdapter(listAdapter);
            WindowListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (sets.size() > 0) {
                                for (SwipeListLayout s : sets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    sets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            CableListAdapter cableListAdapter=new CableListAdapter();
            cableListAdapter.notifyDataSetChanged();
            WindowCableListView.setAdapter(cableListAdapter);
            WindowCableListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (cablesets.size() > 0) {
                                for (SwipeListLayout s : cablesets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    cablesets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            addNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNodeDialogShow(marker.getPosition(),"WellWindow",project.getProjectId());
                }
            });
            addCable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectCableDialogShow(nodeCacheList,"WellWindow");
                }
            });
            sendNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodeAndcableCancle(yinShangDian.getYsdname());
                    updateAnnotation(yinShangDian.getYsdname(),"UPPOINT");
                    markLigature();
                }
            });
            cancelNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodelist.removeAll(nodelist);
                    cablelist.removeAll(cablelist);
                    nodeCacheList.removeAll(nodeCacheList);
                    cableCacheList.removeAll(cableCacheList);
                    if(curShowWindowMarker!=null){
                        curShowWindowMarker.hideInfoWindow();
                    }
                }
            });
        }else if(s.indexOf("guaqiang")!=-1){
            String x=marker.getTitle();
            x=x.replace("guaqiang","");
            windowname=x;
            final GuaQiang guaQiang=DataSupport.where("gqname=?",x).find(GuaQiang.class).get(0);
            //初始化上一节点的list数据
            initMakerNodeList(guaQiang.getGqname());
            //初始化已当前标记为结束点的光缆段，list数据
            initMakerCableList(guaQiang.getGqname());
            TextView gqname=(TextView) view.findViewById(R.id.gq_name_window);
            TextView jingdu=(TextView)view.findViewById(R.id.gq_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.gq_weidu_window);
            WindowListView=(ListView)view.findViewById(R.id.guaqiang_node_name_list_window);
            WindowCableListView=(ListView)view.findViewById(R.id.guaqiang_cable_name_list_window);
            addNode=(Button)view.findViewById(R.id.add_node_guaqiang_window);
            addCable=(Button)view.findViewById(R.id.add_cable_guaqiang_window);
            cancelNode=(Button)view.findViewById(R.id.cancel_node_window);
            sendNode=(Button)view.findViewById(R.id.send_node_window);
            gqname.setText(guaQiang.getGqname());
            jingdu.setText(guaQiang.getLongitude());
            weidu.setText(guaQiang.getLatitude());

            //listview(上一节点) 的数据设置以及删除
            ListAdapter listAdapter= new ListAdapter();
            listAdapter.notifyDataSetChanged();
            WindowListView.setAdapter(listAdapter);
            WindowListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (sets.size() > 0) {
                                for (SwipeListLayout s : sets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    sets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            CableListAdapter cableListAdapter=new CableListAdapter();
            cableListAdapter.notifyDataSetChanged();
            WindowCableListView.setAdapter(cableListAdapter);
            WindowCableListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (cablesets.size() > 0) {
                                for (SwipeListLayout s : cablesets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    cablesets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            addNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNodeDialogShow(marker.getPosition(),"WellWindow",project.getProjectId());
                }
            });
            addCable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectCableDialogShow(nodeCacheList,"WellWindow");
                }
            });
            sendNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodeAndcableCancle(guaQiang.getGqname());
                    updateAnnotation(guaQiang.getGqname(),"WALLHANG");
                    markLigature();
                }
            });
            cancelNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodelist.removeAll(nodelist);
                    cablelist.removeAll(cablelist);
                    nodeCacheList.removeAll(nodeCacheList);
                    cableCacheList.removeAll(cableCacheList);
                    if(curShowWindowMarker!=null){
                        curShowWindowMarker.hideInfoWindow();
                    }
                }
            });

        }else if(s.indexOf("jietouhe")!=-1){
            String x=marker.getTitle();
            x=x.replace("jietouhe","");
            windowname=x;
            final JieTouHe jieTouHe=DataSupport.where("jthname=?",x).find(JieTouHe.class).get(0);
            //初始化上一节点的list数据
            initMakerNodeList(jieTouHe.getJthname());
            //初始化已当前标记为结束点的光缆段，list数据
            initMakerCableList(jieTouHe.getJthname());
            TextView jthname=(TextView)view.findViewById(R.id.jth_name_window);
            TextView jingdu=(TextView)view.findViewById(R.id.jth_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.jth_weidu_window);
            WindowListView=(ListView)view.findViewById(R.id.jth_node_name_list_window);
            WindowCableListView=(ListView)view.findViewById(R.id.jth_cable_name_list_window);
            addNode=(Button)view.findViewById(R.id.add_node_jth_window);
            addCable=(Button)view.findViewById(R.id.add_cable_jth_window);
            cancelNode=(Button)view.findViewById(R.id.cancel_node_window);
            sendNode=(Button)view.findViewById(R.id.send_node_window);
            jthname.setText(jieTouHe.getJthname());
            jingdu.setText(jieTouHe.getLongitude());
            weidu.setText(jieTouHe.getLatitude());
            //listview(上一节点) 的数据设置以及删除
            ListAdapter listAdapter= new ListAdapter();
            listAdapter.notifyDataSetChanged();
            WindowListView.setAdapter(listAdapter);
            WindowListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (sets.size() > 0) {
                                for (SwipeListLayout s : sets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    sets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            CableListAdapter cableListAdapter=new CableListAdapter();
            cableListAdapter.notifyDataSetChanged();
            WindowCableListView.setAdapter(cableListAdapter);
            WindowCableListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                        case SCROLL_STATE_TOUCH_SCROLL:
                            if (cablesets.size() > 0) {
                                for (SwipeListLayout s : cablesets) {
                                    s.setStatus(SwipeListLayout.Status.Close, true);
                                    cablesets.remove(s);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
            });

            addNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNodeDialogShow(marker.getPosition(),"WellWindow",project.getProjectId());
                }
            });
            addCable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectCableDialogShow(nodeCacheList,"WellWindow");
                }
            });
            sendNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodeAndcableCancle(jieTouHe.getJthname());
                    updateAnnotation(jieTouHe.getJthname(),"JOINTBOX");
                    markLigature();
                }
            });
            cancelNode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nodelist.removeAll(nodelist);
                    cablelist.removeAll(cablelist);
                    nodeCacheList.removeAll(nodeCacheList);
                    cableCacheList.removeAll(cableCacheList);
                    if(curShowWindowMarker!=null){
                        curShowWindowMarker.hideInfoWindow();
                    }
                }
            });

        }
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

    private void toastMarkt(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    LBSTraceClient traceClient=null;
    TraceOverlay traceOverlay;

    @Override
    public void onTraceStatus(List<TraceLocation> list, List<LatLng> list1, String s) {
       //TODO 轨迹纠偏

        //将得到的轨迹显示在地图上
        if(traceOverlay!=null){
            traceOverlay.remove();
        }
        traceOverlay=new TraceOverlay(aMap,list1);
        traceOverlay.zoopToSpan();

    }
    private void startTrace(){
        if(traceClient==null){
            traceClient=LBSTraceClient.getInstance(this);
        }
        traceClient.startTrace(this);
    }
    // 保存所有的标记的点到数据库，已做为管道连接的依据
    private void CreateMark(LatLng latlngss, String pronames, long createtime,String names
            ,String pointtype,String projectId,String orgId){
        PointSet pointSet=new PointSet();
        pointSet.setProname(pronames);
        pointSet.setMarkname(names);
        pointSet.setLatitude(Double.toString(latlngss.latitude));
        pointSet.setLongitude(Double.toString(latlngss.longitude));
        pointSet.setCratetime(createtime);
        pointSet.setStautus(0);
        pointSet.setPointType(pointtype);
        pointSet.setProjrctId(projectId);
        pointSet.setOrgId(orgId);
        pointSet.save();
    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return nodelist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return nodelist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View view, ViewGroup arg2) {
            if(nodelist.size()<=0){
                if(view==null){
                    view = getLayoutInflater().inflate(R.layout.slip_list_item, null);

                }
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            }else {
                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.slip_list_item, null);
                }
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_name.setText(nodelist.get(arg0));
                final SwipeListLayout sll_main = (SwipeListLayout) view
                        .findViewById(R.id.sll_main);
                TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
                TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
                sll_main.setOnSwipeStatusListener(new DesignRouteActivity.MyOnSlipStatusListener(
                        sll_main));
                tv_top.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        sll_main.setStatus(SwipeListLayout.Status.Close, true);
                        String str = nodelist.get(arg0);

                        nodelist.remove(arg0);
                        nodelist.add(0, str);
                        notifyDataSetChanged();
                    }
                });
                tv_delete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String str = nodelist.get(arg0);

                        NodeCache nodeCache=nodeCacheList.get(arg0);

                        Pipeline pipelineList=DataSupport.where("startnodename=? and projectId=? and endnodename=?"
                                ,nodeCache.getStartnodename(),project.getProjectId(),windowname).find(Pipeline.class).get(0);
                        removeConduit.add(pipelineList.getConduitId());

                        //删除上一节点，同时删除已上一节点为起点的光缆
                        List<CableResource> cableResourceList=DataSupport.where("startPointName=? and projectId=?",
                                str,project.getProjectId()).find(CableResource.class);
                        if(cableResourceList.size()>0){
                            for(int i=0;i<cableResourceList.size();i++){
                                CableResource cableResource=cableResourceList.get(i);
                                removeCableSegment.add(cableResource.getCableSegmentId());
                            }
                        }


                        sll_main.setStatus(SwipeListLayout.Status.Close, true);

                        /*删除上一节点的时候
                        1.删除缓存的节点list数据
                        2.删除listview中的list
                        3.删除已该节点为开始，以当前标记为结束点的光缆段*/

                        List<CableCache> cacheList=new ArrayList<>();
                        if(cableCacheList.size()>0){
                           for(int i=0;i<cableCacheList.size();i++){
                               CableCache cableCache=cableCacheList.get(i);
                               if(cableCache.getCableorigin().equals(nodelist.get(arg0))){
                                   cacheList.add(cableCache);
//                                   cableCacheList.remove(cableCache);
                               }
                           }
                            cableCacheList.removeAll(cacheList);
                        }

                        cablelist.removeAll(cablelist);

                        if(cableCacheList.size()>0){
                            for(int i=0;i<cableCacheList.size();i++){
                                CableCache cableCache=cableCacheList.get(i);

                                cablelist.add(cableCache.getCablename());
                                notifyDataSetChanged();
                                WindowCableListView.invalidateViews();
                            }
                        }
                        nodeCacheList.remove(arg0);
                        nodelist.remove(arg0);
                        notifyDataSetChanged();
                    }
                });
            }

            return view;
        }
    }

    class CableMyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public CableMyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (cablesets.size() > 0) {
                    for (SwipeListLayout s : cablesets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        cablesets.remove(s);
                    }
                }
                cablesets.add(slipListLayout);
            } else {
                if (cablesets.contains(slipListLayout))
                    cablesets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }
    }

    class CableListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cablelist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return cablelist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View view, ViewGroup arg2) {
            if(cablelist.size()<=0){
                if(view==null){
                    view = getLayoutInflater().inflate(R.layout.slip_list_item, null);

                }
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            }else {
                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.slip_list_item, null);
                }
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_name.setText(cablelist.get(arg0));
                final SwipeListLayout sll_main = (SwipeListLayout) view
                        .findViewById(R.id.sll_main);
                TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
                TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
                sll_main.setOnSwipeStatusListener(new DesignRouteActivity.CableMyOnSlipStatusListener(
                        sll_main));
                tv_delete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        sll_main.setStatus(SwipeListLayout.Status.Close, true);

                       /*/// 气泡在列表删除一个item时，删除相对应的管道和光缆段

                        }*/

                        CableCache cableCache=cableCacheList.get(arg0);
                        CableResource cableResourceList=DataSupport.where("startPointName=? and projectId=? and endPointName=? and childhole=? and hole=?",
                                cableCache.getCableorigin(),project.getProjectId(),windowname,cableCache.getChildhole(),cableCache.getHole())
                                .find(CableResource.class).get(0);
                        removeCableSegment.add(cableResourceList.getCableSegmentId());

                       cableCacheList.remove(arg0);
                       cablelist.remove(arg0);
                       notifyDataSetChanged();
                    }
                });
            }

            return view;
        }
    }

    //初始化上一节点的list数据
    private void initMakerNodeList(String wellname){
        List<Pipeline> pipelineList=DataSupport.where("endnodename=? and projectId=?",wellname,project.getProjectId())
                .find(Pipeline.class);
        if(pipelineList.size()>0){
            for(int i=0;i<pipelineList.size();i++){
                Pipeline pipeline=pipelineList.get(i);
                nodelist.add(pipeline.getStartnodename());
                NodeCache nodeCache=new NodeCache();
                nodeCache.setStartnodename(pipeline.getStartnodename());
                nodeCache.setPipelinehole(pipeline.getPipelinehole());
                nodeCache.setPipelinetype(pipeline.getPipelinetype());
                nodeCache.setPipelinespecifica(pipeline.getPipelinespecifica());
                nodeCache.setPipelinename(pipeline.getPipelinename());
                nodeCacheList.add(nodeCache);
            }
        }
    }
    private void initMakerCableList(String wellname){
        //初始化已当前标记为结束点的光缆段，list数据
        List<CableResource> cableResourceList=DataSupport.where("endPointName=?",wellname).find(CableResource.class);
        if(cableResourceList.size()>0){
            for(int i=0;i<cableResourceList.size();i++){
                CableResource cableResource=cableResourceList.get(i);
                cablelist.add(cableResource.getCablename());

                CableCache cableCache =new CableCache();
                cableCache.setHole(cableResource.getHole());
                cableCache.setChildhole(cableResource.getChildhole());
                cableCache.setCableorigin(cableResource.getStartPointName());
                cableCache.setCablename(cableResource.getCablename());
                cableCacheList.add(cableCache);
            }
        }
    }

    //保存上一节点的信息之后根据(上一节点)连线
    private void nodePolyline(String endnodename){
        List<Pipeline> polylinesList=DataSupport.where("endnodename=? and projectId=? and orgId=?"
                ,endnodename,project.getProjectId(),project.getOrgId())
                .find(Pipeline.class);

        if(polylinesList.size()>0){
            for(int i=0;i<polylinesList.size();i++){
                Pipeline pipeline=polylinesList.get(i);

                PolylineOptions polylineOptions=new PolylineOptions();
                polylineOptions.add(
                        new LatLng(Double.valueOf(pipeline.getStartLatitude()),Double.valueOf(pipeline.getStartLongitude())),
                        new LatLng(Double.valueOf(pipeline.getEndLatitude()),Double.valueOf(pipeline.getEndLongitude()))
                ).color(R.color.colorPrimaryDark).width(6.0f);
                polylines=aMap.addPolyline(polylineOptions);
                polylineList.add(polylines);
            }
        }
    }
    private String conduitName;
    private void nodeAndcableCancle(String wellName){

        if(addConduitArray.size()>0){
            for(int i=0;i<addConduitArray.size();i++){
                NodeCache nodeCache=addConduitArray.get(i);

                //根据起点的名字在标记表获取信息(起点)
                PointSet startpointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                        nodeCache.getStartnodename(),project.getProjectId(),project.getOrgId())
                        .find(PointSet.class).get(0);

                //根据名字在标记表获取信息(终点)
                PointSet endpointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                        wellName,project.getProjectId(),project.getOrgId())
                        .find(PointSet.class).get(0);

                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("name",nodeCache.getPipelinename());//管道名
                stringMap.put("stand",nodeCache.getPipelinespecifica());//管道规格
                stringMap.put("type",nodeCache.getPipelinetype());//管道类型
                stringMap.put("length","20");//管道长度
                stringMap.put("holeNumber",nodeCache.getPipelinehole());//管道孔数
                stringMap.put("startPointName",nodeCache.getStartnodename());
                stringMap.put("startPointType",startpointSet.getPointType());
                stringMap.put("startLatitude",startpointSet.getLatitude());
                stringMap.put("startLongitude",startpointSet.getLongitude());
                stringMap.put("startPointId",startpointSet.getMarkId());
                stringMap.put("endPointName",endpointSet.getMarkname());
                stringMap.put("endPointType",endpointSet.getPointType());
                stringMap.put("endLatitude",endpointSet.getLatitude());
                stringMap.put("endLongitude",endpointSet.getLongitude());
                stringMap.put("endPointId",endpointSet.getMarkId());
                stringMap.put("addTime",String.valueOf(System.currentTimeMillis()));
                stringMap.put("projectId",project.getProjectId());
                stringMap.put("orgId",project.getOrgId());
                addConduit.add(stringMap);

            }
        }

        if(addCableSegmentArray.size()>0){
            for(int i=0;i<addCableSegmentArray.size();i++){
                CableCache cableCache=addCableSegmentArray.get(i);

                if(addConduitArray.size()>0){
                    for(int a=0;a<addConduitArray.size();a++){
                        NodeCache nodeCache=addConduitArray.get(a);
                        if(nodeCache.getStartnodename().equals(cableCache.getCableorigin())){
                             conduitName=nodeCache.getPipelinename();
                        }
                    }
                }

               /* //根据起点的名字获取管道的名字在管道表中
                Pipeline pipeline=DataSupport.where("projectId=? and startnodename=? and endnodename=?",
                        project.getProjectId(),cableCache.getCableorigin(),wellName)
                        .find(Pipeline.class).get(0);*/

                //根据起点的名字在标记表获取信息(起点)
                PointSet startpointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                        cableCache.getCableorigin(),project.getProjectId(),project.getOrgId())
                        .find(PointSet.class).get(0);//开始节点（上一节点）

                //根据名字在标记表获取信息(终点)
                PointSet endpointSet=DataSupport.where("markname=? and projrctId=? and orgId=?",
                        wellName,project.getProjectId(),project.getOrgId())
                        .find(PointSet.class).get(0);
                Map<String,String> map=new HashMap<>();
                map.put("cableName",cableCache.getCablename());
                map.put("conduitName",conduitName);
                map.put("useHole",cableCache.getHole());
                map.put("useSubHole",cableCache.getChildhole());
                map.put("length","20");
                map.put("startPointName",cableCache.getCableorigin());
                map.put("startPointType",startpointSet.getPointType());
                map.put("startLatitude",startpointSet.getLatitude());
                map.put("startLongitude",startpointSet.getLongitude());
                map.put("startPointId",startpointSet.getMarkId());
                map.put("endPointId",endpointSet.getMarkId());
                map.put("endPointName",endpointSet.getMarkname());
                map.put("endPointType",endpointSet.getPointType());
                map.put("endLatitude",endpointSet.getLatitude());
                map.put("endLongitude",endpointSet.getLongitude());
                map.put("addTime",String.valueOf(System.currentTimeMillis()));
                map.put("projectId",project.getProjectId());
                map.put("orgId",project.getOrgId());

                addCableSegment.add(map);
            }
        }

        if(removeConduit.size()>0){
            for(int i=0;i<removeConduit.size();i++){
                Pipeline pipeline=DataSupport.where("conduitId=?",removeConduit.get(i)).find(Pipeline.class).get(0);
                if(pipeline.isSaved()){
                    pipeline.delete();
                }
            }
        }



        if(removeCableSegment.size()>0){
            for(int i=0;i<removeCableSegment.size();i++){
                CableResource cableResource=DataSupport.where("cableSegmentId=?",removeCableSegment.get(i)).find(CableResource.class).get(0);
                if(cableResource.isSaved()){
                    cableResource.delete();
                }
            }
        }


        nodelist.removeAll(nodelist);
        nodeCacheList.removeAll(nodeCacheList);
        cablelist.removeAll(cablelist);
        cableCacheList.removeAll(cableCacheList);
        if(curShowWindowMarker!=null){
            curShowWindowMarker.hideInfoWindow();
        }

        if(polylineList.size()>0){
            for(int i=0;i<polylineList.size();i++){
                Polyline polyline=polylineList.get(i);
                polyline.remove();
            }
        }
        markLigature();
    }

    private void updatePointSet(String s,Marker arg0){
        PointSet pointSet=DataSupport.where("markname=? and projrctId=?"
                ,s,project.getProjectId()).find(PointSet.class).get(0);
        pointSet.setLatitude(Double.toString(arg0.getPosition().latitude));
        pointSet.setLongitude(Double.toString(arg0.getPosition().longitude));
        pointSet.save();


    }
    private void updateAnnotation(final String name, String updateType){

        if(updateType=="WELL"){
            PeopleWell peopleWellUpdate=DataSupport.where("wellname=? and projectId=?",name,project.getProjectId())
                    .find(PeopleWell.class).get(0);
            projectModel.volleyAnnotationUpdate(updateType, peopleWellUpdate.getAnnotationId(), peopleWellUpdate.getSpecifications(),
                    peopleWellUpdate.getLatitude(), peopleWellUpdate.getLongitude(), "", "", "",
                    addConduit,addCableSegment,removeConduit,removeCableSegment,
                    new RequestResultListener() {
                        @Override
                        public void onSuccess(JSONObject s) {
                            Log.i(TAG,"WELL update success="+s);
                            UpdateDataUtil.saveConduitAndCableSegment(s);
                            markLigature();
                        }

                        @Override
                        public void onError(String s) {
                            Log.i(TAG,"WELL update error="+s);
                        }
                    });
        }else if(updateType=="POLE"){
            Pole poleUpdate=DataSupport.where("polename=? and projectId=?",name,project.getProjectId()).find(Pole.class).get(0);

            projectModel.volleyAnnotationUpdate(updateType, poleUpdate.getAnnotationId(), "",
                    poleUpdate.getLatitude(), poleUpdate.getLongitude(), poleUpdate.getPoleheight(), poleUpdate.getFencha(), "",
                    addConduit,addCableSegment,removeConduit,removeCableSegment,
                    new RequestResultListener() {
                        @Override
                        public void onSuccess(JSONObject s) {
                            Log.i(TAG,"POLE update success="+s);
                            UpdateDataUtil.saveConduitAndCableSegment(s);
                            markLigature();
                        }

                        @Override
                        public void onError(String s) {
                            Log.i(TAG,"POLE update error="+s);
                        }
                    });

        }else if(updateType=="LIGHTBOX"){
            GJiaoX gJiaoXUpdate=DataSupport.where("gjxname=?",name).find(GJiaoX.class).get(0);
            projectModel.volleyAnnotationUpdate("LIGHTBOX", gJiaoXUpdate.getAnnotationId(), gJiaoXUpdate.getFqdjibie(),
                    gJiaoXUpdate.getLatitude(), gJiaoXUpdate.getLongitude(), "", "", gJiaoXUpdate.getGjxlb(),
                    addConduit,addCableSegment,removeConduit,removeCableSegment,
                    new RequestResultListener() {
                        @Override
                        public void onSuccess(JSONObject s) {
                            Log.i(TAG,"LIGHTBOX update success="+s);
                            UpdateDataUtil.saveConduitAndCableSegment(s);
                            markLigature();
                        }

                        @Override
                        public void onError(String s) {
                            Log.i(TAG,"LIGHTBOX update error="+s);
                        }
                    });
        }else if(updateType=="UPPOINT"){
            YinShangDian yinShangDianUpdate=DataSupport.where("ysdname=?",name).find(YinShangDian.class).get(0);
            projectModel.volleyAnnotationUpdate(updateType, yinShangDianUpdate.getAnnotationId(), "",
                    yinShangDianUpdate.getLatitude(), yinShangDianUpdate.getLongitude(), "", "", "",
                    addConduit,addCableSegment,removeConduit,removeCableSegment,
                    new RequestResultListener() {
                        @Override
                        public void onSuccess(JSONObject s) {
                            Log.i(TAG,"UPPOINT update success");
                            UpdateDataUtil.saveConduitAndCableSegment(s);
                            markLigature();
                        }

                        @Override
                        public void onError(String s) {
                            Log.i(TAG,"UPPOINT update error="+s);
                        }
                    });
        }else if(updateType=="WALLHANG"){
            GuaQiang guaQiangUpdate=DataSupport.where("gqname=?",name).find(GuaQiang.class).get(0);
            projectModel.volleyAnnotationUpdate(updateType, guaQiangUpdate.getAnnotationId(), "",
                    guaQiangUpdate.getLatitude(), guaQiangUpdate.getLongitude(), "", "", "",
                    addConduit,addCableSegment,removeConduit,removeCableSegment,
                    new RequestResultListener() {
                        @Override
                        public void onSuccess(JSONObject s) {
                            Log.i(TAG,"WALLHANG update success");
                            UpdateDataUtil.saveConduitAndCableSegment(s);
                            markLigature();
                        }

                        @Override
                        public void onError(String s) {
                            Log.i(TAG,"WALLHANG update error");
                        }
                    });
        }else if(updateType=="JOINTBOX"){
            JieTouHe jieTouHeUpdate=DataSupport.where("jthname=?",name).find(JieTouHe.class).get(0);
            projectModel.volleyAnnotationUpdate(updateType, jieTouHeUpdate.getAnnotationId(), "",
                    jieTouHeUpdate.getLatitude(), jieTouHeUpdate.getLongitude(), "", "", "",
                    addConduit,addCableSegment,removeConduit,removeCableSegment,
                    new RequestResultListener() {
                        @Override
                        public void onSuccess(JSONObject s) {
                            Log.i(TAG,"JOINTBOX update success");
                            UpdateDataUtil.saveConduitAndCableSegment(s);
                            markLigature();
                        }

                        @Override
                        public void onError(String s) {
                            Log.i(TAG,"JOINTBOX update error");

                        }
                    });
        }

        removeCableSegment.removeAll(removeCableSegment);
        removeConduit.removeAll(removeConduit);
        addConduit.removeAll(addConduit);
        addCableSegment.removeAll(addCableSegment);
    }
}