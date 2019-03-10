package com.example.networkstate;

import android.app.Application;

import com.example.library.NetWorManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetWorManager.getDefault().init(this);
    }
}
