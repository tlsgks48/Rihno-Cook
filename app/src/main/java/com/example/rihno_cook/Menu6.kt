package com.example.rihno_cook

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.IMenu2API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.menu6.*

class Menu6 : AppCompatActivity() {
    internal var compositeDisposable3 = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API

    override fun onStop() {
        compositeDisposable3.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu6)

        iMenu2API = Common.api

        // toolbar
        profile_toolbar.title = "내 정보"
        setSupportActionBar(profile_toolbar)

        profil_name.setText(Common.selected_fame_user!!.name)

        compositeDisposable3.add(iMenu2API.Menu6_Info(Common.selected_fame_user!!.name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ menu6List ->
                Toast.makeText(this,"첫 : "+menu6List[0].cnt+" 둘 : "+menu6List[1].cnt,Toast.LENGTH_SHORT).show()
            },
                {thr ->
                    Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                }))

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("레시피 삭제")
        dialog.setMessage("레시피를 삭제 하시겠습니까?")
        //삭제
        fun d_p() {

        }

        val dialog_listner = object : DialogInterface.OnClickListener{
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

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId ) {
            R.id.profil_toolbar_NickUpate -> {
                Toast.makeText(this, "닉수정", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.profil_toolbar_logout -> {
                Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.profil_toolbar_destroy -> {
                Toast.makeText(this, "회원탈퇴", Toast.LENGTH_LONG).show()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
