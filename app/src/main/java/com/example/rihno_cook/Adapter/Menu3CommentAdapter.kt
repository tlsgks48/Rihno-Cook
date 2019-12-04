package com.example.rihno_cook.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Model.Comment
import com.example.rihno_cook.R
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class Menu3CommentAdapter(internal var context: Context?,
                          internal var commentList:ArrayList<Comment>) :
    RecyclerView.Adapter<Menu3CommentAdapter.MyViewHolder>() {
    internal var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: INodeJS

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.recipe_comment_item,p0,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.menu2_comment_user.text = commentList[p1].user
        p0.menu2_comment_text.text = commentList[p1].text
        p0.menu2_comment_time.text = commentList[p1].date

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener  {
        internal var menu2_comment_user: TextView
        internal var menu2_comment_text: TextView
        internal var menu2_comment_time: TextView
        internal var menu2_comment_more: ImageView
        lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        init {
            menu2_comment_user = itemView.findViewById(R.id.Menu2_comment_item_user) as TextView
            menu2_comment_text = itemView.findViewById(R.id.Menu2_comment_item_text) as TextView
            menu2_comment_time = itemView.findViewById(R.id.Menu2_comment_item_time) as TextView
            menu2_comment_more = itemView.findViewById(R.id.Menu2_comment_item_more) as ImageView

            menu2_comment_more.setOnCreateContextMenuListener(this) // 현재 클래스에 OnCreateContextMenuListener 구현 설정


        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            //val edit : MenuItem = menu!!.add(Menu.NONE,1001, 1, "편집")
            //edit.setOnMenuItemClickListener(onEditMenu)
            if(commentList.get(adapterPosition).user!!.equals(Common.selected_fame_user!!.name)) {
                val delete : MenuItem = menu!!.add(Menu.NONE,1002, 2, "삭제")
                delete.setOnMenuItemClickListener(onEditMenu)
            }
        }

        var onEditMenu:MenuItem.OnMenuItemClickListener = MenuItem.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                1001 -> {
                    Toast.makeText(context, "편집", Toast.LENGTH_SHORT).show()
                    return@OnMenuItemClickListener true
                }
                1002 -> {
                    Toast.makeText(context, "삭제 "+commentList.get(adapterPosition).id, Toast.LENGTH_SHORT).show()
                    //삭제를 위한 리트로핏 API
                    val retrofit = RetrofitClient.instance
                    myAPI = retrofit.create(INodeJS::class.java)
                    compositeDisposable.add(myAPI.recipe_comment_delete3(commentList.get(adapterPosition).id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{ message ->

                        })
                    commentList.removeAt(adapterPosition)
                    notifyDataSetChanged()
                    return@OnMenuItemClickListener true
                }
                else -> {
                    return@OnMenuItemClickListener true
                }
            }
        }

    }


}