package com.example.mylocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*


var mapviews : MapView? = null

class MainActivity : AppCompatActivity() {
    //var mapviews : MapView? = null
    var mLocationClient : LocationClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        mapviews = findViewById<View>(R.id.map) as MapView
        //开启普通地图
        //mapviews!!.map.mapType = BaiduMap.MAP_TYPE_NORMAL
        //开启室内图
        //mapviews!!.map.setIndoorEnable(true)
        //地图logo位置
        mapviews!!.setLogoPosition(LogoPosition.logoPostionleftBottom)

        var bundle = intent.extras
        var j = bundle?.getString("j").toString()
        var w = bundle?.getString("w").toString()
        Log.d("精度是123456789123456",j)
        Toast.makeText(this, j, Toast.LENGTH_SHORT).show()
        Log.d("纬度是123456789123456",w)
        Toast.makeText(this, w, Toast.LENGTH_SHORT).show()

        //var point = LatLng(23.143150, 113.02954)

        var point = LatLng(w.toDouble(), j.toDouble())

        var mMapStatus = MapStatus.Builder()
            .target(point)
            .zoom(18F)
            .build()
            var mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus)
            mapviews!!.map.setMapStatus(mMapStatusUpdate)


        //POI城市内检索


        var mPoiSearch = PoiSearch.newInstance()

        val listener: OnGetPoiSearchResultListener = object : OnGetPoiSearchResultListener {
            override fun onGetPoiResult(poiResult: PoiResult) {
                /*if (poiResult.error == SearchResult.ERRORNO.NO_ERROR){
                    var poiOverlay = com.example.mylocation.overlayutil.PoiOverlay(mapviews!!.map)

                    var p : List<PoiAddrInfo> = poiResult.allAddr
                    poiOverlay.setData(poiResult)
                    poiOverlay.addToMap()
                    poiOverlay.zoomToSpan()


                }*/

                var poiname = poiResult.allPoi.get(0).name
                var poiadd = poiResult.allPoi.get(0).address
                var idString = poiResult.allPoi.get(0).uid
            }
            override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult) {}
            override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult) {}

            //废弃
            override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult) {}
        }

        mPoiSearch.setOnGetPoiSearchResultListener(listener)

        mPoiSearch.searchInCity(
            PoiCitySearchOption()

                .city("广州") //必填
                .keyword("美食") //必填
                .pageNum(10)
        )

        /*mPoiSearch.searchNearby(
            PoiNearbySearchOption()
            .location(LatLng(23.232604, 113.274905))
            .radius(100)
            //支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
            .keyword("餐厅")
            .pageNum(10))*/

        mPoiSearch.destroy()


        //地图标志
        /*var point = LatLng(39.944251, 116.494996)
        var bitmap : BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)

        var options : OverlayOptions = MarkerOptions()
            .position(point)
            .icon(bitmap)

        mapviews!!.map.addOverlay(options)*/

        //开启交通图
        /*mapviews!!.map.setTrafficEnabled(true)*/
        //开启热力图
        //mapviews!!.map.setBaiduHeatMapEnabled(true)
       //initLocation()

        checkVersion()
    }

    @SuppressLint("CheckResult")
    private fun checkVersion() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            var rxPermissions =  RxPermissions(this)
            rxPermissions.request(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted: Boolean ->
                    if (granted) { //申请成功
                        //发起连续定位请求
                        initLocation() // 定位初始化
                    } else { //申请失败
                        Toast.makeText(this, "权限未开启", Toast.LENGTH_SHORT).show()
                    }
                }
        }else {
            initLocation();// 定位初始化
        }

    }

    private fun initLocation() {
        //开启定位图层
        mapviews!!.map.isMyLocationEnabled = true

        mLocationClient = LocationClient(applicationContext)
        var myLocationListener : MyLocationListener = MyLocationListener()
        mLocationClient!!.registerLocationListener(myLocationListener)
        var option : LocationClientOption = LocationClientOption()

        //option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy

        option.setOpenGps(true)
        option.setCoorType("bd09ll")
        option.setScanSpan(1000)
        //option.setLocationNotify(true)

        //option.setNeedNewVersionRgc(true)
        mLocationClient!!.locOption = option

        mLocationClient!!.start()
    }

    override fun onResume() {
        super.onResume()
        mapviews?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapviews?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient?.stop()
        mapviews!!.map.setMyLocationEnabled(false)
        mapviews?.onDestroy()
        mapviews = null


    }
}



class MyLocationListener : BDAbstractLocationListener() {
    //private val mapviews: MapView? = null
    var isFirstLoc = true
    override fun onReceiveLocation(location: BDLocation) {

        // MapView 销毁后不在处理新接收的位置
        if (location == null || mapviews == null) {
            return
        }
        val locData = MyLocationData.Builder()
            .accuracy(location.radius) // 设置定位数据的精度信息，单位：米
            .direction(location.direction) // 此处设置开发者获取到的方向信息，顺时针0-360
            .latitude(location.latitude)
            .longitude(location.longitude)
            .build()
        // 设置定位数据, 只有先允许定位图层后设置数据才会生效
        mapviews!!.map.setMyLocationData(locData)

        /*if (isFirstLoc) {
            isFirstLoc = false;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(20.0f);
            mapviews.getMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }*/
    }
}








