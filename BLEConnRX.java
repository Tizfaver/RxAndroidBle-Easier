//by @tizfaver
package com.example.ard_os;

import android.annotation.SuppressLint;
import android.util.Log;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class BLEConnRX{
    private RxBleClient rxBleClient;
    private Disposable scanDisposable, rxConnection;
    private RxBleConnection transmit;
    private boolean forceStopScann = false, isconn = false, wait = false;
    private String mac;
    private UUID uuid;

    /*
    HOW USE THIS CLASS

    > This Class (that could be modified) only scan and connect by the MAC address. You will need to make changes if you don't want that.
    > Only works if RxAndroidBle 2 is implemented:   implementation "com.polidea.rxandroidble2:rxandroidble:1.17.2" <- for Gradle, in the Module.

    Declaring the Client (as github doc):            RxBleClient rxBleClient = RxBleClient.create(getApplicationContext());
    Creating the class object:                       rx = new BLEConnRX(rxBleClient, mac);
    Starting and Connecting:                         rx.startScanAndConnect();
    To check if BLE is connected:                    rx.isConnected();
    To send a message:                               rx.writeMessage("ciao", uuid);
    If for some reason want force stop scanning:     rx.setForceStopScann(false);
    Disconnecting from the BLE device:               rx.disconnect();

    CHECK THE END OF THIS FILE FOR ALL NEEDED PERMISSIONS.

    TODO: make the class bigger with much more functions, example only scan, only connect, scan and connect by Name...
    */

    public BLEConnRX(RxBleClient rx, String newMac, String newUUID){
        this.rxBleClient = rx;
        this.mac = newMac;
        this.uuid = UUID.fromString(newUUID);
    }

    @SuppressLint("CheckResult")
    public void startScanAndConnect(){
        Log.w("bleee", "Calling the Scanning Function........................");
        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build();

        ScanFilter scanFilters = new ScanFilter.Builder() //If you want you can scan by only the MAC address or other filters
                .setDeviceAddress(mac)
                .build();

        if (forceStopScann == false) {
            Observable<ScanResult> scanObservable = rxBleClient.scanBleDevices(scanSettings); //Filter go here, like -> .scanBleDevices(scanSettings, scanFilters)
            scanDisposable = scanObservable.subscribe(
                    scanResult -> {
                        Log.e("bleee", "Scanning for device with this mac: " + mac);
                        if(forceStopScann == true){
                            return;
                        }
                        if (scanResult.getBleDevice().getMacAddress().equals(mac)) { //Check if the MAC address of the scanned device matches the target MAC address
                            Log.i("bleee", "Found compatible Device _-_-_-_-_-_-_-_-_-_-_-_-_");
                            scanDisposable.dispose(); //Stop scanning and connect to the device
                            RxBleDevice device = scanResult.getBleDevice();
                            rxConnection = device.establishConnection(false).subscribe(
                                    rxBleConnection -> { //Connection established
                                        scanDisposable.dispose(); //Stop Scanning
                                        transmit = rxBleConnection; isconn = true;
                                        device.observeConnectionStateChanges().subscribe(
                                                connectionState -> {
                                                    if (connectionState == RxBleConnection.RxBleConnectionState.DISCONNECTED) {
                                                        //BLE device disconnected - start scanning again
                                                        if(wait == false){ //Without this bug will happens, it will call too much parallel startScanAndConnect() and that's but because uses a lot of battery.
                                                            Log.i("bleee", "no conn, so starting scan again");
                                                            startScanAndConnect();
                                                            isconn = false;
                                                            wait = true;
                                                        }
                                                    } else {
                                                        Log.i("bleee", "already conn, so stopping scan");
                                                        isconn = true;
                                                        wait = false;
                                                        scanDisposable.dispose();
                                                    }
                                                }, throwable -> { } //Error handling
                                        );
                                    },
                                    throwable -> { isconn = false; } //Connection error
                            );
                        } else if (forceStopScann == true) { scanDisposable.dispose(); }
                    },
                    throwable -> { } //Scan error
            );
        }
    }

    public void disconnect(){
        try{
            rxConnection.dispose();
            isconn = false;
        } catch (Exception e){ }
    }

    public void setForceStopScann(boolean selection){
        forceStopScann = selection;
    }

    @SuppressLint("CheckResult")
    public boolean isConnected(){
        return isconn;
    }

    @SuppressLint("CheckResult")
    public void writeMessage(String message) {
        transmit.writeCharacteristic(uuid, message.getBytes())
                .subscribe(
                        bytes -> { //Message sent successfully
                            //Log.d("bleee", "Conn is OK, so stop scanning (if for some reason is scanning)");
                            scanDisposable.dispose();
                            forceStopScann = true;
                        },
                        throwable -> { forceStopScann = false; } //Error sending message
                );
    }
}

/*
    THESE ARE THE PERMISSIONS NEEDED (copy and paste them on your AndroidManifest.xml):

    <uses-permission-sdk-23 android:name="android.permission.ACCESS_FINE_LOCATION" tools:node="remove" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission-sdk-23 android:name="android.permission.ACCESS_COARSE_LOCATION" tools:node="remove" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
    <uses-permission android:name="android.permission.BLUETOOTH"/>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

    REALLY IMPORTANT NOTE: if you are calling the function from a Service, you will need this permission to scan and connect in Background,
    even if the MainActivity UI is opened:

    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" android:maxSdkVersion="30" />
 */
