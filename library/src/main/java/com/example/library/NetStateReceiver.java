package com.example.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import com.example.library.annotation.NetWork;
import com.example.library.bean.MethodManager;
import com.example.library.listener.NetWorkListener;
import com.example.library.type.NetType;
import com.example.library.utils.NetWorkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网络监听的广播
 */
public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = NetStateReceiver.class.getSimpleName();

    private Map<Object, List<MethodManager>> methodManager = null;

    private NetType netType;
    private NetWorkListener listener;

    public NetStateReceiver() {
        //重置网络类型为没有网络
        if (netType != null) {
            netType = NetType.NONE;
        }

        if (methodManager == null) {
            methodManager = new HashMap<>();
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

        //将所有订阅网络的Activity中的所有方法分发, 结果在methodManager
        post(netType);

    }

    private void post(NetType netType) {
        Set<Object> objects = methodManager.keySet();
        for (Object getter : objects) {
            if (getter == null) continue;

            List<MethodManager> managerList = methodManager.get(getter);
            if (managerList != null) {
                for (MethodManager method : managerList) {
                    if (method == null) continue;

                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, getter, netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;

                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;

                            case NONE:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager method, Object getter, NetType netType) {
        Method execute = method.getMethod();

        try {
            execute.setAccessible(true);
            execute.invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //注册网络观察者
    public void registerReceiver(Object register) {
        List<MethodManager> methodManagers = methodManager.get(register);
        if (methodManagers == null) {
            methodManagers = findAnntationMethod(register);
            methodManager.put(register, methodManagers);
        }
    }

    private List<MethodManager> findAnntationMethod(Object register) {
        List<MethodManager> managerList = new ArrayList<>();

        Class<?> clazz = register.getClass();
        if (clazz == null) return managerList;

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method == null) continue;

            NetWork netWork = method.getAnnotation(NetWork.class);

            if (netWork == null) continue;

            Type returnType = method.getGenericReturnType();

            if (!"void".equalsIgnoreCase(returnType.toString())) {
                throw new RuntimeException(method.getName() + "方法返回必须是void");
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            if (1 != parameterTypes.length) {
                throw new RuntimeException(method.getName() + "方法参数有且只有一个");
            }

            MethodManager manager = new MethodManager(parameterTypes[0], netWork.netType(), method);

            managerList.add(manager);
        }

        return managerList;
    }

    //移除网络观察者
    public void unregisterReceiver(Object register) {
        if (register == null) return;

        if (!methodManager.isEmpty()) {
            methodManager.remove(register);
        }
    }

    //移除所有
    public void unregisterAllReceiver() {
        if (!methodManager.isEmpty()) {
            methodManager.clear();
        }

        NetWorManager.getDefault().unregisterAllReceiver();
        methodManager = null;
    }
}
