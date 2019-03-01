
package com.baidulocation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.baidu.location.*;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;


public class RNBaiduLocationModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private LocationClient locationClient;

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private BDLocationListener listener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Double latitude = location.getLatitude();    //获取纬度信息
            Double longitude = location.getLongitude(); //获取精度
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String streetNumber = location.getStreetNumber(); //获取街道号
            String cityCode = location.getAdCode();
            WritableMap params = Arguments.createMap();
            params.putDouble("latitude", latitude);
            params.putDouble("longitude", longitude);
            params.putString("country", country);
            params.putString("province", province);
            params.putString("city", city);
            params.putString("district", district);
            params.putString("street", street);
            params.putString("streetNumber", streetNumber);
            params.putString("cityCode", cityCode);
            sendEvent(reactContext, "locationData", params);
            locationClient.stop();
        }
    };

    public RNBaiduLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

    }

    @Override
    public String getName() {
        return "RNBaiduLocation";
    }

    @ReactMethod
    public void init(Promise promise) {
        locationClient = new LocationClient(reactContext);
        locationClient.registerNotifyLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        locationClient.setLocOption(option);
        promise.resolve("1");
    }

    @ReactMethod
    public void check(Promise promise) {
        LocationManager locationManager
                = (LocationManager) this.reactContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        WritableMap params = Arguments.createMap();

        boolean location = ContextCompat.checkSelfPermission(this.reactContext, "android.permission.ACCESS_FINE_LOCATION")
                == PackageManager.PERMISSION_GRANTED;

        params.putBoolean("gpsIsOpen", gps);
        params.putBoolean("gpsPermissionIsOpen", location);
        promise.resolve(params);
    }

    @ReactMethod
    public void toAppSetting(Promise promise) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", this.reactContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", this.reactContext.getPackageName());
        }
        this.reactContext.startActivity(mIntent);
        promise.resolve("1");
    }

    @ReactMethod
    public void toGpsSetting(Promise promise) {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.reactContext.startActivityForResult(locationIntent, 1, null);
        promise.resolve("1");
    }

    @ReactMethod
    public void getLocation(Promise promise) {
        locationClient.start();
        promise.resolve("1");
    }
}