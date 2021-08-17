package com.example.mylocation

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.poi.*
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity3 : AppCompatActivity() {

    var mapviews : MapView? = null

    var wei : String? = null
    var jin : String? = null

    var jindu : String? = null
    var weidu : String? = null

    private var locationService: LocationService? = null
    var mLatitudeStr: String? = null
    var mLongitudeStr: String? = null
    var province: String? = null
    var j: EditText? = null
    var w: EditText? = null
    private val mListener: BDLocationListener = object : BDLocationListener {
        var count = 0
        override fun onReceiveLocation(location: BDLocation) {
            mLatitudeStr = java.lang.Double.toString(location.latitude)
            mLongitudeStr = java.lang.Double.toString(location.longitude)

            /*var myData : MyLocationData? = MyLocationData.Builder()
                .direction(location.direction).latitude(location.latitude)
                .longitude(location.longitude).longitude(location.longitude)
                .build()

            mapviews!!.map.setMyLocationData(myData)*/




            wei  = location.latitude.toString()
            jin = location.longitude.toString()

            mapviews!!.map.isMyLocationEnabled = true

            var point = LatLng(location.latitude.toString().toDouble(), location.longitude.toString().toDouble())


            var mMapStatus = MapStatus.Builder()
                .target(point)
                .zoom(18F)
                .build()
            var mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus)
            mapviews!!.map.setMapStatus(mMapStatusUpdate)

            //地图标志
            var pointss = LatLng(location.latitude.toString().toDouble(), location.longitude.toString().toDouble())
            var bitmap : BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.water_drop)

            var options : OverlayOptions = MarkerOptions()
                .position(pointss)
                .icon(bitmap)

            mapviews!!.map.addOverlay(options)



            var circle : CircleOptions = CircleOptions().center(pointss).fillColor(0x80D2E9FF.toInt()).radius(100)
            mapviews!!.map.addOverlay(circle)

            Log.d("精度shi",jin.toString())
            Log.d("纬度shi",wei.toString())

            Log.d(
                MainActivity2.TAG,
                "onReceiveLocation: 纬度$mLatitudeStr    经度 $mLongitudeStr"
            )

            jindu = mLongitudeStr.toString()
            weidu = mLatitudeStr.toString()
            Log.d("精度",mLongitudeStr.toString())
            Log.d("纬度",mLatitudeStr.toString())
            //j!!.setText(mLongitudeStr)
            //w!!.setText(mLatitudeStr)
            count++
            province = location.province
            if ("" != province && locationService != null) {
                locationService!!.stop()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mapviews = findViewById(R.id.maps) as MapView

        tiaozhuan.setOnClickListener {
            Log.d("精度",jindu.toString())
            Log.d("纬度",weidu.toString())
            var bundle = Bundle()
            bundle.putString("j",jindu)
            bundle.putString("w",weidu)
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        zhoubian.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("j",jindu)
            bundle.putString("w",weidu)
            val intent = Intent(this,MainActivity4::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }



        /*var point = LatLng(23.232604, 113.274905)


        var mMapStatus = MapStatus.Builder()
            .target(point)
            .zoom(12F)
            .build()
        var mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus)
        mapviews!!.map.setMapStatus(mMapStatusUpdate)*/





        j = findViewById(R.id.j)
        w = findViewById(R.id.w)
        locationService = LocationService(applicationContext)
        locationService!!.registerListener(mListener)
        //注册监听
        locationService!!.setLocationOption(locationService!!.defaultLocationClientOption)
        locationService!!.start()



        /*dinweiBtn.setOnClickListener(View.OnClickListener {

            if (!isopen()) {
                Toast.makeText(this, "GPS没开", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "开了", Toast.LENGTH_SHORT).show()

            }

            locationService!!.start()
        }
        )*/






    }

    private fun isopen(): Boolean {
        val locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        const val TAG = "MainActivity"
    }

}