package com.example.rihno_cook.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Model.Recipe
import com.example.rihno_cook.R
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.RetrofitClient
import com.example.rihno_cook.recipe_detail
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MyMenu2Adapter(internal var context: Context?,
                     internal var recipeList:List<Recipe>) :
RecyclerView.Adapter<MyMenu2Adapter.MyViewHolder>(){
    internal var compositeDisposable2 = CompositeDisposable()
    lateinit var myAPI: INodeJS

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.recipe_item,p0,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        Picasso.get().load(recipeList[p1].image).into(p0.recipe_image)
        p0.recipe_name.text = recipeList[p1].name
        //삭제를 위한 리트로핏 API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)
        compositeDisposable2.add(myAPI.recipe_good_number(recipeList[p1].id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                p0.recipe_good.setText("관심("+message+")  ")
            })
        //
        compositeDisposable2.add(myAPI.recipe_comment_number(recipeList[p1].id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                p0.recipe_comment.setText("댓글("+message+")")
            })
        //

       // p0.recipe_good.setText("관심("+recipeList[p1].good+")  ")
       // p0.recipe_comment.setText("댓글("+recipeList[p1].id+")")
        p0.setClick(object:IRecyclerOnClick{
            override fun onClick(view: View, position: Int) {
                //Set recipe selected
                Common.selected_recipe = recipeList[position]
                context!!.startActivity(Intent(context,recipe_detail::class.java))
            }

        })
    }

    inner class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        internal var recipe_image:ImageView
        internal var recipe_name:TextView
        internal var recipe_good:TextView
        internal var recipe_comment:TextView
        lateinit var iRecyclerOnClick:IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        init {
            recipe_image = itemView.findViewById(R.id.recipe_image_item) as ImageView
            recipe_name = itemView.findViewById(R.id.recipe_name_item) as TextView
            recipe_good = itemView.findViewById(R.id.recipe_good_item) as TextView
            recipe_comment = itemView.findViewById(R.id.recipe_comment_item) as TextView
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)
        }

    }
}