package com.example.mylocation

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.mapapi.map.CircleOptions
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.example.mylocation.overlayutil.PoiOverlay
import kotlinx.android.synthetic.main.activity_main3.*


var mapviewssss : MapView? = null

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        mapviewssss = findViewById(R.id.mapsss) as MapView

        //POI城市内检索


        var mPoiSearch = PoiSearch.newInstance()





        val listener: OnGetPoiSearchResultListener = object : OnGetPoiSearchResultListener {
            override  fun onGetPoiResult(poiResult: PoiResult) {

                if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(this@MainActivity4, "未搜索到POI数据", Toast.LENGTH_SHORT).show()
                }

                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    //获取POI检索结果
                    Toast.makeText(this@MainActivity4, "已搜索到POI数据", Toast.LENGTH_SHORT).show()
                    mapviewssss!!.map.clear()
                    val poiOverlay = PoiOverlay(mapviewssss!!.map)
                    poiOverlay.setData(poiResult)

                    //将poiOverlay添加至地图并缩放至合适级别
                    poiOverlay.addToMap()
                    poiOverlay.zoomToSpan()
                }
            }
            override   fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult) {}
            override   fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult) {}

            //废弃
            override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult) {}
        }



        mPoiSearch.setOnGetPoiSearchResultListener(listener)

        var citys : Editable? = city.text
        var jian : Editable? = guanjianzi.text

        var bundle = intent.extras
        var j = bundle?.getString("j").toString()
        var w = bundle?.getString("w").toString()

            jiansuo.setOnClickListener {
                mPoiSearch.searchNearby(
                    PoiNearbySearchOption()
                        .location(LatLng(w.toDouble(),j.toDouble()))
                        .radius(1000)
                        //.city(citys.toString()) //必填
                        .keyword(jian.toString()) //必填
                        .pageNum(0)
                )



                mPoiSearch.destroy()

                mPoiSearch.setOnGetPoiSearchResultListener(listener)


            }





    }
}




