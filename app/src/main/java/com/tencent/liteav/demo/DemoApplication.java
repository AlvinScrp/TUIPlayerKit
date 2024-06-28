package com.tencent.liteav.demo;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.tencent.qcloud.tuiplayer.core.TUIPlayerConfig;
import com.tencent.qcloud.tuiplayer.core.TUIPlayerCore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DemoApplication extends MultiDexApplication {
    private static String TAG = "DemoApplication";

    //    private RefWatcher mRefWatcher;
    private static DemoApplication instance;

    private Context mAppContext;

    private static final String LICENCE_URL =
            "https://license.vod2.myqcloud.com/license/v2/1251420592_1/v_cube.license";
    private static final String LICENCE_KEY = "7ee66104152536a3cc596583207c3896";

    @Override
    public void onCreate() {

        super.onCreate();

        mAppContext = this.getApplicationContext();
        instance = this;

        TUIPlayerConfig config = new TUIPlayerConfig.Builder()
                .enableLog(true)
                .licenseKey(LICENCE_KEY)
                .licenseUrl(LICENCE_URL)
                .build();
        TUIPlayerCore.init(getApplicationContext(), config);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        closeAndroidPDialog();
        preRequestShortVideosIfNeed();
    }

    public static DemoApplication getApplication() {
        return instance;
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preRequestShortVideosIfNeed() {
        try {
            Class<?> shortVideoModelClass = Class.forName("com.tencent.liteav.demo.player.expand.model" +
                    ".ShortVideoModel");
            Method getInstanceMethod = shortVideoModelClass.getMethod("getInstance", Context.class);
            Method preloadMethod = shortVideoModelClass.getMethod("preloadVideosIfNeed");
            Object shortVideoModelObj = getInstanceMethod.invoke(null ,this);
            preloadMethod.invoke(shortVideoModelObj);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "preRequest shortVideo sources failed:", e);
            e.printStackTrace();
        }
    }

}
