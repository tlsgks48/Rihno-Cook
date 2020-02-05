package com.example.rihno_cook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.IMenu2API
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // internal var toolbar: Toolbar? = null
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        //Init API
        iMenu2API = Common.api

        //View setup
        //toolbar = findViewById(R.id.toolbar) as Toolbar
        //setSupportActionBar(toolbar)
        //supportActionBar?.setLogo(R.mipmap.ic_launcher)
        //supportActionBar?.setDisplayUseLogoEnabled(true)

        val secondIntent = intent
        secondIntent.getIntExtra("번호", 0)

        //탭 레이아웃
        val fragmentAdapter = FpageAdapter(supportFragmentManager)

        viewpager_main.adapter = fragmentAdapter
        // 뷰페이저 페이지지정
        if(secondIntent.getIntExtra("번호", 0) == 1){
            viewpager_main.currentItem = 1
        }else if(secondIntent.getIntExtra("번호", 0) == 2){
            viewpager_main.currentItem = 2
        }else if(secondIntent.getIntExtra("번호", 0) == 3){
            viewpager_main.currentItem = 3
        }else {
            viewpager_main.currentItem = 0
        }
        tabs.setupWithViewPager(viewpager_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId ) {
            R.id.toolbar_menu6 -> {
                val nextIntent = Intent(this,Menu6::class.java)
                startActivity(nextIntent)
                return true
            }
            R.id.toolbar_search -> {
                val nextIntent = Intent(this,Search::class.java)
                startActivity(nextIntent)
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }

        }
    }
}
