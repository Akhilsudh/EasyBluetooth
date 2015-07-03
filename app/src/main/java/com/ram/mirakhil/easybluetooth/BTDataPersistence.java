/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ram.mirakhil.easybluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class BTDataPersistence {

    static String FILENAME = "priority_list_file";
    public static ArrayList<String> btDeviceNames = new ArrayList<String>();


    public static void initialise(Context appContext){
        if(!readPriority(appContext)) {
            getBluetoothDevices();
        }
    }

    public static void resetPriorityList(){
        getBluetoothDevices();
    }





    public static boolean storePriority(ArrayList<String> BTOrderedList,Context appContext) {

        boolean rvalue=true;
        try {
            FileOutputStream fos = appContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(serialize(BTOrderedList));
            fos.close();
        }
        catch(Exception e){
            rvalue=false;
        }
        return rvalue;
    }

    public static boolean readPriority(Context appContext){
        boolean rvalue=true;
        try {
            FileInputStream fis = appContext.openFileInput(FILENAME);
            byte[] buffer = new byte[2048];
            if(fis.read(buffer) > 0) {
                btDeviceNames = (ArrayList<String>) deserialize(buffer);
            }
            else{
                rvalue = false;
            }
            fis.close();
        }
        catch(Exception e){
            rvalue=false;
        }
        return rvalue;
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

    public static void getBluetoothDevices(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothAdapter.enable();
        btDeviceNames = new ArrayList<String>();
        Set<BluetoothDevice> a= mBluetoothAdapter.getBondedDevices();
        Iterator I=a.iterator();
        BluetoothDevice n=null;
        while(I.hasNext()) {
            n = (BluetoothDevice) I.next();
            BluetoothClass x=n.getBluetoothClass();
            if(x!=null){
                int xc=x.getDeviceClass();
                if((xc==BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO)||
                        (xc==BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO)||
                        (xc==BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET)||
                        (xc==BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER))
                {
                    String k = n.getName();
                    btDeviceNames.add(k);
                }
            }
        }
    }

}
