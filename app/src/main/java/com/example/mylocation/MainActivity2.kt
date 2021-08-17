package com.example.mylocation

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.example.mylocation.MainActivity2

class MainActivity2 : AppCompatActivity() {

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
            Log.d(
                TAG,
                "onReceiveLocation: 纬度$mLatitudeStr    经度 $mLongitudeStr"
            )
            j!!.setText(mLongitudeStr)
            w!!.setText(mLatitudeStr)
            count++
            province = location.province
            if ("" != province && locationService != null) {
                locationService!!.stop()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tow)
        var button = findViewById(R.id.dinweiBtn) as Button
        j = findViewById(R.id.j)
        w = findViewById(R.id.w)
        locationService = LocationService(applicationContext)
        locationService!!.registerListener(mListener)
        //注册监听
        locationService!!.setLocationOption(locationService!!.defaultLocationClientOption)
       button.setOnClickListener(View.OnClickListener {
            if (!isopen()) {
                Toast.makeText(this@MainActivity2, "GPS没开", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity2, "开了", Toast.LENGTH_SHORT).show()
            }
            locationService!!.start()
        })
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