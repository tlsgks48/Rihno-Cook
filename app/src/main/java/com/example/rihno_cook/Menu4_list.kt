package com.example.rihno_cook

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rihno_cook.Adapter.Menu4CommentAdapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Comment
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_menu4_list.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Menu4_list : AppCompatActivity() {

    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API
    lateinit var myAPI: INodeJS

    // 댓글 리스트
    var commentList = ArrayList<Comment>()

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu4_list)

        // retrofit
        iMenu2API = Common.api

        //삭제를 위한 리트로핏 API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // 현재날짜
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)

        // toolbar
        detail_talk_toolbar.title = "쿡토크 상세보기"
        setSupportActionBar(detail_talk_toolbar)

        // 상세보기 초기 설정
        detail_talk_user.setText(Common.selected_talk!!.user)
        detail_talk_day.setText(Common.selected_talk!!.day)
        detail_talk_text.setText(Common.selected_talk!!.text)
        Glide.with(this).load(Common.selected_talk!!.image).into(detail_talk_image)

        //댓글부분
        detail_talk_recycler.setHasFixedSize(true)
        detail_talk_recycler.layoutManager = LinearLayoutManager(this)

        // 댓글 불러오기
        compositeDisposable.add(iMenu2API.commentList4(Common.selected_talk!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ CommentList ->
                detail_talk_recycler.adapter = Menu4CommentAdapter(this, CommentList)
                // 댓글 전송 버튼 누른다면
                detail_talk_comment_submit.setOnClickListener {
                    CommentList.add(0,
                        Comment(
                            Common.selected_fame_user!!.name!!,
                            Common.selected_talk!!.id,
                            detail_talk_comment.text.toString(),
                            formatted.toString()
                        )
                    )
                    detail_talk_recycler.adapter?.notifyDataSetChanged()

                    // 댓글 서버로 전송
                    compositeDisposable.add(myAPI.recipe_comment4(Common.selected_fame_user!!.name!!, Common.selected_talk!!.id, detail_talk_comment.text.toString(), formatted.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ Cid ->
                            CommentList.get(CommentList.size-1).id = Cid
                            //Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                        })
                    // 댓글 서버로 전송끝

                    detail_talk_comment.setText("")
                }
                // 댓글 전송 버튼 누른다면 끝

            },
                {thr ->
                    Toast.makeText(this,"등록된 댓글이 없습니다. ",Toast.LENGTH_SHORT).show()
                    detail_talk_recycler.adapter = Menu4CommentAdapter(this, commentList)
                    // 댓글 전송 버튼 누른다면
                    detail_talk_comment_submit.setOnClickListener {
                        commentList.add(0,
                            Comment(
                                Common.selected_fame_user!!.name!!,
                                Common.selected_talk!!.id,
                                detail_talk_comment.text.toString(),
                                formatted.toString()
                            )
                        )
                        detail_talk_recycler.adapter?.notifyDataSetChanged()
                        //Toast.makeText(this,""+commentList.get(commentList.size-1).id,Toast.LENGTH_SHORT).show()

                        // 댓글 서버로 전송
                        compositeDisposable.add(myAPI.recipe_comment4(Common.selected_fame_user!!.name!!, Common.selected_talk!!.id, detail_talk_comment.text.toString(), formatted.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe{ Cid ->
                                commentList.get(commentList.size-1).id = Cid
                                //Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            })
                        // 댓글 서버로 전송끝

                        detail_talk_comment.setText("")
                    }
                    // 댓글 전송 버튼 누른다면 끝
                }))
        // 댓글 불러오기 끝

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_menu, menu)
        if(Common.selected_talk!!.user.equals(Common.selected_fame_user!!.name)) { // 작성자와 로그인한 유저가 같다면 수정, 삭제가 보이게

        } else {
            // 아니라면 수정, 삭제가 안보이게
            menu!!.getItem(2).setVisible(false) // 2가 수정, 3이 삭제, 0이 관심, 1이 댓글
            menu.getItem(3).setVisible(false)
        }
        menu!!.getItem(1).setVisible(false)
        compositeDisposable.add(myAPI.recipe_good_first4(Common.selected_fame_user!!.name!!,Common.selected_talk!!.id)
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
        when (item?.itemId) {
            R.id.recipe_toolbar_good -> {
                if (item.icon.alpha == 255) {
                    item.setIcon(R.drawable.fullheart)
                    item.icon.alpha = 250
                    compositeDisposable.add(myAPI.recipe_good4(
                        Common.selected_fame_user!!.name!!,
                        Common.selected_talk!!.id
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { message ->
                        })

                } else if (item.icon.alpha == 250) {
                    item.setIcon(R.drawable.heart)
                    item.icon.alpha = 255
                    compositeDisposable.add(myAPI.recipe_good_delete4(
                        Common.selected_fame_user!!.name!!,
                        Common.selected_talk!!.id
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { message ->
                        })
                }
                return true
            }
            R.id.recipe_toolbar_upate -> {
                //Toast.makeText(this, "수정", Toast.LENGTH_LONG).show()
                val nextIntent = Intent(this, Talk_update::class.java)
                startActivity(nextIntent)
                return true
            }
            R.id.recipe_toolbar_delete -> {
                //Toast.makeText(this, "삭제", Toast.LENGTH_LONG).show()
                var dialog = AlertDialog.Builder(this)
                dialog.setTitle("쿡토크 삭제")
                dialog.setMessage("쿡토크를 삭제 하시겠습니까?")
                //삭제
                fun d_p() {
                    compositeDisposable.add(myAPI.recipe_delete4(Common.selected_talk!!.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { message ->
                            Toast.makeText(this@Menu4_list, message, Toast.LENGTH_SHORT).show()
                        })
                    val nextIntent = Intent(this, MainActivity::class.java)
                    nextIntent.putExtra("번호",3)
                    startActivity(nextIntent)
                }
                var dialog_listner = object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE ->
                                d_p()
                        }
                    }
                }
                dialog.setPositiveButton("확인", dialog_listner)
                dialog.setNeutralButton("Cancel", null)
                dialog.show()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }

        }
    }
}
