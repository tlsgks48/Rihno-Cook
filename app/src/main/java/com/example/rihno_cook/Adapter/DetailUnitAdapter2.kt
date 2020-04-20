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
import com.example.rihno_cook.Model.Unit
import com.example.rihno_cook.R
import com.example.rihno_cook.recipe_detail
import com.squareup.picasso.Picasso

class DetailUnitAdapter2(internal var context: Context?,
                        internal var DunitList:ArrayList<Unit>) :
    RecyclerView.Adapter<DetailUnitAdapter2.MyViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.detail_unit_item2,p0,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return DunitList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.Dunit_title.setText(DunitList[p1].title)
        p0.Dunit_amount.setText(DunitList[p1].amount)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var Dunit_title: TextView
        internal var Dunit_amount: TextView
        lateinit var iRecyclerOnClick: IRecyclerOnClick


        init {
            Dunit_title = itemView.findViewById(R.id.Detail_unit_item_text2) as TextView
            Dunit_amount = itemView.findViewById(R.id.Detail_unit_item_unit2) as TextView
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

        }

    }
}