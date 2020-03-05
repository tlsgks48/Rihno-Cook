package com.example.rihno_cook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.rihno_cook.Adapter.Menu3Adapter
import com.example.rihno_cook.Adapter.Menu4Adapter
import com.example.rihno_cook.Adapter.MyMenu2Adapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Good
import com.example.rihno_cook.Model.Recipe
import com.example.rihno_cook.Model.Talk
import com.example.rihno_cook.Model.Video
import com.example.rihno_cook.Retrofit.IMenu2API
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_menu6_list.*

class Menu6_list : AppCompatActivity() {
    internal var compositeDisposable3 = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API
    var Tlist = arrayListOf<Talk>()
    var Tlist2 = arrayListOf<Talk>()
    var Tlist3 = arrayListOf<Talk>()

    var Vlist = arrayListOf<Video>()
    var Vlist2 = arrayListOf<Video>()
    var Vlist3 = arrayListOf<Video>()

    var Glist = arrayListOf<Good>()
    var Glist2 = arrayListOf<Good>()
    var Glist3 = arrayListOf<Good>()

    var Rlist = listOf<Recipe>()
    var Rlist2 = listOf<Recipe>()
    var Rlist3 = mutableListOf<Recipe>()

    // Gson
    val gson = Gson()

    var json : String? = null
    var json2 : String? = null
    var json3 : String? = null

    override fun onStop() {
        compositeDisposable3.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu6_list)

        iMenu2API = Common.api

        menu6_list_recycler.setHasFixedSize(true)
        menu6_list_recycler2.setHasFixedSize(true)
        menu6_list_recycler3.setHasFixedSize(true)

        // 나의 관심일때 빼고는 안보이기 위해서.
        menu6_list_Text1.visibility = View.GONE
        menu6_list_Text2.visibility = View.GONE
        menu6_list_Text3.visibility = View.GONE

        // 각 버튼에 맞는 화면을 출력...
        if(Common.selected_menu6 == 1){
            menu6_list_name.setText("레시피")

            // 서버에서 JsonArray형식으로 불러와서 각 List에 맞게 출력한다.
            compositeDisposable3.add(iMenu2API.Menu6_list0(Common.selected_fame_user!!.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu2List ->

                    json = menu2List.get(0).toString()
                    Rlist = gson.fromJson(json, object : TypeToken<ArrayList<Recipe>>() {}.type)

                    menu6_list_recycler.layoutManager = LinearLayoutManager(this)
                    menu6_list_recycler.adapter = MyMenu2Adapter(this, Rlist,2)
                },
                    {thr ->
                        Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                    }))

        }else if(Common.selected_menu6 == 2){
            menu6_list_name.setText("쿡TV")
            compositeDisposable3.add(iMenu2API.Menu6_list0(Common.selected_fame_user!!.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu2List ->

                    json = menu2List.get(1).toString()
                    Vlist = gson.fromJson(json, object : TypeToken<ArrayList<Video>>() {}.type)

                    menu6_list_recycler.layoutManager = LinearLayoutManager(this)
                    menu6_list_recycler.adapter = Menu3Adapter(this, Vlist,2)
                },
                    {thr ->
                        Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                    }))

        }else if(Common.selected_menu6 == 3){
            menu6_list_name.setText("쿡토크")
            compositeDisposable3.add(iMenu2API.Menu6_list0(Common.selected_fame_user!!.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu2List ->

                    json = menu2List.get(2).toString()
                    Tlist = gson.fromJson(json, object : TypeToken<ArrayList<Talk>>() {}.type)

                    menu6_list_recycler.layoutManager = LinearLayoutManager(this)
                    menu6_list_recycler.adapter = Menu4Adapter(this, Tlist,2)
                },
                    {thr ->
                        Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                    }))

        }else if(Common.selected_menu6 == 4){
            menu6_list_name.setText("댓글")
        }else if(Common.selected_menu6 == 5){
            menu6_list_Text1.visibility = View.VISIBLE
            menu6_list_Text2.visibility = View.VISIBLE
            menu6_list_Text3.visibility = View.VISIBLE
            menu6_list_name.setText("나의 관심")
            compositeDisposable3.add(iMenu2API.Menu6_list_good(Common.selected_fame_user!!.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu2List ->
                    json = menu2List.get(0).toString()
                    Glist = gson.fromJson(json, object : TypeToken<ArrayList<Good>>() {}.type)

                    json = menu2List.get(1).toString()
                    Glist2 = gson.fromJson(json, object : TypeToken<ArrayList<Good>>() {}.type)

                    json = menu2List.get(2).toString()
                    Glist3 = gson.fromJson(json, object : TypeToken<ArrayList<Good>>() {}.type)

                    for(i in 0 until Glist.size){
                        compositeDisposable3.add(iMenu2API.Menu6_list_good_list(Glist.get(i).recipe_id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ menuList ->

                                json = menuList.get(0).toString()
                                Rlist2 = gson.fromJson(json, object : TypeToken<ArrayList<Recipe>>() {}.type)
                                Rlist3.add(Rlist2.get(0))

                                menu6_list_recycler.layoutManager = LinearLayoutManager(this)
                                menu6_list_recycler.adapter = MyMenu2Adapter(this, Rlist3,2)
                            },
                                {thr ->
                                    Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                                }))
                    }

                    Log.d("size2",Glist2.size.toString())
                    for (j in 0 until Glist2.size) {
                        compositeDisposable3.add(iMenu2API.Menu6_list_good_list(Glist2.get(j).recipe_id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ menuList2 ->

                                json = menuList2.get(1).toString()
                                Vlist2 = gson.fromJson(json, object : TypeToken<ArrayList<Video>>() {}.type)
                                Vlist3.add(Vlist2.get(0))

                                menu6_list_recycler2.layoutManager = LinearLayoutManager(this)
                                menu6_list_recycler2.adapter = Menu3Adapter(this, Vlist3,2)
                            },
                                {thr ->
                                    Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                                }))
                    }

                    Log.d("size3",Glist3.size.toString())
                    for(h in 0 until Glist3.size) {
                        compositeDisposable3.add(iMenu2API.Menu6_list_good_list(Glist3.get(h).recipe_id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ menuList3 ->

                                json = menuList3.get(2).toString()
                                Tlist2 = gson.fromJson(json, object : TypeToken<ArrayList<Talk>>() {}.type)
                                Tlist3.add(Tlist2.get(0))

                                menu6_list_recycler3.layoutManager = LinearLayoutManager(this)
                                menu6_list_recycler3.adapter = Menu4Adapter(this, Tlist3,2)
                            },
                                {thr ->
                                    Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                                }))
                    }

                },
                    {thr ->
                        Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                    }))
        }



    }
}
