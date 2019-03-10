package com.example.library.listener;

import com.example.library.type.NetType;

/**
 * 网络状态的监听
 */
public interface NetWorkListener {
    void onConnect(NetType type);
    void onDisConnect();
}
