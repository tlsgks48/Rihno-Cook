package com.example.rihno_cook.Retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClinet {
    private var ourInstance: Retrofit?= null
    val instance: Retrofit
        get() {
            if(ourInstance == null)
                ourInstance = Retrofit.Builder() // 메뉴2 불러오는것.
                    .baseUrl("http://10.0.3.2:3000/") // 에뮬은 10.0.2.2 , Genymotion는 10.0.3.2 가 localhost주소 , 핸드폰 테더링(본폰) : 192.168.42.186 , 부폰 : 192.168.42.152
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()) // ScalarsConverterFactory, 리싸이클러뷰할때 GsonConverterFactory
                    .build()
            return ourInstance!!
        }
}