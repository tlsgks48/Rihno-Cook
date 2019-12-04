package com.example.rihno_cook.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Model.Dorder
import com.example.rihno_cook.Model.Order
import com.example.rihno_cook.R
import com.squareup.picasso.Picasso

class DetailOrderAdapter(internal var context: Context?,
                     internal var DorderList:List<Dorder>) :
    RecyclerView.Adapter<DetailOrderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DetailOrderAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.detail_order_item,p0,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return DorderList.size
    }

    override fun onBindViewHolder(p0: DetailOrderAdapter.MyViewHolder, p1: Int) {
        Picasso.get().load(DorderList[p1].image).into(p0.Dorder_image)
        p0.Dorder_number.text = (p1+1).toString()
        p0.Dorder_text.setText(DorderList[p1].text)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var Dorder_image: ImageView
        internal var Dorder_number: TextView
        internal var Dorder_text: TextView
        lateinit var iRecyclerOnClick: IRecyclerOnClick

        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        init {
            Dorder_image = itemView.findViewById(R.id.Detail_order_item_image) as ImageView
            Dorder_text = itemView.findViewById(R.id.Detail_order_item_text) as TextView
            Dorder_number = itemView.findViewById(R.id.Detail_order_item_number) as TextView
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

        }

    }

}