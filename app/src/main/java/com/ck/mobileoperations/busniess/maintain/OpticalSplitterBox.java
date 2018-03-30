package com.ck.mobileoperations.busniess.maintain;

import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.login.LoginActivity;
import com.ck.mobileoperations.busniess.maintain.bluetooth.BlueTooth_Devices;
import com.ck.mobileoperations.busniess.maintain.bluetooth.MyService;
import com.ck.mobileoperations.busniess.maintain.vooptical.CaptureActivity;
import com.ck.mobileoperations.busniess.maintain.vooptical.JumpOperationDialog;
import com.ck.mobileoperations.entity.JumpOperation;
import com.ck.mobileoperations.entity.OpticalFiberJumper;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenkai on 2018/2/8.
 */

public class OpticalSplitterBox extends Activity{
    private static final int LOCATION_CODE = 1;
    public static String DEVICE_NAME = "DEVICE_NAME";
    public static String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String DEVICE_RSSI = "DEVICE_RSSI";
    private String mDeviceName;
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private String mDeviceAddress;
    private String mRssi;
    private String rev_str = "";
    private Handler mhandler = new Handler();
    private static MyService mService;
    private final static int REQUEST_ENABLE_BT = 1;
    private boolean isConnect=false;//蓝牙是否连接
    private boolean isJumpOperation=false;//是不是跳纤的操作
    private int jumpstatus=0;//选择跳纤的起点和终点
    @Bind(R.id.create_patical_box)
    Button button;
    @Bind(R.id.name)
    TextView opticalName;
    @Bind(R.id.type)TextView opticalType;
    @Bind(R.id.address)TextView optocalAdsress;
    @Bind(R.id.out_image) ImageView outImage;
    @Bind(R.id.verify_message)Button verifyMessage;
    @Bind(R.id.jump_operation)Button jumpOperation;
    private ListView mRecyclerView;
    private OpticalBoxItemAdapter opticalBoxItemAdapter;
    private String name;
    private String address;
    private String type;
    private int count;
    private List<String> order=new ArrayList<>();

    private BluetoothAdapter mBluetoothAdapter;
    private static BluetoothGattCharacteristic target_chara = null;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optical_box_layout);
        ButterKnife.bind(this);
        mRecyclerView = (ListView) findViewById(R.id.duanzi_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(OpticalSplitterBox.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {// 没有权限。
                    if (ActivityCompat.shouldShowRequestPermissionRationale(OpticalSplitterBox.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                        ActivityCompat.requestPermissions(OpticalSplitterBox.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                    } else {
                        // 申请授权。
                        ActivityCompat.requestPermissions(OpticalSplitterBox.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);

                    }
                }else {
                    Intent intent=new Intent(getBaseContext(), CaptureActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        outImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();

    }
    private void init(){

        name=getIntent().getStringExtra("name");
        address=getIntent().getStringExtra("address");
        type=getIntent().getStringExtra("type");

        opticalName.setText(name);
        if(type==null){
            opticalType.setText("");
        }else{
            opticalType.setText(type+"芯");

            count=Integer.valueOf(type);
            int number=count/12;
            for(int i=0;i<number;i++){
                order.add(String.valueOf((char)(65+i)));
            }

            opticalBoxItemAdapter=new OpticalBoxItemAdapter(this,order,name);
            mRecyclerView.setAdapter(opticalBoxItemAdapter);
        }

        optocalAdsress.setText(address);
        verifyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type==null){
                    tostMessage("请先扫描光交接箱二维码，获取到虚拟信息!");
                }else {
                    //判断是否开启了蓝牙，如果是关闭状态则打开蓝牙
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    LocationManager locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (adapter == null || !adapter.isEnabled()) {
                        startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                    }else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        tostMessage("请打开手机GPS定位功能配合蓝牙操作！");
                    }else
                        startActivityForResult(new Intent(OpticalSplitterBox.this, BlueTooth_Devices.class), 0);
                }

            }
        });
        jumpOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnect==false){
                    tostMessage("未连接到蓝牙设备，请点击”验证信息“按钮连接到蓝牙设备!");
                }else {
                    isJumpOperation=true;
                    showJumpOperationDialog();
                }
            }
        });

    }
    JumpOperationDialog jumpOperationDialog;
    private void showJumpOperationDialog(){
        jumpOperationDialog=new JumpOperationDialog(this,R.style.loading_dialog,JumpOnClickListener);
        jumpOperationDialog.show();
        jumpstatus=0;
    }
    View.OnClickListener JumpOnClickListener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tiaoxian_origin:
                    jumpstatus=1;
                    tostMessage("请读取RFID信息，获取跳纤结起始点数据");
                    break;
                case R.id.tiaoxian_end:
                    jumpstatus=2;
                    tostMessage("请读取RFID信息，获取跳纤结束点数据");
                    break;
                case R.id.cancel_jump:
                    jumpOperationDialog.dismiss();
                    jumpstatus=0;
                    isJumpOperation=false;
                    break;
                case R.id.send_jump:
                    jumpOperationDialog.dismiss();
                    jumpstatus=0;
                    isJumpOperation=false;
                    new AlertDialog.Builder(OpticalSplitterBox.this)
                            .setTitle("确定跳纤")
                            .setMessage("操作人:user3  是否确认跳纤操作?")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("'确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveJumpOperation();
                                }
                            }).show();

                    break;

            }
        }
    };
    private void saveJumpOperation(){

        String str1=jumpOperationDialog.originName.getText().toString();
        String str2=jumpOperationDialog.originRfID.getText().toString();
        String str3=jumpOperationDialog.originFflange.getText().toString();
        String str4= jumpOperationDialog.originLocation.getText().toString();
        String str5=jumpOperationDialog.endName.getText().toString();
        String str6=jumpOperationDialog.endRfID.getText().toString();
        String str7=jumpOperationDialog.endFlange.getText().toString();
        String str8=jumpOperationDialog.endLocation.getText().toString();
        /*String str9=jumpOperationDialog.operationName.getText().toString();*/
        if(str2.length()>0 && str6.length()>0 ) {
            if(str2.equals(str6)){
               tostMessage("跳纤的起始点一样，重新选择!");
            }else {
                JumpOperation jumpOperation=new JumpOperation();
                jumpOperation.setOperationName("user3");
                jumpOperation.setOriginFlange(str3);
                jumpOperation.setOriginLocation(str4);
                jumpOperation.setOriginName(str1);
                jumpOperation.setOriginRfID(str2);

                jumpOperation.setEndFlange(str7);
                jumpOperation.setEndLocation(str8);
                jumpOperation.setEndName(str5);
                jumpOperation.setEndRfID(str6);
                jumpOperation.save();
            }

        }else {
            tostMessage("跳纤起点或终点不能为空!");
        }

    }
    private void tostMessage(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null)
            return;
        mDeviceName=data.getStringExtra(DEVICE_NAME);
        if(mDeviceName==null)
            mDeviceName="未命名";
        mDeviceAddress = data.getStringExtra(DEVICE_ADDRESS);
        mRssi = data.getStringExtra(DEVICE_RSSI);
        if(requestCode==0){
            Intent gattServiceIntent = new Intent(this, MyService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }

    }
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MyService.LocalBinder) service).getService();
            if (!mService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            mService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            final String action = intent.getAction();
            if (MyService.ACTION_GATT_CONNECTED.equals(action)) {
                //btnSearch.setText("已连接");
                isConnect=true;
                new AlertDialog.Builder(OpticalSplitterBox.this)
                        .setTitle("连接成功")
                        .setMessage("已成功连接到硬件设备，请读取RFID数据开始验证或跳纤操作")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                System.out.println("BroadcastReceiver :" + "device connected");

            } else if (MyService.ACTION_GATT_DISCONNECTED.equals(action)) {
                /*btnSearch.setText("未连接");*/
                tostMessage("未连接到蓝牙设备");
                isConnect=false;
                //System.out.println("BroadcastReceiver :" + "device disconnected");

            } else if (MyService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mService.getSupportedGattServices());
                System.out.println("BroadcastReceiver :" + "device SERVICES_DISCOVERED");


            } else if (MyService.ACTION_DATA_AVAILABLE.equals(action)) {
                final String string = intent.getExtras().getString(MyService.EXTRA_DATA);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("REV_DATA", string);
                        message.setData(bundle);
                        handler.sendMessage(message);
//                        try {
//                            Thread.sleep(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                }).start();

            }
        }
    };
    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";


        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            System.out.println("Service uuid:" + uuid);

            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();

                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mService.readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    mService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
                    mService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,true);
                }

                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);

        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String string = msg.getData().getString("REV_DATA");
                    System.out.println("REV_DATA_RFID="+string);
                    if (string.length() ==36) {
                        displayData(string);
                    }else if(string.length()==34){
                        string="bb"+string;
                        displayData(string);
                    }
                    System.out.println("蓝牙返回的数据======"+string);

            }
            super.handleMessage(msg);
        }
    };
    private String addBlank(String string){
        StringBuilder sb=new StringBuilder();
        sb.append(string);
        for(int i=2;i<sb.length();i+=3) {
            sb.insert(i, " ");
        }
        sb.insert(sb.length(), " ");
        string=sb.toString();
        return string;
    }
    private String jumpName,jumpflange,jumplocatipn,jumprfid;
    private void displayData(final String rev_string) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss yyyy年MM月dd日");
        Date curDate =  new Date(System.currentTimeMillis());//获取当前时间
        String timeStr =  df.format(curDate);
        if (rev_string.compareTo("00") == 0) {
           /* btnSearch.setText("已连接");
            rev_str = "蓝牙设备已连接上"+"\n";*/
        }else
        {
            if(rev_string.indexOf("abaa")>0||rev_string.indexOf("abaa")==0){
                rev_str =rev_str+ addBlank(rev_string.toUpperCase())+ "\n"+ timeStr +"\n"+"\n";
            }
            else {
                //rev_str =rev_str+ '\0';
                rev_str += addBlank(rev_string).toUpperCase();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isJumpOperation == false) {
                    List<OpticalFiberJumper> opticalFiberJumperList = DataSupport.findAll(OpticalFiberJumper.class);
                    if (opticalFiberJumperList.size() > 0) {
                        List<OpticalFiberJumper> verify = DataSupport.where("rfId=?", rev_string).find(OpticalFiberJumper.class);
                        if (verify.size() > 0) {
                            final OpticalFiberJumper opticalFiberJumper = verify.get(0);
                            new AlertDialog.Builder(OpticalSplitterBox.this)
                                    .setTitle("验证信息")
                                    .setMessage("该RFID是" + opticalFiberJumper.getOpticalBoxName() + "光交接箱的" + opticalFiberJumper.getFlangeName() + "盘"
                                            + opticalFiberJumper.getLocation() + "号的光芯" + "")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(OpticalSplitterBox.this, OpticalFiberJumperActivity.class);
                                            intent.putExtra("order", opticalFiberJumper.getFlangeName());
                                            intent.putExtra("gjjxname", opticalFiberJumper.getOpticalBoxName());
                                            intent.putExtra("rfid", opticalFiberJumper.getRfId());
                                            intent.putExtra("localtion", opticalFiberJumper.getLocation());
                                            OpticalSplitterBox.this.startActivity(intent);
                                        }
                                    })
                                    .show();

                        } else {
                            tostMessage("当前光芯信息未录入，请录入后再验证!");
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "当前光芯信息未录入，请录入后再验证!", Toast.LENGTH_SHORT).show();
                    }
                }else {

                    if(jumpstatus==1){
                        List<OpticalFiberJumper> opticalFiberJumperList = DataSupport.findAll(OpticalFiberJumper.class);
                        if (opticalFiberJumperList.size() > 0) {
                            List<OpticalFiberJumper> verify = DataSupport.where("rfId=?", rev_string).find(OpticalFiberJumper.class);
                            if (verify.size() > 0) {
                                final OpticalFiberJumper opticalFiberJumper = verify.get(0);
                                new AlertDialog.Builder(OpticalSplitterBox.this)
                                        .setTitle("起点设置")
                                        .setMessage("是否确认设置该光芯为跳纤起点？")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                jumpName=opticalFiberJumper.getOpticalBoxName();
                                                jumplocatipn=opticalFiberJumper.getLocation();
                                                jumpflange=opticalFiberJumper.getFlangeName();
                                                jumprfid=opticalFiberJumper.getRfId();
                                                jumpOperationDialog.originName.setText(jumpName);
                                                jumpOperationDialog.originRfID.setText(jumprfid);
                                                jumpOperationDialog.originFflange.setText(jumpflange);
                                                jumpOperationDialog.originLocation.setText(jumplocatipn);

                                            }
                                        })
                                        .show();

                            } else {
                                tostMessage("当前光芯信息未录入录入错误，请重新录入后再验证!");
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "当前光芯信息未录入或录入错误，请重新录入后再验证!", Toast.LENGTH_SHORT).show();
                        }
                    }else if(jumpstatus==2){
                        List<OpticalFiberJumper> opticalFiberJumperList = DataSupport.findAll(OpticalFiberJumper.class);
                        if (opticalFiberJumperList.size() > 0) {
                            List<OpticalFiberJumper> verify = DataSupport.where("rfId=?", rev_string).find(OpticalFiberJumper.class);
                            if (verify.size() > 0) {
                                final OpticalFiberJumper opticalFiberJumper = verify.get(0);
                                new AlertDialog.Builder(OpticalSplitterBox.this)
                                        .setTitle("终点设置")
                                        .setMessage("是否确认设置该光芯为跳纤终点？")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                jumpName=opticalFiberJumper.getOpticalBoxName();
                                                jumplocatipn=opticalFiberJumper.getLocation();
                                                jumpflange=opticalFiberJumper.getFlangeName();
                                                jumprfid=opticalFiberJumper.getRfId();
                                                jumpOperationDialog.endName.setText(jumpName);
                                                jumpOperationDialog.endRfID.setText(jumprfid);
                                                jumpOperationDialog.endFlange.setText(jumpflange);
                                                jumpOperationDialog.endLocation.setText(jumplocatipn);
                                            }
                                        })
                                        .show();

                            } else {
                                tostMessage("当前光芯信息未录入录入错误，请重新录入后再验证!");
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "当前光芯信息未录入或录入错误，请重新录入后再验证!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        tostMessage("请先选择跳纤起点或终点！");
                    }


                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mService != null) {
            final boolean result = mService.connect(mDeviceAddress);
            System.out.println("Connect request result=" + result);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        mService = null;
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(MyService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(MyService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(MyService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}


