package com.example.rihno_cook.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Menu4_list
import com.example.rihno_cook.Model.Talk
import com.example.rihno_cook.R
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Menu4Adapter(internal var context: Context?,
                   internal var talkList:ArrayList<Talk>, internal var type:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal var compositeDisposable41 = CompositeDisposable()
    lateinit var myAPI: INodeJS

    @RequiresApi(Build.VERSION_CODES.O)
    val onlyDate: LocalDate = LocalDate.now()



    inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var talk_image: ImageView
        internal var talk_user: TextView
        internal var talk_day: TextView
        internal var talk_text: TextView
        internal var talk_good: TextView
        internal var talk_comment: TextView
        lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        init {
            talk_image = itemView.findViewById(R.id.talk_image_item) as ImageView
            talk_user = itemView.findViewById(R.id.talk_user_item) as TextView
            talk_day = itemView.findViewById(R.id.talk_day_item) as TextView
            talk_text = itemView.findViewById(R.id.talk_text_item) as TextView
            talk_good = itemView.findViewById(R.id.talk_good_item) as TextView
            talk_comment = itemView.findViewById(R.id.talk_comment_item) as TextView
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
                view = LayoutInflater.from(context).inflate(R.layout.talk_item,p0,false)
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
        return talkList.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

        when(type){
            1 -> {
                if(talkList[p1].image.equals("")){
                    (p0 as ViewHolder1).talk_image.visibility = View.GONE
                }
                else {
                    Glide.with(context!!).load(talkList[p1].image).override(200, 200).into((p0 as ViewHolder1).talk_image)
                }
                p0.talk_user.text = talkList[p1].user
                p0.talk_text.text = talkList[p1].text

                //관심과 추천 서버에서 불러오기
                val retrofit = RetrofitClient.instance
                myAPI = retrofit.create(INodeJS::class.java)
                compositeDisposable41.add(myAPI.recipe_good_number4(talkList[p1].id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{ message ->
                        p0.talk_good.setText("관심("+message+")  ")
                    })
                //
                compositeDisposable41.add(myAPI.recipe_comment_number4(talkList[p1].id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{ message ->
                        p0.talk_comment.setText("댓글("+message+")")
                    })
                //

                try {
                    @SuppressLint("SimpleDateFormat")
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val d1 : Date = formatter.parse(onlyDate.toString())
                    val d2 : Date = formatter.parse(talkList[p1].day)

                    // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
                    // 연산결과 -950400000. long type 으로 return 된다.
                    val calDate : Long = d2.time - d1.time

                    // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
                    // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다
                    var calDateDays : Long = calDate / (24*60*60*1000)
                    calDateDays = Math.abs(calDateDays)
                    if(calDateDays < 1){
                        p0.talk_day.text = "오늘"
                    }
                    else if(calDateDays > 364){
                        calDateDays = calDateDays/365
                        p0.talk_day.text = calDateDays.toString()+"년 전"
                    }else if(calDateDays > 29) {
                        calDateDays = calDateDays/30
                        p0.talk_day.text = calDateDays.toString()+"달 전"
                    }else if(calDateDays > 6) {
                        calDateDays = calDateDays/7
                        p0.talk_day.text = calDateDays.toString()+"주 전"
                    }else{
                        p0.talk_day.text = calDateDays.toString()+"일 전"
                    }
                    talkList[p1].day = p0.talk_day.text.toString()
                }catch (e: ParseException){}

                p0.setClick(object:IRecyclerOnClick{
                    override fun onClick(view: View, position: Int) {
                        //Set recipe selected
                        Common.selected_talk = talkList[position]
                        context!!.startActivity(Intent(context, Menu4_list::class.java))
                    }

                })
            }
            2 -> {
                Glide.with(context!!).load(talkList[p1].image).override(200,200).into((p0 as ViewHolder2).menu6_image)
                p0.menu6_name.text = ""
                p0.menu6_text.text = talkList[p1].text
                p0.menu6_witer.text = "by "+talkList[p1].user
            }
        }

    }

}