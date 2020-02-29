package com.example.rihno_cook

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_menu3_list.*
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class Menu3_list : AppCompatActivity() {

    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API
    lateinit var myAPI: INodeJS

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu3_list)

        // retrofit
        iMenu2API = Common.api

        //삭제를 위한 리트로핏 API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        //
        // toolbar
        detail_video_toolbar.title = "쿡TV 상세보기"
        setSupportActionBar(detail_video_toolbar)
        detail_video_toolbar.hideOverflowMenu()

        menu3_list_videoView.setVideoPath(Common.selected_video!!.video)
        val  mediaController : MediaController = MediaController(this)
        menu3_list_videoView.setMediaController(mediaController)

        menu3_list_videoView.seekTo(10).toString()
       // menu3_list_videoView.start()

        // 제목 ~ 내용
        menu3_list_title.setText(Common.selected_video!!.name)
        menu3_list_writer.setText(Common.selected_video!!.user)
        menu3_list_text.setText(Common.selected_video!!.text)

       //Glide.with(this).load("http://192.168.56.1:3000/달래장.mp4").into(menu3_list_imageView)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_menu, menu)
        if(Common.selected_video!!.user.equals(Common.selected_fame_user!!.name)) { // 작성자와 로그인한 유저가 같다면 수정, 삭제가 보이게

        } else {
            // 아니라면 수정, 삭제가 안보이게
            menu!!.getItem(2).setVisible(false) // 2가 수정, 3이 삭제, 0이 관심, 1이 댓글
            menu.getItem(3).setVisible(false)
        }
        compositeDisposable.add(myAPI.recipe_good_first3(Common.selected_fame_user!!.name!!,Common.selected_video!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                if( message == 1) {
                    menu!!.getItem(0).icon.alpha = 250
                    menu.getItem(0).setIcon(R.drawable.fullheart)
                }
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
                    compositeDisposable.add(myAPI.recipe_good3(Common.selected_fame_user!!.name!!,Common.selected_video!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ message ->
                            //Toast.makeText(this@recipe_detail,message,Toast.LENGTH_SHORT).show()
                        })

                }
                else if(item.icon.alpha == 250) {
                    item.setIcon(R.drawable.heart)
                    item.icon.alpha = 255
                    compositeDisposable.add(myAPI.recipe_good_delete3(Common.selected_fame_user!!.name!!,Common.selected_video!!.id)
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
                val nextIntent = Intent(this, Video_comment::class.java)
                startActivity(nextIntent)
                return true
            }
            R.id.recipe_toolbar_upate -> {
                //Toast.makeText(this, "수정", Toast.LENGTH_LONG).show()
                val nextIntent = Intent(this, Video_update::class.java)
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
                    compositeDisposable.add(myAPI.recipe_delete3(Common.selected_video!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ message ->
                            Toast.makeText(this@Menu3_list,message,Toast.LENGTH_SHORT).show()
                        })

                    val nextIntent = Intent(this, MainActivity::class.java)
                    nextIntent.putExtra("번호",2)
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
