package com.example.rihno_cook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu6_list.*

class Menu6_list : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu6_list)

        menu6_list_recycler.setHasFixedSize(true)
    }
}
