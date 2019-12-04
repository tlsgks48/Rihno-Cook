package com.example.rihno_cook.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Menu3_list
import com.example.rihno_cook.Model.Video
import com.example.rihno_cook.R
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class Menu3Adapter(internal var context: Context?,
                   internal var videoList:ArrayList<Video>) :
    RecyclerView.Adapter<Menu3Adapter.MyViewHolder>() {
    internal var compositeDisposable21 = CompositeDisposable()
    lateinit var myAPI: INodeJS

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var video_video: ImageView
        internal var video_name: TextView
        internal var video_good: TextView
        internal var video_comment: TextView
        lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        init {
            video_video = itemView.findViewById(R.id.video_video_item) as ImageView
            video_name = itemView.findViewById(R.id.video_name_item) as TextView
            video_good = itemView.findViewById(R.id.video_good_item) as TextView
            video_comment = itemView.findViewById(R.id.video_comment_item) as TextView
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Menu3Adapter.MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.video_item,p0,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(p0: Menu3Adapter.MyViewHolder, p1: Int) {
        Glide.with(context!!).load(videoList[p1].video).override(200,200).into(p0.video_video)
        p0.video_name.text = videoList[p1].name

        //관심과 추천 서버에서 불러오기
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)
        compositeDisposable21.add(myAPI.recipe_good_number3(videoList[p1].id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                p0.video_good.setText("관심("+message+")  ")
            })
        //
        compositeDisposable21.add(myAPI.recipe_comment_number3(videoList[p1].id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                p0.video_comment.setText("댓글("+message+")")
            })
        //

        p0.setClick(object:IRecyclerOnClick{
            override fun onClick(view: View, position: Int) {
                //Set recipe selected
                Common.selected_video = videoList[position]
                context!!.startActivity(Intent(context,Menu3_list::class.java))
            }

        })
    }
}