package com.example.rihno_cook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.rihno_cook.Adapter.Menu3Adapter
import com.example.rihno_cook.Adapter.Menu4Adapter
import com.example.rihno_cook.Adapter.MyMenu2Adapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.IMenu2API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_menu6_list.*

class Menu6_list : AppCompatActivity() {
    internal var compositeDisposable3 = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API

    override fun onStop() {
        compositeDisposable3.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu6_list)

        iMenu2API = Common.api

        menu6_list_recycler.setHasFixedSize(true)


        if(Common.selected_menu6 == 1){
            menu6_list_name.setText("레시피")
            compositeDisposable3.add(iMenu2API.Menu6_list1(Common.selected_fame_user!!.name,0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu2List ->
                    menu6_list_recycler.layoutManager = GridLayoutManager(this,2)
                    menu6_list_recycler.adapter = MyMenu2Adapter(this, menu2List)
                },
                    {thr ->
                        Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                    }))
        }else if(Common.selected_menu6 == 2){
            menu6_list_name.setText("쿡TV")
            compositeDisposable3.add(iMenu2API.Menu6_list2(Common.selected_fame_user!!.name,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu2List ->
                    menu6_list_recycler.layoutManager = GridLayoutManager(this,2)
                    menu6_list_recycler.adapter = Menu3Adapter(this, menu2List)
                },
                    {thr ->
                        Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                    }))
        }else if(Common.selected_menu6 == 3){
            menu6_list_name.setText("토크")
            compositeDisposable3.add(iMenu2API.Menu6_list3(Common.selected_fame_user!!.name,2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu2List ->
                    menu6_list_recycler.layoutManager = LinearLayoutManager(this)
                    menu6_list_recycler.adapter = Menu4Adapter(this, menu2List)
                },
                    {thr ->
                        Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                    }))
        }else if(Common.selected_menu6 == 4){
            menu6_list_name.setText("댓글")
        }else if(Common.selected_menu6 == 5){
            menu6_list_name.setText("나의 관심")
        }



    }
}
