package com.example.mylocation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.example.mylocation.overlayutil.PoiOverlay
import kotlinx.android.synthetic.main.activity_main4.*

class Fruit(val key : String, val city : String, val dis : String, val mslatitude : Double, val mlongitude : Double)

class FruitAdapter(activity5: MainActivity5, val resourceid : Int, data : List<Fruit>) :
        ArrayAdapter<Fruit>(activity5,resourceid,data){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceid, parent,false)
        val fkey : TextView = view.findViewById(R.id.sug_key)
        val fcity : TextView = view.findViewById(R.id.sug_city)
        val fdis : TextView = view.findViewById(R.id.sug_dis)
        val fmslatitude : TextView = view.findViewById(R.id.sug_mLatitudeStr)
        val fmlongitude : TextView = view.findViewById(R.id.sug_mlongituder)
        val fruit = getItem(position)
        if (fruit != null){
            fkey.text = fruit.key
            fcity.text = fruit.city
            fdis.text = fruit.dis
            fmslatitude.text = fruit.mslatitude.toString()
            fmlongitude.text = fruit.mlongitude.toString()
        }
        return view
    }
}

var mslatitude : Double? = null
var mlongitude : Double? = null
var key : String? = null
var city : String? = null
var dis : String? = null

class MainActivity5 : AppCompatActivity() {
    private val fruitlist = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)



       val mSugListView : ListView = findViewById(R.id.sug_list) as ListView
       val  mKeyWordsView : AutoCompleteTextView = findViewById(R.id.editText) as AutoCompleteTextView
        mKeyWordsView.threshold = 1

        var mSuggestionSearch = SuggestionSearch.newInstance()

        val listener = OnGetSuggestionResultListener {

            //处理sug检索结果

            if (it == null || it.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(this@MainActivity5, "未搜索到POI数据", Toast.LENGTH_SHORT).show();
            }

            if (it.error == SearchResult.ERRORNO.NO_ERROR){



                Toast.makeText(this@MainActivity5, "已搜索到POI数据", Toast.LENGTH_SHORT).show()

                val suggest: MutableList<HashMap<String, String>> = ArrayList<HashMap<String, String>>()
                for (info in it.getAllSuggestions()) {
                    if (info.getKey() != null && info.getDistrict() != null && info.getCity() != null) {
                        val map: HashMap<String, String> = HashMap()
                        map["key"] = info.getKey()
                        map["city"] = info.getCity()
                        map["dis"] = info.getDistrict()
                        map["mLatitudeStr"] = info.getPt().latitude.toString()
                        map["mlongitudeStr"] = info.getPt().longitude.toString()
                        //map["mLatitudeStr"] = info.getPt().longitude
                        /*key = info.getKey()
                        city = info.getCity()
                        dis = info.getDistrict()
                        mslatitude = info.getPt().latitude
                        mlongitude = info.getPt().longitude

                        fruitlist.add(Fruit(key.toString(),city.toString(),dis.toString(),
                            mslatitude.toString().toDouble(),mlongitude.toString().toDouble()))*/

                        suggest.add(map)
                    }
                }

                /*val suggests: MutableList<HashMap<String, Double>> = ArrayList<HashMap<String, Double>>()
                for (info in it.getAllSuggestions()) {
                    if (info.getKey() != null && info.getDistrict() != null && info.getCity() != null) {
                        val map: HashMap<String, Double> = HashMap()

                         mslatitude = info.getPt().latitude
                         mlongitude = info.getPt().longitude

                        Log.d("纬度",mslatitude.toString())
                        Log.d("京都",mlongitude.toString())

                    }
                }*/

                val simpleAdapter = SimpleAdapter(
                    applicationContext,
                    suggest,
                    R.layout.item_layout,
                    arrayOf("key", "city", "dis", "mLatitudeStr","mlongitudeStr"),
                    intArrayOf(R.id.sug_key, R.id.sug_city, R.id.sug_dis, R.id.sug_mLatitudeStr,R.id.sug_mlongituder)
                )
                mSugListView.setAdapter(simpleAdapter)
                mSugListView.setOnItemClickListener { parent, view, position, id ->
                    val fruit = suggest[position]
                    var ssweidu = fruit["mLatitudeStr"]
                    var ssjindu = fruit["mlongitudeStr"]
                    Log.d("纬度",ssweidu.toString())
                    Log.d("经度",ssjindu.toString())

                    var bundle = Bundle()
                    bundle.putString("j",ssjindu)
                    bundle.putString("w",ssweidu)
                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)

                    Toast.makeText(this@MainActivity5, fruit.toString(), Toast.LENGTH_SHORT).show()
                }

                /*initFruit()

                val adapter = FruitAdapter(this, R.layout.item_layout, fruitlist)
                mSugListView.adapter = adapter*/

                simpleAdapter.notifyDataSetChanged()

            }
        }

        mSuggestionSearch.setOnGetSuggestionResultListener(listener)



        var scity : Editable? = scity.text
        var sjian : Editable? = sguanjianzi.text

        sou.setOnClickListener {
            mSuggestionSearch.requestSuggestion(
                SuggestionSearchOption()
                    .city(scity.toString())
                    .keyword(sjian.toString())
            )

            mSuggestionSearch.destroy()

            mSuggestionSearch.setOnGetSuggestionResultListener(listener)
        }

    }


}




