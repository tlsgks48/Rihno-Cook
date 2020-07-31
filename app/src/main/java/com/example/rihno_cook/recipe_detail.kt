package com.example.rihno_cook

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.rihno_cook.Adapter.DetailOrderAdapter
import com.example.rihno_cook.Adapter.DetailUnitAdapter
import com.example.rihno_cook.Adapter.DetailUnitAdapter2
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Order
import com.example.rihno_cook.Model.Unit
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import java.util.ArrayList

class recipe_detail : AppCompatActivity() {

    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API
    lateinit var myAPI:INodeJS

    var i : Int = 10000

    // 재료와 요리순서 리스트
    var DUnitList = arrayListOf<Unit>()
    var DOrderList = arrayListOf<Order>()

    // ctrl+O
    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // toolbar
        detail_recipe_toolbar.title = "레시피 상세보기"
        setSupportActionBar(detail_recipe_toolbar)
        detail_recipe_toolbar.hideOverflowMenu()


        // retrofit
        iMenu2API = Common.api

        //삭제를 위한 리트로핏 API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // i = Common.selected_recipe!!.id
        // Toast.makeText(this, "id는 "+ i, Toast.LENGTH_LONG).show()

        // 제목 ~ 팁
        R_detail_title.setText(Common.selected_recipe!!.name)
        R_detail_writer.setText(Common.selected_recipe!!.user)
        R_detail_text.setText(Common.selected_recipe!!.text)
        R_detail_tip.setText(Common.selected_recipe!!.tip)

        // 카테고리 1~4
        R_detail_kind.setText(Common.selected_recipe!!.category1)
        R_detail_count.setText(Common.selected_recipe!!.category2)
        R_detail_time.setText(Common.selected_recipe!!.category3)
        R_detail_level.setText(Common.selected_recipe!!.category4)

        Picasso.get().load(Common.selected_recipe!!.image).into(R_detail_main_image)

        // 재료 추가

        // Gson
        val gson = Gson()

        val json : String? = Common.selected_recipe!!.unit
        if(json != null && json !="") {
            DUnitList = gson.fromJson(json, object : TypeToken<ArrayList<Unit>>() {}.type)
        }

        R_detail_recyc_material.setHasFixedSize(true)
        R_detail_recyc_material.layoutManager = LinearLayoutManager(this)
        R_detail_recyc_material.adapter = DetailUnitAdapter(this,DUnitList)

        R_detail_recyc_order2.setHasFixedSize(true)
        R_detail_recyc_order2.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        R_detail_recyc_order2.adapter = DetailUnitAdapter2(this,DUnitList)

        R_detail_recyc_order2.visibility = View.INVISIBLE

        // 순서 추가
        R_detail_recyc_order.setHasFixedSize(true)
        R_detail_recyc_order.layoutManager = LinearLayoutManager(this)


        // 요리 순서 불러오기
        compositeDisposable.add(iMenu2API.getMenu2_OrderList(Common.selected_recipe!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ DorderList ->
                R_detail_recyc_order.adapter = DetailOrderAdapter(this,DorderList)
                //Toast.makeText(this@Login,message,Toast.LENGTH_SHORT).show()
            })
        // 요리 순서 불러오기 끝

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_menu, menu)
        compositeDisposable.add(iMenu2API.LoginUserList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ LoginUser ->
                Common.selected_recipe_user = LoginUser[0]
                //Toast.makeText(this@recipe_detail,Common.selected_recipe!!.user+"로그인 유저는 : "+LoginUser.get(0).name,Toast.LENGTH_SHORT).show()
                if(Common.selected_recipe!!.user.equals(LoginUser.get(0).name)) { // 작성자와 로그인한 유저가 같다면 수정, 삭제가 보이게

                } else {
                    // 아니라면 수정, 삭제가 안보이게
                    menu!!.getItem(2).setVisible(false) // 2가 수정, 3이 삭제, 0이 관심, 1이 댓글
                    menu.getItem(3).setVisible(false)
                }
                compositeDisposable.add(myAPI.recipe_good_first(Common.selected_recipe_user!!.name!!,Common.selected_recipe!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{ message ->
                        if( message == 1) {
                            menu!!.getItem(0).icon.alpha = 250
                            menu.getItem(0).setIcon(R.drawable.fullheart)
                        }
                    })
                // LoginUser 끝
            })

        return true
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId ) {
            R.id.recipe_toolbar_good -> {
                //val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
                //Toast.makeText(this," 관심 "+item.icon.alpha+", 두번:"+drawable!!.alpha, Toast.LENGTH_LONG).show()
                if(item.icon.alpha == 255) {
                    item.setIcon(R.drawable.fullheart)
                    item.icon.alpha = 250
                    compositeDisposable.add(myAPI.recipe_good(Common.selected_recipe_user!!.name!!,
                        Common.selected_recipe!!.category0,Common.selected_recipe!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ message ->
                            //Toast.makeText(this@recipe_detail,message,Toast.LENGTH_SHORT).show()
                        })

                }
                else if(item.icon.alpha == 250) {
                    item.setIcon(R.drawable.heart)
                    item.icon.alpha = 255
                    compositeDisposable.add(myAPI.recipe_good_delete(Common.selected_recipe_user!!.name!!,Common.selected_recipe!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ message ->
                            //Toast.makeText(this@recipe_detail,message,Toast.LENGTH_SHORT).show()
                        })
                }
                return true
            }
            R.id.recipe_toolbar_comment -> {
                //Toast.makeText(this, "댓글", Toast.LENGTH_LONG).show()
                val nextIntent = Intent(this, recipe_comment::class.java)
                startActivity(nextIntent)
                return true
            }
            R.id.recipe_toolbar_upate -> {
                //Toast.makeText(this, "수정", Toast.LENGTH_LONG).show()
                val nextIntent = Intent(this, recipeUpdate::class.java)
                startActivity(nextIntent)
                return true
            }
            R.id.recipe_toolbar_delete -> {
                //Toast.makeText(this, "삭제", Toast.LENGTH_LONG).show()
                var dialog = AlertDialog.Builder(this)
                dialog.setTitle("레시피 삭제")
                dialog.setMessage("레시피를 삭제 하시겠습니까?")
                //삭제
                fun d_p() {
                    compositeDisposable.add(myAPI.recipe_delete(Common.selected_recipe!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ message ->
                            Toast.makeText(this@recipe_detail,message,Toast.LENGTH_SHORT).show()
                        })

                    compositeDisposable.add(myAPI.recipe_order_delete(Common.selected_recipe!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ message ->
                            //Toast.makeText(this@recipe_detail,message,Toast.LENGTH_SHORT).show()
                        })
                    val nextIntent = Intent(this, MainActivity::class.java)
                    nextIntent.putExtra("번호",1)
                    startActivity(nextIntent)

                }

                var dialog_listner = object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE ->
                                d_p()
                        }
                    }
                }
                dialog.setPositiveButton("확인",dialog_listner)
                dialog.setNeutralButton("Cancel",null)
                dialog.show()

                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }

        }
    }
}
