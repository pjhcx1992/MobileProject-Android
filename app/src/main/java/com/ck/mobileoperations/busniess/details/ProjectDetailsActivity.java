package com.ck.mobileoperations.busniess.details;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ck.mobileoperations.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CK on 2017/12/15.
 */

public class ProjectDetailsActivity extends Activity {

    @Bind(R.id.back_home)
    ImageView backHome;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.back_home)
    void backhome(){
        finish();
    }
}
