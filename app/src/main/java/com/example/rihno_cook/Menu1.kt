package com.example.rihno_cook

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rihno_cook.Adapter.MyMenu2Adapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Good_Fame
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_menu1.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Menu1.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Menu1.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Menu1 : Fragment() {
    internal var compositeDisposable3 = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API
    lateinit var myAPI: INodeJS

    var GfameList = arrayListOf<Good_Fame>()
    var GcommendList = arrayListOf<Good_Fame>()

    // ctrl+O
    override fun onStop() {
        compositeDisposable3.clear()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_menu1, container, false)
        // retrofit
        iMenu2API = Common.api

        //삭제를 위한 리트로핏 API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // Gson
        val gson = Gson()

        // 인기 레시피 불러오기
        rootView?.recycler_menu1?.setHasFixedSize(true)
        rootView?.recycler_menu1?.layoutManager = LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)

        // 인기 레시피 스와이프 리프레쉬
        rootView?.menu1_swipe_refresh?.setOnRefreshListener {
            if(Common.isConnectedToInternet(activity))
            {
                // 인기있는 레시피를 최대 3개 조회
                compositeDisposable3.add(myAPI.good_fame()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{ message ->
                        val json : String? = message
                        if(json != null && json !="") {
                            GfameList = gson.fromJson(json, object : TypeToken<ArrayList<Good_Fame>>() {}.type)
                        }
                        //Toast.makeText(activity,"GfameList size : "+GfameList.size, Toast.LENGTH_SHORT).show()
                        rootView.menu1_swipe_refresh.isRefreshing = false
                        if(GfameList.size == 1) {
                            compositeDisposable3.add(iMenu2API.Menu1_fameList(GfameList.get(0).recipe_id,0,0)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ menu2List ->
                                    rootView.recycler_menu1.adapter = MyMenu2Adapter(activity, menu2List,1)
                                },
                                    {thr ->
                                        Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                    }))
                        }
                        else if(GfameList.size == 2) {
                            compositeDisposable3.add(iMenu2API.Menu1_fameList(GfameList.get(0).recipe_id,GfameList.get(1).recipe_id,0)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ menu2List ->
                                    rootView.recycler_menu1.adapter = MyMenu2Adapter(activity, menu2List,1)
                                },
                                    {thr ->
                                        Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                    }))
                        }
                        else if(GfameList.size == 3) {
                            compositeDisposable3.add(iMenu2API.Menu1_fameList(GfameList.get(0).recipe_id,GfameList.get(1).recipe_id,GfameList.get(2).recipe_id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ menu2List ->
                                    rootView.recycler_menu1.adapter = MyMenu2Adapter(activity, menu2List,1)
                                },
                                    {thr ->
                                        Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                    }))
                        }
                    })
                // 3개 최대 조회 끝
            }
            else
            {
                Toast.makeText(activity,"Please check your connection1",Toast.LENGTH_SHORT).show()
            }
        }

        //Default , load first time
        rootView?.menu1_swipe_refresh?.post(Runnable{
            if(Common.isConnectedToInternet(activity))
            {
                // 인기있는 레시피를 최대 3개 조회
                compositeDisposable3.add(myAPI.good_fame()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{ message ->
                        val json : String? = message
                        if(json != null && json !="") {
                            GfameList = gson.fromJson(json, object : TypeToken<ArrayList<Good_Fame>>() {}.type)
                        }
                        //Toast.makeText(activity,"GfameList size : "+GfameList.size, Toast.LENGTH_SHORT).show()
                        rootView.menu1_swipe_refresh.isRefreshing = false
                        if(GfameList.size == 1) {
                            compositeDisposable3.add(iMenu2API.Menu1_fameList(GfameList.get(0).recipe_id,0,0)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ menu2List ->
                                    rootView.recycler_menu1.adapter = MyMenu2Adapter(activity, menu2List,1)
                                },
                                    {thr ->
                                        Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                    }))
                        }
                        else if(GfameList.size == 2) {
                            compositeDisposable3.add(iMenu2API.Menu1_fameList(GfameList.get(0).recipe_id,GfameList.get(1).recipe_id,0)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ menu2List ->
                                    rootView.recycler_menu1.adapter = MyMenu2Adapter(activity, menu2List,1)
                                },
                                    {thr ->
                                        Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                    }))
                        }
                        else if(GfameList.size == 3) {
                            compositeDisposable3.add(iMenu2API.Menu1_fameList(GfameList.get(0).recipe_id,GfameList.get(1).recipe_id,GfameList.get(2).recipe_id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ menu2List ->
                                    rootView.recycler_menu1.adapter = MyMenu2Adapter(activity, menu2List,1)
                                },
                                    {thr ->
                                        Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                    }))
                        }
                    })
            }
            else
            {
                Toast.makeText(activity,"Please check your connection1",Toast.LENGTH_SHORT).show()
            }
        })
        // 인기 스와이프 끝

        rootView?.recycler_menu1_2?.setHasFixedSize(true)
        rootView?.recycler_menu1_2?.layoutManager = LinearLayoutManager(activity!!,LinearLayoutManager.HORIZONTAL,false)

        // 유저 불러오기
        compositeDisposable3.add(iMenu2API.LoginUserList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ LoginUser ->
                Common.selected_fame_user = LoginUser[0]
                //Toast.makeText(activity, "인기 유저는 "+Common.selected_fame_user!!.name,Toast.LENGTH_SHORT).show()
                // 추천 스와이프
                rootView?.menu1_swipe_refresh2?.setOnRefreshListener{
                    if(Common.isConnectedToInternet(activity))
                    {
                        compositeDisposable3.add(myAPI.good_commend(Common.selected_fame_user!!.name!!)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({ message ->
                                val json: String? = message
                                if (json != null && json != "") {
                                    GcommendList = gson.fromJson(json, object : TypeToken<ArrayList<Good_Fame>>() {}.type)
                                }
                                //Toast.makeText(activity,"GcommendList size : "+GcommendList.size, Toast.LENGTH_SHORT).show()
                                rootView.menu1_swipe_refresh2.isRefreshing = false
                                // 1. 유저 관심 불러오기 2. 관심 불러온 카테고리로 메뉴에 있는 레시피들 불러오기 3. 관심한 레시피와 중복되지 않게하기
                                // 유저 관심 종류가 1, 2, 3에 따라
                                if(GcommendList.size == 1){
                                    compositeDisposable3.add(iMenu2API.Menu1_commendList(GcommendList.get(0).category0,0,0)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ menu2List ->
                                            rootView.recycler_menu1_2.adapter = MyMenu2Adapter(activity, menu2List,1)
                                        },
                                            {thr ->
                                                Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                            }))
                                } else if(GcommendList.size == 2){
                                    compositeDisposable3.add(iMenu2API.Menu1_commendList(GcommendList.get(0).category0,GcommendList.get(1).category0,0)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ menu2List ->
                                            rootView.recycler_menu1_2.adapter = MyMenu2Adapter(activity, menu2List,1)
                                        },
                                            {thr ->
                                                Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                            }))
                                } else if(GcommendList.size >= 3) {
                                    compositeDisposable3.add(iMenu2API.Menu1_commendList(GcommendList.get(0).category0,GcommendList.get(1).category0,GcommendList.get(2).category0)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ menu2List ->
                                            rootView.recycler_menu1_2.adapter = MyMenu2Adapter(activity, menu2List,1)
                                        },
                                            {thr ->
                                                Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                            }))
                                }
                            },
                                {thr ->
                                    Toast.makeText(activity,"관심 체크한 레시피가 없습니다. "+thr.message,Toast.LENGTH_SHORT).show()
                                }))
                    }
                    else {
                        Toast.makeText(activity,"Please check your connection1_2",Toast.LENGTH_SHORT).show()
                    }
                }

                rootView?.menu1_swipe_refresh2?.post(Runnable {
                    if(Common.isConnectedToInternet(activity))
                    {
                        compositeDisposable3.add(myAPI.good_commend(Common.selected_fame_user!!.name!!)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({ message ->
                                val json: String? = message
                                if (json != null && json != "") {
                                    GcommendList = gson.fromJson(json, object : TypeToken<ArrayList<Good_Fame>>() {}.type)
                                }
                                //Toast.makeText(activity,"GcommendList size : "+GcommendList.size, Toast.LENGTH_SHORT).show()
                                rootView.menu1_swipe_refresh2.isRefreshing = false
                                // 1. 유저 관심 불러오기 2. 관심 불러온 카테고리로 메뉴에 있는 레시피들 불러오기 3. 관심한 레시피와 중복되지 않게하기
                                // 유저 관심 종류가 1, 2, 3에 따라
                                if(GcommendList.size == 1){
                                    compositeDisposable3.add(iMenu2API.Menu1_commendList(GcommendList.get(0).category0,0,0)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ menu2List ->
                                            rootView.recycler_menu1_2.adapter = MyMenu2Adapter(activity, menu2List,1)
                                        },
                                            {thr ->
                                                Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                            }))
                                } else if(GcommendList.size == 2){
                                    compositeDisposable3.add(iMenu2API.Menu1_commendList(GcommendList.get(0).category0,GcommendList.get(1).category0,0)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ menu2List ->
                                            rootView.recycler_menu1_2.adapter = MyMenu2Adapter(activity, menu2List,1)
                                        },
                                            {thr ->
                                                Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                            }))
                                } else if(GcommendList.size >= 3) {
                                    compositeDisposable3.add(iMenu2API.Menu1_commendList(GcommendList.get(0).category0,GcommendList.get(1).category0,GcommendList.get(2).category0)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ menu2List ->
                                            rootView.recycler_menu1_2.adapter = MyMenu2Adapter(activity, menu2List,1)
                                        },
                                            {thr ->
                                                Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                                            }))
                                }
                            },
                                {thr ->
                                    Toast.makeText(activity,"관심 체크한 레시피가 없습니다. "+thr.message,Toast.LENGTH_SHORT).show()
                                }))
                    }
                    else {
                        Toast.makeText(activity,"Please check your connection1_2",Toast.LENGTH_SHORT).show()
                    }
                })
            })
        // 유저 불러오기 끝

        return rootView
    }

}
