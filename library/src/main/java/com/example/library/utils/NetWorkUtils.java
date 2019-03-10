package com.example.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.library.NetWorManager;
import com.example.library.type.NetType;

public class NetWorkUtils {

    /**
     * 检查当前网络是否可用
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetWorkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) NetWorManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) return false;

         NetworkInfo[] info = connMgr.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo netInfo : info) {
                if (netInfo == null) {
                    continue;
                }
                if (NetworkInfo.State.CONNECTED == netInfo.getState()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取当前网络类型
     * -1: 没有网络
     * @return
     */
    @SuppressLint("MissingPermission")
    public static NetType getNetType() {
        ConnectivityManager connMgr = (ConnectivityManager) NetWorManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) return NetType.NONE;

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) return  NetType.NONE;

        int type = networkInfo.getType();

        if (ConnectivityManager.TYPE_MOBILE == type) {

            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }
        } else if (ConnectivityManager.TYPE_WIFI == type) {
            return NetType.WIFI;
        }

        return NetType.NONE;
    }


}
