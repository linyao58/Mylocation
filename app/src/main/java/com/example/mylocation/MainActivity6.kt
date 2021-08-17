package com.example.mylocation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater.from
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.search.route.*
import com.example.mylocation.overlayutil.DrivingRouteOverlay
import com.example.mylocation.overlayutil.WalkingRouteOverlay
import kotlinx.android.synthetic.main.activity_main5.*
import java.util.Date.from

var jsmapViews : MapView? = null

class MainActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        jsmapViews = findViewById(R.id.jsmap) as MapView

        var mSearch = RoutePlanSearch.newInstance()

        val listener : OnGetRoutePlanResultListener = object : OnGetRoutePlanResultListener{
            override fun onGetIndoorRouteResult(p0: IndoorRouteResult?) {
                TODO("Not yet implemented")
            }

            override fun onGetTransitRouteResult(p0: TransitRouteResult?) {
                TODO("Not yet implemented")
            }

            override fun onGetDrivingRouteResult(drivingRouteResult: DrivingRouteResult?) {
               /* var overlay : DrivingRouteOverlay = DrivingRouteOverlay(jsmapViews!!.map)
                if (drivingRouteResult != null) {
                    if (drivingRouteResult.getRouteLines().size > 0){
                        overlay.setData(drivingRouteResult.getRouteLines().get(0))
                    overlay.addToMap()
                    }

                }*/
            }

            override fun onGetWalkingRouteResult(walkingRouteResult: WalkingRouteResult?) {
                var overlay : WalkingRouteOverlay = WalkingRouteOverlay(jsmapViews!!.map)
                if (walkingRouteResult != null) {
                    if (walkingRouteResult.getRouteLines().size > 0){
                        overlay.setData(walkingRouteResult.getRouteLines().get(0))
                        overlay.addToMap()
                    }

                }
            }

            override fun onGetMassTransitRouteResult(p0: MassTransitRouteResult?) {
                TODO("Not yet implemented")
            }

            override fun onGetBikingRouteResult(p0: BikingRouteResult?) {
                TODO("Not yet implemented")
            }

        }

        mSearch.setOnGetRoutePlanResultListener(listener)



        bt_jiache.setOnClickListener {

            var cityst = city12.text.toString()
            var dingmingst = diming1.text.toString()
            var cityen = city123.text.toString()
            var dingmingen = diming12.text.toString()

            var stNode : PlanNode = PlanNode.withCityNameAndPlaceName(cityst, dingmingst)
            var enNode : PlanNode = PlanNode.withCityNameAndPlaceName(cityen, dingmingen)

            mSearch.walkingSearch/*drivingSearch*/(
                // DrivingRoutePlanOption()
                WalkingRoutePlanOption()
                    .from(stNode)
                    .to(enNode)
            )


            mSearch.destroy()

            mSearch.setOnGetRoutePlanResultListener(listener)
        }


    }
}