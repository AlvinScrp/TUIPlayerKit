package com.tencent.liteav.demo.superplayer.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 画中画中间层
 */
public class SuperPlayerPipProxy {

    private static final String TAG = "SuperPlayerPipProxy";

    private Object mPipManagerObj;
    private Object mPipAdapterObj;
    private Class<?> mPipManagerClass;
    private Class<?> mPipDftAdapterClass;

    private PipProxyActionCallback mActionCallback;

    @SuppressWarnings({"RedundantArrayCreation", "JavaReflectionInvocation"})
    public SuperPlayerPipProxy(Activity activity) {
        try {
            mPipManagerClass = Class.forName("com.tencent.qcloud.tuiplayer.pip.TUIPIPManager");
            Class<?> managerFactoryClass = Class.forName("com.tencent.qcloud.tuiplayer.pip.TUIPIPManagerFactory");
            Method createManagerMethod = managerFactoryClass.getMethod("createPIPManager", Activity.class);
            mPipManagerObj = createManagerMethod.invoke(null, activity);

            // create adapter
            mPipDftAdapterClass = Class.forName("com.tencent.qcloud.tuiplayer.pip.TUIDftActionAdapter");
            Constructor<?> adapterConstructor = mPipDftAdapterClass.getConstructor(Context.class);
            mPipAdapterObj = adapterConstructor.newInstance(activity);

            // setAdapter
            Class<?> baseAdapterClass = Class.forName("com.tencent.qcloud.tuiplayer.pip.TUIBaseActionAdapter");
            Method setAdapterMethod = mPipManagerClass.getMethod("setPIPActionAdapter", baseAdapterClass);
            setAdapterMethod.invoke(mPipManagerObj, mPipAdapterObj);

            // setListener
            Class<?> listenerClass = Class.forName("com.tencent.qcloud.tuiplayer.pip.action" +
                    ".TUIDftActionAdapter$OnPictureInPictureClickListener");
            Method setListenerMethod = mPipDftAdapterClass.getMethod("setListener", listenerClass);
            ActionCallbackHandler listenerHandler = new ActionCallbackHandler();
            Object actionListenerObj = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{listenerClass}
                    , listenerHandler);
            setListenerMethod.invoke(mPipAdapterObj, new Object[]{actionListenerObj});
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            Log.e(TAG, "", e);
        }
    }

    public int enterPip(View displayView) {
        try {
            if (null != mPipManagerClass && null != mPipManagerObj) {
                Method enterMethod = mPipManagerClass.getDeclaredMethod("enterPipMode", View.class);
                Integer result = (Integer) enterMethod.invoke(mPipManagerObj, displayView);
                if (null != result) {
                    return result;
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            Log.e(TAG, "", e);
        }
        return -1;
    }

    public void onPipModeChanged(boolean pipMode, Configuration configuration) {
        try {
            if (null != mPipManagerClass && null != mPipManagerObj) {
                Method modeChangedMethod = mPipManagerClass.getMethod("onPipModeChanged", boolean.class,
                        Configuration.class);
                modeChangedMethod.invoke(mPipManagerObj, pipMode, configuration);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "", e);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        try {
            if (null != mPipManagerClass && null != mPipManagerObj) {
                Method modeChangedMethod = mPipManagerClass.getMethod("onConfigurationChanged", Configuration.class);
                modeChangedMethod.invoke(mPipManagerObj, configuration);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "", e);
        }
    }

    public void setActivityLifecycle(Lifecycle lifecycle) {
        try {
            if (null != mPipManagerClass && null != mPipManagerObj) {
                Method modeChangedMethod = mPipManagerClass.getMethod("setActivityLifecycle", Lifecycle.class);
                modeChangedMethod.invoke(mPipManagerObj, lifecycle);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "", e);
        }
    }

    public void switchPlayStatus(boolean isPlaying) {
        try {
            if (null != mPipDftAdapterClass && null != mPipAdapterObj) {
                Method method = mPipDftAdapterClass.getMethod("switchPlayStatus", boolean.class);
                method.invoke(mPipAdapterObj, isPlaying);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "", e);
        }
    }

    public void setPipActionCallback(PipProxyActionCallback callback) {
        mActionCallback = callback;
    }

    private class ActionCallbackHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final String methodName = method.getName();
            if (TextUtils.equals(methodName, "onClickBack")) {
                if (null != mActionCallback) {
                    mActionCallback.onClickBack();
                }
            } else if (TextUtils.equals(methodName, "onClickForward")) {
                if (null != mActionCallback) {
                    mActionCallback.onClickForward();
                }
            } else if (TextUtils.equals(methodName, "onClickPlay")) {
                if (null != mActionCallback) {
                    mActionCallback.onClickPlay();
                }
            } else {
                Log.e(TAG, "ActionCallbackHandler invoke unknown method:" + methodName);
            }
            return null;
        }
    }

    /**
     * superPlayer delegate PIP action callback
     */
    public interface PipProxyActionCallback {

        void onClickPlay();

        void onClickBack();

        void onClickForward();
    }

}
