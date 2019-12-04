package com.example.rihno_cook.Retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private var ourInstance:Retrofit?= null
    val instance:Retrofit
        get() {
            if(ourInstance == null)
                ourInstance = Retrofit.Builder() // 로그인, 회원가입 리트로핏!!
                    .baseUrl("http://10.0.3.2:3000/") // 에뮬은 10.0.2.2 , Genymotion는 10.0.3.2 가 localhost주소 , 핸드폰 테더링 : 192.168.42.186 , 부폰 : 192.168.42.152
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory, 리싸이클러뷰할때 GsonConverterFactory
                    .build()
            return ourInstance!!
        }
}