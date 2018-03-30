package com.ck.mobileoperations.busniess.maintain.vooptical;

import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ck.mobileoperations.R;

/**
 * Created by chenkai on 2018/2/11.
 */

public class JumpOperationDialog extends Dialog {
    Activity context;
    public TextView originName,endName;
    public TextView originFflange,endFlange;
    public TextView originRfID,endRfID;
    public TextView originLocation,endLocation;
    public Button originButton,endButton;
    private Button cancelJump,sendJump;
    public TextView operationName;
    public View.OnClickListener onClickListener;
    public JumpOperationDialog( Activity context) {
        super(context);
        this.context=context;
    }

    public JumpOperationDialog(Activity context, int theme, View.OnClickListener onClickListener) {
        super(context, theme);
        this.context=context;
        this.onClickListener=onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_jumpoperation_dialog);

        originFflange=(TextView) findViewById(R.id.jumporigin_flange_name);
        originLocation=(TextView)findViewById(R.id.jumporigin_location_name);
        originName=(TextView)findViewById(R.id.jumporigin_gjjx_name);
        originRfID=(TextView)findViewById(R.id.jumporigin_rfidid_name);
        originButton=(Button)findViewById(R.id.tiaoxian_origin);
        operationName=(TextView)findViewById(R.id.operation_name);

        endFlange=(TextView) findViewById(R.id.jumpend_flange_name);
        endLocation=(TextView)findViewById(R.id.jumpend_location_name);
        endName=(TextView)findViewById(R.id.jumpend_gjjx_name);
        endRfID=(TextView)findViewById(R.id.jumpend_rfidid_name);
        endButton=(Button)findViewById(R.id.tiaoxian_end);
        cancelJump=(Button)findViewById(R.id.cancel_jump);
        sendJump=(Button)findViewById(R.id.send_jump);
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

        originButton.setOnClickListener(onClickListener);
        endButton.setOnClickListener(onClickListener);
        cancelJump.setOnClickListener(onClickListener);
        sendJump.setOnClickListener(onClickListener);
        this.setCancelable(true);
    }
}
