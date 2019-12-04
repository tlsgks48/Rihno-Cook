package com.example.rihno_cook.Adapter

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.rihno_cook.AppConstants
import com.example.rihno_cook.Interface.IRecyclerOnClick
import com.example.rihno_cook.Model.Order
import com.example.rihno_cook.Model.setOrderText
import com.example.rihno_cook.R
import com.ipaulpro.afilechooser.utils.FileUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso

class Menu2OrderAdapter (val activi : Activity,val context: Context, val OrderList:ArrayList<Order>) : RecyclerView.Adapter<Menu2OrderAdapter.MyViewHolder>() {
    //val activi : Activity? = null
    var OfileUri: Uri? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var Order_number: TextView
        internal var Order_text: EditText
        internal var Order_image: ImageView
        internal var Order_delete: ImageView
        //internal var Order_image: EditText
        //internal var Unit_delete: Button
        lateinit var iRecyclerOnClick: IRecyclerOnClick


        fun setClick(iRecyclerOnClick: IRecyclerOnClick)
        {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }
        init {
            Order_number = itemView.findViewById(R.id.Order_Item_number) as TextView
            Order_text = itemView.findViewById(R.id.Order_Item_text) as EditText
            Order_text.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    OrderList[adapterPosition].setOrderText(Order_text.text.toString())
                }

            })

            Order_image = itemView.findViewById(R.id.Order_Item_image) as ImageView
            Order_image.setOnClickListener {

               // val pickImageIntent = Intent(Intent.ACTION_PICK,
               //     MediaStore.Images.Media.EXTERNAL_CONTENT_URI) , pickImageIntent
                val getCountIntent = FileUtils.createGetContentIntent()
                val intent = Intent.createChooser(getCountIntent, "Select a file")
                activi.startActivityForResult(intent, adapterPosition+3000)
                //Toast.makeText(context, "adapter : "+adapterPosition, Toast.LENGTH_LONG).show()

            }

            Order_delete = itemView.findViewById(R.id.Order_Item_Delete) as ImageView
            Order_delete.setOnClickListener {
                OrderList.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
            //Unit_delete = itemView.findViewById(R.id.Material_Item_delete) as Button
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            iRecyclerOnClick.onClick(v!!,adapterPosition)
        }

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Menu2OrderAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.recipe_order_item,p0,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }

    override fun onBindViewHolder(p0: Menu2OrderAdapter.MyViewHolder, p1: Int) {


        Picasso.get().load(OrderList[p1].image).into(p0.Order_image)
        p0.Order_number.text = (p1+1).toString()
        p0.Order_text.setText(OrderList[p1].text)
        // p0.Unit_title.setText(UnitList[p1].title)
        // p0.Unit_amount.setText(UnitList[p1].amount)
        //p0.Unit_title.text = UnitList[p1].title
        //p0.Unit_amount.text = UnitList[p1].amount
    }


}

