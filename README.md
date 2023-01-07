# RxAndroidBle-Easier

## Introduction
This is a class that make **even easier** the Library [RxAndroidBle](https://github.com/dariuszseweryn/RxAndroidBle "RxAndroidBle"). 

I was working for a my project that works with the BLE. So I started surfing the internet until I found this blog [Making Android Ble work](https://medium.com/@martijn.van.welie/making-android-ble-work-part-1-a736dcd53b02 "Making Android Ble work"). In conclusion I decided to choose the RxAndroidBle, because I though it was the easiest, but I noticed how it was not too easy and tidy to use the library. So I decided to create a Class that is based on this library, but makes your project easier and cleaner.

## Gettings started
As I said this file is based on the library in question, so follow the instruction provided by the doc for the installation of the **RxJava 2** version.

### Gradle
If you use Gradle to build your project — as a Gradle project implementation dependency:
```
implementation "com.polidea.rxandroidble2:rxandroidble:1.17.2"
```

### Maven
If you use Maven to build your project — as a Maven project dependency:
```
<dependency>
  <groupId>com.polidea.rxandroidble2</groupId>
  <artifactId>rxandroidble</artifactId>
  <version>1.17.2</version>
  <type>aar</type>
</dependency>
```

## Permissions
These are the basic permissions you need, to scan and connect to the BLE device with this class:
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `BLUETOOTH_ADMIN`
- `BLUETOOTH`

These permissions are added like this in your `AndroidManifest.xml`:
```java
<uses-permission-sdk-23 android:name="android.permission.ACCESS_FINE_LOCATION" tools:node="remove" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission-sdk-23 android:name="android.permission.ACCESS_COARSE_LOCATION" tools:node="remove" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.BLUETOOTH"/>
```
Really important note -> If you are trying to run this class's functions in a `Service` you **MUST** provide the `ACCESS_BACKGROUND_LOCATION`, that can be added in the Manifest with:
```
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" android:maxSdkVersion="30" />
```

As official documentation explain, if you are using a API from 31 you need these permission to connect to the BLE device:
- `android.permission.BLUETOOTH_CONNECT`
- `android.permission.BLUETOOTH_SCAN`

And remeber that some of these permissions need the RunTime Permissions Request, plese follow the [offical documentation](https://developer.android.com/training/permissions/requesting "offical documentation").


## Usage

For now, the class will automatically try to reconnect to the BLE device. In the future, if I feel like it, I will also add the various functions to connect not only with the MAC address but also with the device name. It's all in the TODO at the bottom of this Readme file.
The actual functions:

- Declaring the Client (as github doc):
`RxBleClient rxBleClient = RxBleClient.create(getApplicationContext());`

- Creating the class object:
`rx = new BLEConnRX(rxBleClient Bleclient, String mac, String uuid);`

- Starting and Connecting:
`rx.startScanAndConnect();`

- To check if BLE is connected:
`rx.isConnected();`

- To send a message:
`rx.writeMessage(String message);`

- If for some reason want force stop scanning:
`rx.setForceStopScann(boolean); //true = force stop, false = do not force stop`

- Disconnecting from the BLE device:
`rx.disconnect();`

## TODO
- Add constructor to connect to the BLE device via their name.
- Add a reconnect system that user can choose if enable or disable.
- Check if isConnected() function is working correctly.
- A function that only return scanned devices.
- A function that only try connect to the BLE device.










