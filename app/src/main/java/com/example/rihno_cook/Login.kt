package com.example.rihno_cook

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Presenter.ILoginPresenter
import com.example.rihno_cook.Presenter.LoginPresenter
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import com.example.rihno_cook.View.ILoginView
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.enter_name_layout.*


class Login : AppCompatActivity(), ILoginView {
    override fun onLoginResult(message: String) {
        //Toast.makeText(this@Login,message,Toast.LENGTH_SHORT).show()
    }

    internal lateinit var loginPresenter: ILoginPresenter

    lateinit var myAPI:INodeJS
    var compositeDisposable = CompositeDisposable()

    var check_name : String = ""
    lateinit var context: Context
    init{
        instance = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: Login? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Init
        loginPresenter = LoginPresenter(this)

        //

        //Inot API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // test
        //Common.gtest1(myAPI.loginUser(edit_email.text.toString(),edit_password.text.toString()))

        //Event
        login_button.setOnClickListener {
            login(edit_email.text.toString(),edit_password.text.toString())
            //loginPresenter.onLogin(edit_email.text.toString(),edit_password.text.toString())

            //login_user()
        }


/*        login_button.setOnClickListener {
            login(edit_email.text.toString(),edit_password.text.toString())
        }*/

        register_button.setOnClickListener {
            register() // edit_email.text.toString(),edit_password.text.toString()
        }

    }

    private fun register() { // email: String, password: String

        val enter_name_view = LayoutInflater.from(this@Login)
            .inflate(R.layout.enter_name_layout,null)

        MaterialStyledDialog.Builder(this@Login)
            .setTitle("회원가입")
            .setDescription("회원가입 정보를 입력하세요!")
            .setCustomView(enter_name_view)
            .setHeaderColor(R.color.MacmorrisColorPalette2)
            .setStyle(Style.HEADER_WITH_TITLE)
            .setNegativeText("취소")
            .onNegative{dialog, _ -> dialog.dismiss() }
            .setPositiveText("회원가입")
            .onPositive { _, _ ->
                val edit_name = enter_name_view.findViewById<View>(R.id.edit_name) as EditText
                val edit_dial_email = enter_name_view.findViewById<View>(R.id.edit_dial_email) as EditText
                val edit_dial_password = enter_name_view.findViewById<View>(R.id.edit_dial_password) as EditText
                // email, password 대신, 에디트를
                compositeDisposable.add(myAPI.registerUser(edit_dial_email.text.toString(), edit_name.text.toString(),edit_dial_password.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{ message ->
                            Toast.makeText(this@Login,message,Toast.LENGTH_SHORT).show()
                    })
            }.show()
    }

    private fun login_check(email: String) {
        compositeDisposable.add(myAPI.loginCheck(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                check_name = message
                //Toast.makeText(this@Login,check_name,Toast.LENGTH_SHORT).show()
                login_user(email,check_name)
            })
    }

    private fun login_user(email: String, name: String) {
        compositeDisposable.add(myAPI.login_user(name, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                //Toast.makeText(this@Login,message,Toast.LENGTH_SHORT).show()
            })
    }

    private fun login(email: String, password: String) {
        compositeDisposable.add(myAPI.loginUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                if(message.contains("pw")) { // encrypted_password 대신 pw를, salt?
                    login_check(email)
                    Toast.makeText(this@Login, "Login success!", Toast.LENGTH_SHORT).show()
                    val nextIntent = Intent(this, MainActivity::class.java)
                    startActivity(nextIntent)
                }
                else
                    Toast.makeText(this@Login,message,Toast.LENGTH_SHORT).show()
            })
    }

    override fun onStop() {

        compositeDisposable.clear()
        super.onStop()
    }
}
