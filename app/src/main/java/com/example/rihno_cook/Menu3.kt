package com.example.rihno_cook


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rihno_cook.Adapter.Menu3Adapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Video
import com.example.rihno_cook.Retrofit.IMenu2API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_menu3.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Menu3 : Fragment() {

    internal var compositeDisposable31 = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API

    // ctrl+O
    override fun onStop() {
        compositeDisposable31.clear()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_menu3, container, false)
        iMenu2API = Common.api

        // 리싸이클러뷰 설정
        rootView?.video_recycler_menu3?.setHasFixedSize(true)
        rootView?.video_recycler_menu3?.layoutManager = GridLayoutManager(activity,2)

        // 스와이프 리프레쉬 설정
        rootView?.video_swipe_refresh?.setOnRefreshListener {
            if(Common.isConnectedToInternet(activity))
            {
                compositeDisposable31.add(iMenu2API.menu3List
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ menu2List ->
                        rootView.video_recycler_menu3.adapter = Menu3Adapter(activity, menu2List,1)
                        rootView.video_swipe_refresh.isRefreshing = false
                    },
                        {thr ->
                            Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                            rootView.video_swipe_refresh.isRefreshing = false
                        }))
            }
            else
            {
                Toast.makeText(activity,"Please check your connection3",Toast.LENGTH_SHORT).show()
            }
        }

        rootView?.video_swipe_refresh?.post(Runnable {
            if(Common.isConnectedToInternet(activity))
            {
                compositeDisposable31.add(iMenu2API.menu3List
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ menu2List ->
                        rootView.video_recycler_menu3.adapter = Menu3Adapter(activity, menu2List,1)
                        rootView.video_swipe_refresh.isRefreshing = false
                    },
                        {thr ->
                            Toast.makeText(activity,""+thr.message,Toast.LENGTH_SHORT).show()
                            rootView.video_swipe_refresh.isRefreshing = false
                        }))
            }
            else
            {
                Toast.makeText(activity,"Please check your connection3",Toast.LENGTH_SHORT).show()
            }
        })

        rootView?.video_uploade_button!!.setOnClickListener {
            //Toast.makeText(activity,"이동", Toast.LENGTH_SHORT).show()
            val nextIntent = Intent(activity, Video_upload::class.java)
            startActivity(nextIntent)
        }
        return rootView
    }


}
