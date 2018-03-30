package com.ck.mobileoperations.busniess.design;

import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ck.mobileoperations.R;

/**
 * Created by chenkai on 2018/1/2.
 */

public class CreateRouteDialog extends Dialog {

    Activity context;
    private View.OnClickListener monClickListener;

    private Button cancelCreate;
    private Button querenCreate;
    public EditText proName;
    public CreateRouteDialog( Activity context) {
        super(context);
        this.context=context;
    }

    protected CreateRouteDialog( Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context=context;
        this.monClickListener=clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_route_dialog);
        proName=(EditText)findViewById(R.id.pro_name);
        cancelCreate=(Button)findViewById(R.id.cancel_create);
        querenCreate=(Button)findViewById(R.id.queren_create);

         /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */

        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        cancelCreate.setOnClickListener(monClickListener);
        querenCreate.setOnClickListener(monClickListener);
        this.setCancelable(true);

    }
}
