package com.example.networkstate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.library.Constants;
import com.example.library.NetWorManager;
import com.example.library.listener.NetWorkListener;
import com.example.library.type.NetType;

public class NetStateTestActivity extends AppCompatActivity implements NetWorkListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetWorManager.getDefault().init(getApplication());
        NetWorManager.getDefault().setListener(this);
    }


    @Override
    public void onConnect(NetType type) {
        Log.i(Constants.LOG_TAG, "onConnect 网络类型 : "+type.name());
    }

    @Override
    public void onDisConnect() {
        Log.i(Constants.LOG_TAG, "onDisConnect: 网络中断");
    }
}
