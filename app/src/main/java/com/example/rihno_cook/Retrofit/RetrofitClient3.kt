package com.example.rihno_cook.Retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient3 {
    private var ourInstance: Retrofit?= null
    fun getClient(baseUrl :String):Retrofit {
            if(ourInstance == null)
                ourInstance = Retrofit.Builder() // 업로드 파일 리트로핏 !!
                    .baseUrl("http://10.0.3.2:3000/") // 에뮬은 10.0.2.2 , Genymotion는 10.0.3.2 가 localhost주소 , 핸드폰 테더링 : 192.168.42.186
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverterFactory, 리싸이클러뷰할때 GsonConverterFactory
                    .build()
            return ourInstance!!
        }
}