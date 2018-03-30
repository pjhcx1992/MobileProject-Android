package com.ck.mobileoperations.utils;

import com.mob.MobApplication;
import com.mob.MobSDK;

import org.litepal.LitePalApplication;

/**
 * Created by chenkai on 2018/1/2.
 */

public class AppContext extends MobApplication {


    private static AppContext context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        LitePalApplication.initialize(this);
    }
}
