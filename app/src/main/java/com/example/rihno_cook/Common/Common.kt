package com.example.rihno_cook.Common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.rihno_cook.Model.*
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import com.example.rihno_cook.Retrofit.RetrofitClinet2
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
//import com.example.rihno_cook.Retrofit.RetrofitClient
import java.net.ConnectException

object Common {
    lateinit var myAPI:INodeJS
    internal var compositeDisposable0 = CompositeDisposable()

    var selected_recipe: Recipe?=null
    var selected_recipe_user: LoginUser?=null
    var selected_fame_user: LoginUser?=null
    var selected_video: Video?=null
    var selected_talk: Talk?=null

    fun isConnectedToInternet(context: Context?) : Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm!=null)
        {
            if (Build.VERSION.SDK_INT < 23)
            {
                val ni = cm.activeNetworkInfo
                if(ni!=null)
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI ||
                            ni.type == ConnectivityManager.TYPE_MOBILE)
            }
            else
            {
                val n = cm.activeNetwork
                if(n!=null)
                {
                    val nc = cm.getNetworkCapabilities(n)
                    return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                }
            }
        }
        return false
    }

    val api:IMenu2API
    get() {
        val retrofit =  RetrofitClinet2.instance
        return retrofit.create(IMenu2API::class.java)
    }

    fun gtest1(fu:Observable<String>) {
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        compositeDisposable0.add(myAPI.qtest1(fu).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())

    }

}