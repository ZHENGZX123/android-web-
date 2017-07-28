package kiway.cn.webviewjszhuru.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import kiway.cn.webviewjszhuru.R;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MapActivity extends Activity {
    public static String address = "";//定位的地址
    MapView mMapView = null;
    BaiduMap baiduMap;
    private LocationClient mLocationClient;//定位SDK的核心类
    MyLocationListener myLocationListener;//定位监听
    boolean isFirstLoc = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);
        //以下开启定位服务
        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();//开启定位
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//设置定位间隔
        option.setIsNeedAddress(true);//设置需要地址
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        LatLng ll = new LatLng(100, 100);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }
            address = location.getAddrStr();
            baiduMap.clear();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            LatLng llA = new LatLng(location.getLatitude(), location.getLongitude());
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_location_on);
            MarkerOptions ooA = new MarkerOptions().position(llA).icon(bitmap)
                    .zIndex(9).draggable(true);
            baiduMap.addOverlay(ooA);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }
}
