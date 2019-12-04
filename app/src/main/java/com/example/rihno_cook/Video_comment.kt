package com.example.rihno_cook

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.rihno_cook.Adapter.Menu3CommentAdapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Comment
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_comment.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Video_comment : AppCompatActivity() {

    // 서버
    internal var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: INodeJS
    internal lateinit var iMenu2API: IMenu2API

    // 댓글 리스트
    var commentList = ArrayList<Comment>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_comment)

        //retrofit API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)
        // retrofit2
        iMenu2API = Common.api

        // 현재날짜
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)

        Menu3_comment_recycler.setHasFixedSize(true)
        Menu3_comment_recycler.layoutManager = LinearLayoutManager(this)

        // 댓글 불러오기
        compositeDisposable.add(iMenu2API.commentList3(Common.selected_video!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ CommentList ->
                Menu3_comment_recycler.adapter = Menu3CommentAdapter(this, CommentList)
                // 댓글 전송 버튼 누른다면
                Menu3_comment_submit.setOnClickListener {
                    CommentList.add(
                        Comment(
                            Common.selected_fame_user!!.name!!,
                            Common.selected_video!!.id,
                            Menu3_comment_comment.text.toString(),
                            formatted.toString()
                        )
                    )
                    Menu3_comment_recycler.adapter?.notifyDataSetChanged()

                    // 댓글 서버로 전송
                    compositeDisposable.add(myAPI.recipe_comment3(Common.selected_fame_user!!.name!!, Common.selected_video!!.id, Menu3_comment_comment.text.toString(), formatted.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ Cid ->
                            CommentList.get(CommentList.size-1).id = Cid
                            //Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                        })
                    // 댓글 서버로 전송끝

                    Menu3_comment_comment.setText("")
                }
                // 댓글 전송 버튼 누른다면 끝

            },
                {thr ->
                    Toast.makeText(this,"등록된 댓글이 없습니다. ",Toast.LENGTH_SHORT).show()
                    Menu3_comment_recycler.adapter = Menu3CommentAdapter(this, commentList)
                    // 댓글 전송 버튼 누른다면
                    Menu3_comment_submit.setOnClickListener {
                        commentList.add(
                            Comment(
                                Common.selected_fame_user!!.name!!,
                                Common.selected_video!!.id,
                                Menu3_comment_comment.text.toString(),
                                formatted.toString()
                            )
                        )
                        Menu3_comment_recycler.adapter?.notifyDataSetChanged()
                        //Toast.makeText(this,""+commentList.get(commentList.size-1).id,Toast.LENGTH_SHORT).show()

                        // 댓글 서버로 전송
                        compositeDisposable.add(myAPI.recipe_comment3(Common.selected_fame_user!!.name!!, Common.selected_video!!.id, Menu3_comment_comment.text.toString(), formatted.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe{ Cid ->
                                commentList.get(commentList.size-1).id = Cid
                                //Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            })
                        // 댓글 서버로 전송끝

                        Menu3_comment_comment.setText("")
                    }
                    // 댓글 전송 버튼 누른다면 끝
                }))
        // 댓글 불러오기 끝
    }
}
