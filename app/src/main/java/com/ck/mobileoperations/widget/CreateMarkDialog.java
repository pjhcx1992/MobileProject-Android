package com.ck.mobileoperations.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.amap.api.maps.model.LatLng;
import com.ck.mobileoperations.R;

/**
 * Created by CK on 2017/12/21.
 */

public class CreateMarkDialog extends Dialog {
    Activity context;
    private View.OnClickListener mClickListener;
    private Button btn_jing;
    private Button btn_gan;
    private Button btn_xiang;
    private Button btn_qiang;
    private Button btn_dian;
    private Button btn_qita;

    public CreateMarkDialog( Activity context) {
        super(context);
        this.context=context;
    }

    public CreateMarkDialog(Activity context, int theme, View.OnClickListener onClickListener) {
        super(context, theme);
        this.context=context;
        this.mClickListener=onClickListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_mark_dialog);

        btn_jing=(Button)findViewById(R.id.ren_shou_jing);
        btn_gan=(Button)findViewById(R.id.dian_gan);
        btn_xiang=(Button)findViewById(R.id.guang_jiao_xiang);
        btn_qiang=(Button)findViewById(R.id.gua_qiang);
        btn_dian=(Button)findViewById(R.id.yin_shang_dian);
        btn_qita=(Button)findViewById(R.id.qi_ta);

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
        btn_jing.setOnClickListener(mClickListener);
        btn_gan.setOnClickListener(mClickListener);
        btn_xiang.setOnClickListener(mClickListener);
        btn_qiang.setOnClickListener(mClickListener);
        btn_dian.setOnClickListener(mClickListener);
        btn_qita.setOnClickListener(mClickListener);
        this.setCancelable(true);
    }

}
