package com.example.rihno_cook.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Menu3_list
import com.example.rihno_cook.Model.Video
import com.example.rihno_cook.R
import com.example.rihno_cook.Retrofit.INodeJS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException

class Menu3Adapter(internal var context: Context?,
                   internal var videoList:ArrayList<Video>, internal var type:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal var compositeDisposable21 = CompositeDisposable()
    lateinit var myAPI: INodeJS

    inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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

    inner class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var menu6_image: ImageView
        internal var menu6_name: TextView
        internal var menu6_text: TextView
        internal var menu6_witer: TextView
        lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        init {
            menu6_image = itemView.findViewById(R.id.menu6_Item_image) as ImageView
            menu6_name = itemView.findViewById(R.id.menu6_Item_name) as TextView
            menu6_text = itemView.findViewById(R.id.menu6_Item_text) as TextView
            menu6_witer = itemView.findViewById(R.id.menu6_Item_writer) as TextView
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when(viewType) {
            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.video_item,p0,false)
                ViewHolder1(view)
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.menu6_item,p0,false)
                ViewHolder2(view)
            }
            else -> throw RuntimeException("알 수 없는 뷰 타입 에러")
        }

    }



    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

        when(type){
            1 -> {
                Glide.with(context!!).load(videoList[p1].video).override(200,200).into((p0 as ViewHolder1).video_video)
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
            2 -> {
                Glide.with(context!!).load(videoList[p1].video).override(200,200).into((p0 as ViewHolder2).menu6_image)
                p0.menu6_name.text = videoList[p1].name
                p0.menu6_text.text = videoList[p1].text
                p0.menu6_witer.text = "by "+videoList[p1].user
            }
        }


    }
}