package com.ck.mobileoperations.busniess.maintain.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ck.mobileoperations.R;
import com.ck.mobileoperations.busniess.maintain.OpticalSplitterBox;

import java.util.ArrayList;

public class BlueTooth_Devices extends Activity {
    public static String DEVICE_NAME = "DEVICE_NAME";
    public static String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String DEVICE_RSSI = "DEVICE_RSSI";
    private static final long SCAN_PERIOD = 10000;

    private ArrayList<Integer> rssis;             // 蓝牙信号强度
    private ListView lvService;
    private Handler mHandler;
    private LeDeviceListAdapter deviceAdapter;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.acticity_devices);
        init();
        scanLeDevice();
        deviceAdapter=new LeDeviceListAdapter();
        lvService.setAdapter(deviceAdapter);
        lvService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice device = deviceAdapter.getDevice(position);
                if (device == null)
                    return;
                final Intent intent = new Intent(BlueTooth_Devices.this, OpticalSplitterBox.class);
                intent.putExtra(DEVICE_NAME, device.getName());
                intent.putExtra(DEVICE_ADDRESS, device.getAddress());
                intent.putExtra(DEVICE_RSSI, rssis.get(position).toString());
                setResult(0, intent);
                finish();
            }
        });
    }

    private void scanLeDevice() {
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);
			/* 开始扫描蓝牙设备，带mLeScanCallback 回调函数 */
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }


    private void init() {
        lvService = (ListView) findViewById(R.id.lv_service);
        mHandler = new Handler();

        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    deviceAdapter.addDevice(device,rssi);
                    deviceAdapter.notifyDataSetChanged();
                }
            });
            System.out.println("Name:" + device.getName());
            System.out.println("Addr:" + device.getAddress());
            System.out.println("rssi:" + rssi);
        }
    };
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;

        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            rssis = new ArrayList<Integer>();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                rssis.add(rssi);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
            rssis.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // General ListView optimization code.
            // 加载listview每一项的视图
            view = mInflator.inflate(R.layout.list_item, null);
            TextView deviceAddress = (TextView) view.findViewById(R.id.tv_addr);
            TextView deviceName = (TextView) view.findViewById(R.id.tv_name);
            TextView deviceRssi = (TextView) view.findViewById(R.id.tv_rssi);

            BluetoothDevice device = mLeDevices.get(i);
            deviceAddress.setText(device.getAddress());
            deviceName.setText(device.getName());
            deviceRssi.setText("" + rssis.get(i));
            return view;
        }
    }


    @Override
    protected void onDestroy() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        super.onDestroy();
    }

}