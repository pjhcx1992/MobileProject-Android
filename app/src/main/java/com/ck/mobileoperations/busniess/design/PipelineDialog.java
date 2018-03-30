package com.ck.mobileoperations.busniess.design;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ck.mobileoperations.R;

/**
 * Created by chenkai on 2018/1/17.
 */

public class PipelineDialog extends Dialog {
    Activity context;
    private Button buttonOriGin;
    private Button buttonEnd;
    public TextView originName,endName;
    public TextView originJingdu,originWeidu;
    public TextView endJingdu,endWeidu;
    private Button sendCreate,cancelCreate;
    private View.OnClickListener monClickListener;
    public PipelineDialog( Activity context) {
        super(context);
        this.context=context;
    }

    protected PipelineDialog(Activity context, int theme, View.OnClickListener onClickListener) {
        super(context, theme);
        this.context=context;
        this.monClickListener=onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_guandao_dialog);


        buttonOriGin=(Button)findViewById(R.id.button_origin);
        buttonEnd=(Button)findViewById(R.id.button_end);
        sendCreate=(Button)findViewById(R.id.guandao_send_mark);
        cancelCreate=(Button)findViewById(R.id.guandao_cancel_mark);

        originName=(TextView)findViewById(R.id.origin_name);
        originJingdu=(TextView)findViewById(R.id.origin_jingdu);
        originWeidu=(TextView)findViewById(R.id.origin_weidu);
        endName=(TextView)findViewById(R.id.end_name);
        endJingdu=(TextView)findViewById(R.id.end_jingdu);
        endWeidu=(TextView)findViewById(R.id.end_weidu);



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

        buttonOriGin.setOnClickListener(monClickListener);
        buttonEnd.setOnClickListener(monClickListener);
        sendCreate.setOnClickListener(monClickListener);
        cancelCreate.setOnClickListener(monClickListener);
        this.setCancelable(true);


    }
}
