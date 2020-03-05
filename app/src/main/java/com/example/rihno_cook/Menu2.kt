package com.example.rihno_cook


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rihno_cook.Adapter.MyMenu2Adapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Recipe
import com.example.rihno_cook.Retrofit.IMenu2API
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_menu2.*
import kotlinx.android.synthetic.main.fragment_menu2.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Menu2 : Fragment() {
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API

    // ctrl+O
    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var rootView = inflater.inflate(R.layout.fragment_menu2, container, false)
        iMenu2API = Common.api

        rootView?.recycler_menu2?.setHasFixedSize(true)
        rootView?.recycler_menu2?.layoutManager = GridLayoutManager(activity!!,2)

        // 새로고침 할 경우
        rootView?.swipe_refresh?.setOnRefreshListener {
            if(Common.isConnectedToInternet(activity))
                {
                    fetchMenu2()
                }
                else
            {
                Toast.makeText(activity,"Please check your connection2",Toast.LENGTH_SHORT).show()
            }
        }
        //Default , load first time
        rootView?.swipe_refresh?.post(Runnable {
            if(Common.isConnectedToInternet(activity))
            {
                fetchMenu2()
            }
            else
            {
                Toast.makeText(activity,"Please check your connection2",Toast.LENGTH_SHORT).show()
            }
        })

        rootView.recipe_uploade_button.setOnClickListener {
            val nextIntent = Intent(activity, RecipeUpload::class.java)
            startActivity(nextIntent)
        }

        return rootView
    }

    private fun fetchMenu2(){
        val dialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Pleas wait...")
            .build()
        if (!swipe_refresh.isRefreshing)
            dialog.show()
        compositeDisposable.add(iMenu2API.menu2List
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ menu2List ->
                recycler_menu2.adapter = MyMenu2Adapter(activity, menu2List,1)
                if (!swipe_refresh.isRefreshing)
                    dialog.dismiss()
                swipe_refresh.isRefreshing = false
            },
                {thr ->
                    Toast.makeText(context,""+thr.message,Toast.LENGTH_SHORT).show()
                    if (!swipe_refresh.isRefreshing)
                        dialog.dismiss()
                    swipe_refresh.isRefreshing = false
            }))
    }

}
