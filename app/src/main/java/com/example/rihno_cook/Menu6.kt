package com.example.rihno_cook

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.IMenu2API
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu6)

        iMenu2API = Common.api

        // toolbar
        profile_toolbar.title = "내 정보"
        setSupportActionBar(profile_toolbar)

        profil_name.setText(Common.selected_fame_user!!.name)

        compositeDisposable3.add(
            iMenu2API.Menu6_Info(Common.selected_fame_user!!.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu6List ->
                    profil_s1_number.setText("" + menu6List[0].cnt)
                    profil_s2_number.setText("" + menu6List[1].cnt)
                    profil_s3_number.setText("" + menu6List[2].cnt)
                    //profil_s4_number.setText("" + menu6List[3].cnt)
                    profil_s5_number.setText("" + menu6List[4].cnt)
                    //Toast.makeText(this,"첫 : "+menu6List[0].cnt+" 둘 : "+menu6List[1].cnt,Toast.LENGTH_SHORT).show()

                    // 레시피
                    save1.setOnClickListener {
                        Menu6_list_Info(menu6List[0].cnt,1)
                    }
                    // 쿡tv
                    save2.setOnClickListener {
                        Menu6_list_Info(menu6List[1].cnt,2)
                    }
                    // 토크
                    save3.setOnClickListener {
                        Menu6_list_Info(menu6List[2].cnt,3)
                    }
                    // 댓글

/*                    save4.setOnClickListener {
                        Menu6_list_Info(menu6List[3].cnt,4)
                    }*/
                    // 나의관심
                    save5.setOnClickListener {
                        Menu6_list_Info(menu6List[4].cnt,5)
                    }
                },
                    { thr ->
                        Toast.makeText(this, "" + thr.message, Toast.LENGTH_SHORT).show()
                    })
        )

        profil_create_text.setOnClickListener {
            val enter_name_view = LayoutInflater.from(this@Menu6)
                .inflate(R.layout.menu6_dialog, null)

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("나의소개 변경")
            dialog.setView(enter_name_view)
            //삭제
            fun d_p() {
                val edit_name = enter_name_view.findViewById<View>(R.id.menu6_list_editText1) as EditText
                profil_text.setText(edit_name.text.toString())
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
            dialog.setNeutralButton("취소",null)
            dialog.show()
        }
    }
    // 메인끝.

    fun Menu6_list_Info(cnt:Int, a:Int) {
        Common.selected_menu6 = a
        if(cnt > 0) {
            val nextIntent = Intent(this, Menu6_list::class.java)
            startActivity(nextIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val enter_name_view = LayoutInflater.from(this@Menu6)
            .inflate(R.layout.menu6_dialog, null)

        when(item?.itemId ) {
            R.id.profil_toolbar_NickUpate -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("닉네임 변경")
                dialog.setView(enter_name_view)
                //삭제
                fun d_p() {
                    val edit_name = enter_name_view.findViewById<View>(R.id.menu6_list_editText1) as EditText
                    profil_name.setText(edit_name.text.toString())
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
                dialog.setNeutralButton("취소",null)
                dialog.show()
                //Toast.makeText(this, "닉네임 수정", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.profil_toolbar_logout -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("로그아웃")
                dialog.setMessage("로그아웃 하시겠습니까?")
                //삭제
                fun d_p() {
                    val nextIntent = Intent(this, Login::class.java)
                    startActivity(nextIntent)
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
                dialog.setNeutralButton("취소",null)
                dialog.show()
                //Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.profil_toolbar_destroy -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("회원탈퇴")
                dialog.setMessage("정말 회원탈퇴를 하시겠습니까?")
                //삭제
                fun d_p() {
                    val nextIntent = Intent(this, Login::class.java)
                    startActivity(nextIntent)
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
                dialog.setNeutralButton("취소",null)
                dialog.show()
                //Toast.makeText(this, "회원탈퇴", Toast.LENGTH_LONG).show()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
