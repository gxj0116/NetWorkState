package com.example.networkstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.library.Constants;
import com.example.library.NetWorManager;
import com.example.library.annotation.NetWork;
import com.example.library.listener.NetWorkListener;
import com.example.library.type.NetType;

public class NetStateTestActivity extends AppCompatActivity /*implements NetWorkListener*/ {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        NetWorManager.getDefault().init(getApplication());
//        NetWorManager.getDefault().setListener(this);

        NetWorManager.getDefault().registerReceiver(this);
    }

//    @Override
//    public void onConnect(NetType type) {
//        Log.i(Constants.LOG_TAG, "onConnect 网络类型 : "+type.name());
//    }
//
//    @Override
//    public void onDisConnect() {
//        Log.i(Constants.LOG_TAG, "onDisConnect: 网络中断");
//    }

    @NetWork(netType = NetType.WIFI)
    public void netWork(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.i(Constants.LOG_TAG, "netWork: 网络连接成功 网络类型: WIFI");
                break;
            case CMNET:
            case CMWAP:
                Log.i(Constants.LOG_TAG, "netWork: NetStateTestActivity 网络连接成功， 网络类型:"+netType.name());
                break;
            case NONE:
                Log.i(Constants.LOG_TAG, "netWork: 没有网络");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        //反注册
        NetWorManager.getDefault().unregisterReceiver(this);
        NetWorManager.getDefault().unregisterAllReceiver();
    }
}
