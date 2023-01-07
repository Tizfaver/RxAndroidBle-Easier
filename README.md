# RxAndroidBle-Easier

## Introduction
This is a class that make **even easier** the Library [RxAndroidBle](https://github.com/dariuszseweryn/RxAndroidBle "RxAndroidBle"). 
Lately I have found myself using the RxAndoirdBle library. I noticed how it was not too easy and tidy to use the library in question. So I decided to create a Class that is based on this library, but making your project easier and cleaner.

## Gettings started
As I said this file is based on the library in question, so follow the instruction provided by the doc for the installation of the **RxJava 2** version.

###Gradle
If you use Gradle to build your project — as a Gradle project implementation dependency:
```
implementation "com.polidea.rxandroidble2:rxandroidble:1.17.2"
```

###Maven
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
These are the basic permissions you need to scan and connect to the BLE device with this class:
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `BLUETOOTH_ADMIN`
- `BLUETOOTH`

added like this in your `AndroidManifest.xml`:
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













