package com.example.rihno_cook

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.rihno_cook.Adapter.Menu2CommentAdapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Comment
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recipe_comment.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class recipe_comment : AppCompatActivity() {

    // 서버
    internal var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: INodeJS
    internal lateinit var iMenu2API: IMenu2API

    // 댓글 리스트
    var commentList = ArrayList<Comment>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_comment)

        //retrofit API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)
        // retrofit2
        iMenu2API = Common.api

        // 현재날짜
        val onlyDate: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = onlyDate.format(formatter)

        //
        //Toast.makeText(this, "id는 "+ Common.selected_recipe!!.id+", 날짜는 "+onlyDate.toString()+", 유저는 "+ Common.selected_recipe_user!!.name, Toast.LENGTH_LONG).show()

        Menu2_comment_recycler.setHasFixedSize(true)
        Menu2_comment_recycler.layoutManager = LinearLayoutManager(this)
       // Menu2_comment_recycler.adapter = Menu2CommentAdapter(this,commentList)

        // 댓글 불러오기
        compositeDisposable.add(iMenu2API.commentList(Common.selected_recipe!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ CommentList ->
                    Menu2_comment_recycler.adapter = Menu2CommentAdapter(this, CommentList)
                    //Menu2_comment_recycler.adapter?.notifyDataSetChanged()
                    // 댓글 전송 버튼 누른다면
                    Menu2_comment_submit.setOnClickListener {
                        CommentList.add(
                            Comment(
                                Common.selected_recipe_user!!.name!!,
                                Common.selected_recipe!!.id,
                                Menu2_comment_comment.text.toString(),
                                formatted.toString()
                            )
                        )
                        Menu2_comment_recycler.adapter?.notifyDataSetChanged()

                        // 댓글 서버로 전송
                        compositeDisposable.add(myAPI.recipe_comment(Common.selected_recipe_user!!.name!!, Common.selected_recipe!!.id, Menu2_comment_comment.text.toString(), formatted.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe{ Cid ->
                                CommentList.get(CommentList.size-1).id = Cid
                                //Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            })
                        // 댓글 서버로 전송끝

                        Menu2_comment_comment.setText("")
                    }
                    // 댓글 전송 버튼 누른다면 끝

            },
                {thr ->
                    Toast.makeText(this,"등록된 댓글이 없습니다. ",Toast.LENGTH_SHORT).show()
                    Menu2_comment_recycler.adapter = Menu2CommentAdapter(this, commentList)
                    //Menu2_comment_recycler.adapter?.notifyDataSetChanged()
                    // 댓글 전송 버튼 누른다면
                    Menu2_comment_submit.setOnClickListener {
                        commentList.add(
                            Comment(
                                Common.selected_recipe_user!!.name!!,
                                Common.selected_recipe!!.id,
                                Menu2_comment_comment.text.toString(),
                                formatted.toString()
                            )
                        )
                        Menu2_comment_recycler.adapter?.notifyDataSetChanged()
                        //Toast.makeText(this,""+commentList.get(commentList.size-1).id,Toast.LENGTH_SHORT).show()

                        // 댓글 서버로 전송
                        compositeDisposable.add(myAPI.recipe_comment(Common.selected_recipe_user!!.name!!, Common.selected_recipe!!.id, Menu2_comment_comment.text.toString(), formatted.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe{ Cid ->
                                commentList.get(commentList.size-1).id = Cid
                                //Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            })
                        // 댓글 서버로 전송끝

                        Menu2_comment_comment.setText("")
                    }
                    // 댓글 전송 버튼 누른다면 끝
                }))
        // 댓글 불러오기 끝


    }

    override fun onBackPressed() {
        super.onBackPressed()

        Toast.makeText(this,"뒤로 가기",Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}
