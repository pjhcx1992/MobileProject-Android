package com.ck.mobileoperations.busniess.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps.AMap;
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
import com.ck.mobileoperations.busniess.design.DesignRouteActivity;
import com.ck.mobileoperations.busniess.design.model.ProjectModelImpl;
import com.ck.mobileoperations.entity.GJiaoX;
import com.ck.mobileoperations.entity.GuaQiang;
import com.ck.mobileoperations.entity.JieTouHe;
import com.ck.mobileoperations.entity.PeopleWell;
import com.ck.mobileoperations.entity.Pipeline;
import com.ck.mobileoperations.entity.PointSet;
import com.ck.mobileoperations.entity.Pole;
import com.ck.mobileoperations.entity.ProName;
import com.ck.mobileoperations.entity.YinShangDian;
import com.ck.mobileoperations.utils.RequestResultListener;
import com.ck.mobileoperations.widget.CreateMarkDialog;
import com.ck.mobileoperations.widget.DianGanDialog;
import com.ck.mobileoperations.widget.GuaQiangDialog;
import com.ck.mobileoperations.widget.GuangJiaoXiangDialog;
import com.ck.mobileoperations.widget.JieTouHeDialog;
import com.ck.mobileoperations.widget.RenShouJingDialog;
import com.ck.mobileoperations.widget.YinShangDianDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.ck.mobileoperations.R.drawable.diangan_mark;
import static com.ck.mobileoperations.R.drawable.gjjx_mark;
import static com.ck.mobileoperations.R.drawable.gq_mark;
import static com.ck.mobileoperations.R.drawable.jth_mark;
import static com.ck.mobileoperations.R.drawable.rsj_mark;
import static com.ck.mobileoperations.R.drawable.ysd_mark;


/**
 * Created by CK on 2017/12/18.
 */

public class CheckRouteActivity extends Activity implements AMap.OnMapClickListener,
        AMap.OnMapLongClickListener,AMap.OnCameraChangeListener,AMap.OnMapTouchListener
        ,AMap.InfoWindowAdapter,TraceStatusListener{
    private MapView mMapView = null;
    private AMap aMap;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private double x;
    private double y;
    private AMapLocation aMapLocation;
    private Marker curShowWindowMarker;
    private String proname;
    private String orgId;
    private Polyline polylines;
    private List<Polyline> polylineList=new ArrayList<>();
    private ProjectModelImpl projectModel;
    private RequestQueue mQueue;
    @Bind(R.id.cancel_plan)
    TextView  cancelPlan;
    /*@Bind(R.id.save_plan)
    TextView savePlan;*/
    @Bind(R.id.project_name)
    TextView projectName;
    @Bind(R.id.check_sava)
    Button cheecksave;
    @Bind(R.id.check_cancel)
    Button checkcancel;

    CreateMarkDialog createMarkDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_route);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        init();
        initLocation();
        startLocation();//开始定位

        mQueue = Volley.newRequestQueue(CheckRouteActivity.this);
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


        cheecksave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setTitle("审核")
                        .setMessage("是否通过该线路设计？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final ProName proName=DataSupport.where("proname=? and orgId=?",proname,orgId).find(ProName.class).get(0);
                                if(proName.getStatus().equals("0")){
                                    toastMarkt("该项目创建还未完成，无法进行此操作!");
                                }else {
                                    projectModel.volleyProjectStatus(proName.getProjectId(), "3", new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            proName.setCheckstatus("3");
                                            proName.save();
                                            toastMarkt("项目已审核通过!");
                                        }

                                        @Override
                                        public void onError(String s) {
                                            toastMarkt("网络请求错误，请稍后重试!");
                                        }
                                    });

                                }


                            }
                        }).show();

            }
        });

        checkcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setTitle("审核")
                        .setMessage("是否驳回该线路设计？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                final ProName proName=DataSupport.where("proname=? and orgId=?",proname,orgId).find(ProName.class).get(0);
                                if(proName.getStatus().equals("0")){
                                    toastMarkt("该项目创建还未完成，无法进行此操作!");
                                }else {
                                    projectModel.volleyProjectStatus(proName.getProjectId(), "2", new RequestResultListener() {
                                        @Override
                                        public void onSuccess(JSONObject s) {
                                            proName.setCheckstatus("2");
                                            proName.save();
                                            toastMarkt("项目已审核驳回!");
                                        }

                                        @Override
                                        public void onError(String s) {
                                            toastMarkt("网络请求错误，请稍后重试!");
                                        }
                                    });

                                }
                            }
                        }).show();
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
//        startTrace();

        markLigature();


    }
    private String selectorganizationId;
    private ProName project;
    private void SetTheTag(){
        //TODO (4) 设置数据库保存的标记到地图
        proname=getIntent().getStringExtra("proname");
        selectorganizationId=getIntent().getStringExtra("orgId");
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
        locationOption.setGpsFirst(true);        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(false);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差

        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(10000);

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



//       // 定义 Marker拖拽的监听
//       AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {
//
//           // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
//           // 这个位置可能与拖动的之前的marker位置不一样。
//           // marker 被拖动的marker对象。
//           @Override
//           public void onMarkerDragStart(Marker arg0) {
//               System.out.println("kaishi==="+arg0.getPosition().latitude);
//
//           }
//           // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
//           // 这个位置可能与拖动的之前的marker位置不一样。
//           // marker 被拖动的marker对象。
//           @Override
//           public void onMarkerDragEnd(Marker arg0) {
//               //TODO (3)标记移动结束之后更新，新的经纬度信息到数据库
//               final LatLng latLng=arg0.getPosition();
//
//               String s=arg0.getTitle().trim();
//               if(s.indexOf("人手井")!=-1){
//                   s=s.replace("人手井","");
//                   PeopleWell peopleWell=DataSupport.where("wellname=?",s).find(PeopleWell.class).get(0);
//                   peopleWell.setLatitude(Double.toString(arg0.getPosition().latitude));
//                   peopleWell.setLongitude(Double.toString(arg0.getPosition().longitude));
//                   peopleWell.save();
//               }else if (s.indexOf("电杆")!=-1){
//                   s=s.replace("电杆","");
//                  Pole pole=DataSupport.where("polename=?",s).find(Pole.class).get(0);
//                  pole.setLatitude(Double.toString(arg0.getPosition().latitude));
//                  pole.setLongitude(Double.toString(latLng.longitude));
//                  pole.save();
//               }else if(s.indexOf("光交接箱")!=-1){
//                   s=s.replace("光交接箱","");
//                   GJiaoX gJiaoX=DataSupport.where("gjxname=?",s).find(GJiaoX.class).get(0);
//                   gJiaoX.setLatitude(Double.toString(latLng.latitude));
//                   gJiaoX.setLongitude(Double.toString(latLng.longitude));
//                   gJiaoX.save();
//               }else if(s.indexOf("引上点")!=-1){
//                   s=s.replace("引上点","");
//                   YinShangDian yinShangDian=DataSupport.where("ysdname=?",s).find(YinShangDian.class).get(0);
//                   yinShangDian.setLongitude(Double.toString(latLng.longitude));
//                   yinShangDian.setLatitude(Double.toString(latLng.latitude));
//                   yinShangDian.save();
//               }else if(s.indexOf("guaqiang")!=-1){
//                   s=s.replace("guaqiang","");
//                   GuaQiang guaQiang=DataSupport.where("gqname=?",s).find(GuaQiang.class).get(0);
//                   guaQiang.setLongitude(Double.toString(latLng.longitude));
//                   guaQiang.setLatitude(Double.toString(latLng.latitude));
//                   guaQiang.save();
//
//               }else if(s.indexOf("jietouhe")!=-1){
//                   s=s.replace("jietouhe","");
//                   JieTouHe jieTouHe=DataSupport.where("jthname=?",s).find(JieTouHe.class).get(0);
//                   jieTouHe.setLongitude(Double.toString(latLng.longitude));
//                   jieTouHe.setLatitude(Double.toString(latLng.latitude));
//                   jieTouHe.save();
//               }
//
//
//           }
//
//           // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
//           // 这个位置可能与拖动的之前的marker位置不一样。
//           // marker 被拖动的marker对象。
//           @Override
//           public void onMarkerDrag(Marker arg0) {
//               // TODO Auto-generated method stub
//               System.out.println("guochengzhong==="+arg0.getPosition().latitude);
//           }
//       };
//       aMap.setOnMarkerDragListener(markerDragListener);// 绑定marker拖拽事件

       aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式

       AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
           // marker 对象被点击时回调的接口
           // 返回 true 则表示接口已响应事件，否则返回false
           @Override
           public boolean onMarkerClick(final Marker marker) {

               curShowWindowMarker=marker;

               return false;
           }

       };
      // 绑定 Marker 被点击事件
       aMap.setOnMarkerClickListener(markerClickListener);


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
    //点解设置标记弹出选择标记
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
    private String rsjsize;
    AdapterView.OnItemSelectedListener GJJXonItemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            rsjsize=renShouJingDialog.rsjSize.getSelectedItem().toString().trim();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener rsjOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
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


                            final PeopleWell peopleWell=new PeopleWell();
                            peopleWell.setCategory(1);
                            peopleWell.setLatLng(latLngs);
                            peopleWell.setLatitude(Double.toString(latLngs.latitude));
                            peopleWell.setLongitude(Double.toString(latLngs.longitude));
                            peopleWell.setWellname(renShouJingDialog.rsjName.getText().toString().trim());
                            peopleWell.setTotheproject(proname);

                            peopleWell.setSpecifications(rsjsize);
                            peopleWell.save();
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

    DianGanDialog dianGanDialog;
    private void DianGan(LatLng latLng){

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
                case R.id.diangan_send_mark:
                    dianGanDialog.dismiss();
                    if(dianGanDialog.dianganName.getText().toString().trim().length()<1){
                        Toast.makeText(getBaseContext(),"电杆名不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        if(dianGanDialog.dianganName.getText().toString().trim().contains("#")){

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
                            pole.save();

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
                            gJiaoX.save();

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
                            yinShangDian.save();

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
                case R.id.gq_send_mark:
                    guaQiangDialog.dismiss();
                    if(guaQiangDialog.gqname.getText().toString().trim().length()<1){
                        toastMarkt("挂墙名不能为空");
                    }else {
                        if(guaQiangDialog.gqname.getText().toString().trim().contains(proname) &&
                                guaQiangDialog.gqname.getText().toString().trim().contains("挂墙")){
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
                            guaQiang.save();

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
                            jieTouHe.save();

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

    public void render(Marker marker, View view) {


        //如果想修改自定义Infow中内容，请通过view找到它并修改
        String s=marker.getTitle().trim();
        if(s.indexOf("人手井")!=-1){
            //获取到人手井name
            String x=marker.getTitle().trim();
            x=x.replace("人手井","").trim();
            final PeopleWell peopleWell=DataSupport.where("wellname=?",x).find(PeopleWell.class).get(0);
            TextView rsjname=(TextView) view.findViewById(R.id.rsj_name_window);
            TextView jingdu=(TextView) view.findViewById(R.id.jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.weidu_window);
            final Spinner guige=(Spinner) view.findViewById(R.id.rsj_size_window);
            rsjname.setText(peopleWell.getWellname());
            jingdu.setText(peopleWell.getLongitude());
            weidu.setText(peopleWell.getLatitude());
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

        }else if(s.indexOf("当前位置")!=-1){
            /*TextView qu=(TextView)view.findViewById(R.id.dqwz_qu_window);
            TextView dizhi=(TextView)view.findViewById(R.id.dqwz_dizhi_window);*/
            TextView jingdu=(TextView)view.findViewById(R.id.dqwz_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.dqwz_weidu_window);
            jingdu.setText(Double.toString(marker.getPosition().longitude));
            weidu.setText(Double.toString(marker.getPosition().latitude));
            /*dizhi.setText("杭州市西湖区文三路马塍路");
            qu.setText("西湖区");*/
        }else if(s.indexOf("电杆")!=-1){

            String x=marker.getTitle().trim();
            x=x.replace("电杆","").trim();
            final Pole pole=DataSupport.where("polename=?",x).find(Pole.class).get(0);
            final EditText dgname=(EditText)view.findViewById(R.id.diangan_name_window);
            TextView dgjingdu=(TextView) view.findViewById(R.id.diangan_jingdu_window);
            TextView dgweidu=(TextView)view.findViewById(R.id.diangan_weidu_window);
            final Spinner dgheight=(Spinner)view.findViewById(R.id.diangan_height_window);
            final Spinner dgfencha=(Spinner)view.findViewById(R.id.diangan_fencha_window);
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
        }else if(s.indexOf("光交接箱")!=-1){
            String x=marker.getTitle().trim();
            x=x.replace("光交接箱","");
            final GJiaoX gJiaoX=DataSupport.where("gjxname=?",x).find(GJiaoX.class).get(0);

            EditText gjjxname=(EditText)view.findViewById(R.id.gjjx_name_window);
            final Spinner gjjxjb=(Spinner)view.findViewById(R.id.gjjx_jibie_window);
            final Spinner gjjxlx=(Spinner)view.findViewById(R.id.gjjx_leixing_window);
            TextView jingdiu=(TextView)view.findViewById(R.id.gjjx_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.gjjx_weidu_window);

            gjjxname.setText(gJiaoX.getGjxname());
            setSpinnerItemSelectedByValue(gjjxjb,gJiaoX.getFqdjibie());
            setSpinnerItemSelectedByValue(gjjxlx,gJiaoX.getGjxlb());
            jingdiu.setText(gJiaoX.getLongitude());
            weidu.setText(gJiaoX.getLatitude());

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
            YinShangDian yinShangDian=DataSupport.where("ysdname=?",x).find(YinShangDian.class).get(0);
            EditText ysdname=(EditText)view.findViewById(R.id.ysd_name_window);
            TextView jingdu=(TextView)view.findViewById(R.id.ysd_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.ysd_weidu_window);

            ysdname.setText(yinShangDian.getYsdname());
            jingdu.setText(yinShangDian.getLongitude());
            weidu.setText(yinShangDian.getLatitude());
        }else if(s.indexOf("guaqiang")!=-1){
            String x=marker.getTitle();
            x=x.replace("guaqiang","");
            GuaQiang guaQiang=DataSupport.where("gqname=?",x).find(GuaQiang.class).get(0);

            EditText gqname=(EditText)view.findViewById(R.id.gq_name_window);
            TextView jingdu=(TextView)view.findViewById(R.id.gq_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.gq_weidu_window);

            gqname.setText(guaQiang.getGqname());
            jingdu.setText(guaQiang.getLongitude());
            weidu.setText(guaQiang.getLatitude());

        }else if(s.indexOf("jietouhe")!=-1){
            String x=marker.getTitle();
            x=x.replace("jietouhe","");
            JieTouHe jieTouHe=DataSupport.where("jthname=?",x).find(JieTouHe.class).get(0);

            EditText jthname=(EditText)view.findViewById(R.id.jth_name_window);
            TextView jingdu=(TextView)view.findViewById(R.id.jth_jingdu_window);
            TextView weidu=(TextView)view.findViewById(R.id.jth_weidu_window);
            jthname.setText(jieTouHe.getJthname());
            jingdu.setText(jieTouHe.getLongitude());
            weidu.setText(jieTouHe.getLatitude());

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
}