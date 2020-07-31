package com.example.rihno_cook.Presenter

import android.content.Intent
import com.example.rihno_cook.Login
import com.example.rihno_cook.MainActivity
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.View.ILoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter (internal var iLoginView: ILoginView) : ILoginPresenter{

    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()


    override fun onLogin(email: String, password: String) {

        //Inot API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

// 로그인 액티비티의 콘테스트를 끌고와야함
        compositeDisposable.add(myAPI.loginUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                if(message.contains("pw")) { // encrypted_password 대신 pw를, salt?
                    iLoginView.onLoginResult("Login success")

                    val nextIntent = Intent(Login.applicationContext(), MainActivity::class.java)
                    Login.applicationContext().startActivity(nextIntent)

                }
                else
                    iLoginView.onLoginResult(message)
            })

/*        var user = User(email, password)
        val isLoginSuccess = user.isDataValid
        if(isLoginSuccess)
            iLoginView.onLoginResult("Login success")
        else
            iLoginView.onLoginResult("Login error")*/

    }



}