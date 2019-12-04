package com.example.rihno_cook.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Model.Unit
import com.example.rihno_cook.Model.setAmount
import com.example.rihno_cook.Model.setTitle

import com.example.rihno_cook.R

class Menu2UnitAdapter(val context: Context, val UnitList:ArrayList<Unit>) : RecyclerView.Adapter<Menu2UnitAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.recipe_material_item,p0,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return UnitList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.Unit_title.setText(UnitList[p1].title)
        p0.Unit_amount.setText(UnitList[p1].amount)
        //p0.Unit_title.text = UnitList[p1].title
        //p0.Unit_amount.text = UnitList[p1].amount
    }

   inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
       internal var Unit_title: EditText
       internal var Unit_amount: EditText
       internal var Unit_delete: Button
       lateinit var iRecyclerOnClick: IRecyclerOnClick


       fun setClick(iRecyclerOnClick: IRecyclerOnClick)
       {
           this.iRecyclerOnClick = iRecyclerOnClick;
       }

       init {
           Unit_title = itemView.findViewById(R.id.Material_Item_title) as EditText
           Unit_title.addTextChangedListener(object : TextWatcher {
               override fun afterTextChanged(s: Editable?) {
               }
               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               }
               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                   UnitList[adapterPosition].setTitle(Unit_title.text.toString())
               }
           })

           Unit_amount = itemView.findViewById(R.id.Material_Item_amount) as EditText
           Unit_amount.addTextChangedListener(object  : TextWatcher {
               override fun afterTextChanged(s: Editable?) {
               }
               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               }
               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                   UnitList[adapterPosition].setAmount(Unit_amount.text.toString())
               }
           })

           Unit_delete = itemView.findViewById(R.id.Material_Item_delete) as Button
           Unit_delete.setOnClickListener {
               UnitList.removeAt(adapterPosition)
               //notifyItemRemoved(adapterPosition)
               //notifyItemRangeChanged(adapterPosition,UnitList.size)
               notifyDataSetChanged() // 자바랑 많이다름..

           }

           itemView.setOnClickListener(this)
       }
       override fun onClick(v: View?) {
           iRecyclerOnClick.onClick(v!!,adapterPosition)
       }

   }
    companion object {
        lateinit var UnitList:ArrayList<Unit>
    }
}