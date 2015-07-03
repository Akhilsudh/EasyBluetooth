// IBluetooth.aidl
package android.bluetooth;
import android.bluetooth.BluetoothDevice;

interface IBluetooth {
   String getRemoteAlias(in String address);
   boolean setRemoteAlias(in String address, in String name);
}
