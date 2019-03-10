package com.example.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.library.listener.NetWorkListener;
import com.example.library.type.NetType;
import com.example.library.utils.NetWorkUtils;

/**
 * 网络监听的广播
 */
public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = NetStateReceiver.class.getSimpleName();

    private NetType netType;
    private NetWorkListener listener;

    public NetStateReceiver() {
        //重置网络类型为没有网络
        if (netType != null) {
            netType = NetType.NONE;
        }
    }

    public void setListener(NetWorkListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.i(Constants.LOG_TAG, "onReceive: 网络发生改变");
        }

        netType = NetWorkUtils.getNetType();
        if (NetWorkUtils.isNetWorkAvailable()) {
            Log.i(Constants.LOG_TAG, "onReceive: 网络连接成功");
            if (listener != null) {
                listener.onConnect(netType);
            }
        } else {
            Log.i(Constants.LOG_TAG, "onReceive: 网络连接失败 ");
        }

    }
}
