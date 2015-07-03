package com.ram.mirakhil.easybluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothA2dp;
import android.content.Context;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BTConnectThread implements Runnable {

    static String connectedDeviceName = "";
    static List<BluetoothDevice> connectedDevices = null;
    BluetoothA2dp mBluetoothA2dp;
    boolean isDisconnect = false;
    static BluetoothA2dp a2dp = null;
    private BTActivity appContext = null;

    public BTConnectThread(BTActivity context, boolean b) {
        connectedDeviceName = "";
        connectedDevices = null;
        isDisconnect = b;
        appContext = context;
    }

    @Override
    public void run() {
        if (!isDisconnect) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.enable();
            try {
                PriorityConnect();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            //boolean success = false;
            while (true) {
                try {
                    currentConnectionStatus();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (connectedDevices != null) {
                    if(connectedDevices.size() == 0) {
                        connectedDeviceName = "Searching...";
                        updateConnectionStatus(connectedDeviceName);
                    }
                    else{
                        for (String priorityName : BTDataPersistence.btDeviceNames) {

                            for (BluetoothDevice connectedDev : connectedDevices) {
                                if (priorityName.equals(connectedDev.getName())) {
                                        connectedDeviceName = priorityName+" is Connected";
                                        updateConnectionStatus(connectedDeviceName);
                                        return;
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void updateConnectionStatus(final String status){
        appContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appContext.connectStatusText.setText(status);
            }

        });
    }

    public void PriorityConnect() throws IOException, InterruptedException {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bluetoothDevices= mBluetoothAdapter.getBondedDevices();


        BTDataPersistence.initialise(appContext);

        BluetoothDevice findingDevice=null;
        boolean connection=false;
        for(int i=0;((i<BTDataPersistence.btDeviceNames.size())&&(!connection));i++) {
            Iterator I=bluetoothDevices.iterator();
            while ((I.hasNext())&&(!connection)) {
                findingDevice = (BluetoothDevice) I.next();
                String findingDeviceName = findingDevice.getName();
                if (findingDeviceName.equals(BTDataPersistence.btDeviceNames.get(i))) {
                    connectBluetoothA2dp(appContext, findingDevice);
                    for(int j=0;j<13;j++) {
                        Thread.sleep(1000);
                        if(a2dp != null){
                            if(a2dp.getConnectionState(findingDevice)==BluetoothProfile.STATE_CONNECTED){
                                connection=true;
                                break;
                            }
                        }
                    } break;
                }
            }
        }
    }








    public void connectBluetoothA2dp(Context context,final BluetoothDevice deviceToConnect) {
        try {
            Class<?> c2 = Class.forName("android.os.ServiceManager");
            Method m2 = c2.getDeclaredMethod("getService", String.class);
            IBinder b = (IBinder) m2.invoke(c2.newInstance(), "bluetooth_a2dp");
            if (b == null) {
                // For Android 4.2 Above Devices
                BluetoothAdapter.getDefaultAdapter().getProfileProxy(context,
                        new BluetoothProfile.ServiceListener() {

                            @Override
                            public void onServiceDisconnected(int profile) {

                            }

                            @Override
                            public void onServiceConnected(int profile,
                                                           BluetoothProfile proxy) {
                                BluetoothA2dp a2dp2 = (BluetoothA2dp) proxy;
                                if(a2dp2 != null) {
                                    a2dp = a2dp2;
                                }
                                try {

                                    a2dp2.getClass().getMethod("connect",BluetoothDevice.class).invoke(a2dp2, deviceToConnect);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, BluetoothProfile.A2DP);
            }
            else {
                // For Android below 4.2 devices
                Class<?> c3 = Class.forName("android.bluetooth.IBluetoothA2dp");
                Class<?>[] s2 = c3.getDeclaredClasses();
                Class<?> c = s2[0];
                Method m = c.getDeclaredMethod("asInterface", IBinder.class);
                m.setAccessible(true);
                IBluetoothA2dp a2dp = (IBluetoothA2dp) m.invoke(null, b);
                a2dp.connect(deviceToConnect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////
    public void currentConnectionStatus() throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        connectedDevices=null;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.getProfileProxy(appContext,
                new BluetoothProfile.ServiceListener() {

                    @Override
                    public void onServiceDisconnected(int profile) {
                        mBluetoothA2dp = null;
                    }

                    @Override
                    public void onServiceConnected(int profile,
                                                   BluetoothProfile proxy) {
                        mBluetoothA2dp = (BluetoothA2dp) proxy;
                        if(mBluetoothA2dp != null) {
                            connectedDevices = mBluetoothA2dp.getConnectedDevices();
                        }
                    }
                }, BluetoothProfile.A2DP);
    }
}
