package com.ck.mobileoperations.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ck.mobileoperations.R;

/**
 * Created by CK on 2017/1/15.
 */

public class OpticalCableDialog extends Dialog {
    Activity context;

    public EditText opticalcableName;
    public EditText opticalcableModel;
    public Spinner  opticalcableSize;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    private Button sendOptical;
    private Button cancelOptical;

    private View.OnClickListener mClickListener;

    public OpticalCableDialog(Activity context) {
        super(context);
        this.context=context;
    }

    public OpticalCableDialog(Activity context, int theme, View.OnClickListener onClickListener, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        super(context, theme);
        this.context=context;
        this.mClickListener=onClickListener;
        this.onItemSelectedListener=onItemSelectedListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_opticalcable_dialog);
        opticalcableName=(EditText)findViewById(R.id.opticalcable_name);
        opticalcableSize=(Spinner)findViewById(R.id.optical_cable_size);
        opticalcableModel=(EditText)findViewById(R.id.optical_cable_model);

        sendOptical=(Button)findViewById(R.id.send_optical);
        cancelOptical=(Button)findViewById(R.id.cancel_optical);
         /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);


        sendOptical.setOnClickListener(mClickListener);
        cancelOptical.setOnClickListener(mClickListener);
        opticalcableSize.setOnItemSelectedListener(onItemSelectedListener);

        this.setCancelable(true);



    }
}
