package com.example.library;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.NetworkRequest;

import com.example.library.listener.NetWorkListener;

public class NetWorManager {
    private static volatile NetWorManager mInstance;
    private NetStateReceiver receiver;
    private Application mApplication;

    private NetWorManager() {
        receiver = new NetStateReceiver();
    }

    public static NetWorManager getDefault() {
        if (mInstance == null) {
            synchronized (NetWorManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorManager();
                }
            }
        }

        return mInstance;
    }

    /**
     * 注册网络监听
     * @param listener
     */
    public void setListener(NetWorkListener listener) {
        if (listener != null) {
            receiver.setListener(listener);
        }
    }

    /**
     * 注册广播事件
     * @param application
     */
    public void init(Application application) {
        this.mApplication = application;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);
    }


    public Context getApplication() {
        if (mApplication == null) {
            throw  new RuntimeException("mApplication not init");
        }
        return mApplication;
    }

    public void registerReceiver(Object register) {
        receiver.registerReceiver(register);
    }

    public void unregisterReceiver(Object register) {
        receiver.unregisterReceiver(register);
    }

    public void unregisterAllReceiver() {
        receiver.unregisterAllReceiver();
    }
}
