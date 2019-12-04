package com.example.rihno_cook.Model

class Recipe {
    var id:Int=0
    var user:String?=null
    var name:String?=null
    var image:String?=null
    var text:String?=null
    var category0:Int=0
    var category1:String?=null
    var category2:String?=null
    var category3:String?=null
    var category4:String?=null
    var unit:String?=null
    var order_number:Int=0
    var tip:String?=null
    var comment:Int=0
    var good:Int=0

    constructor()
    constructor(ID:Int,user:String,name:String,image:String,text:String,category1:String,category2:String,category3:String,category4:String,unit:String,order_number:Int,tip:String,comment:Int,good:Int)
    {
        this.id = ID
        this.user = user
        this.name = name
        this.image = image
        this.text = text
        this.category1 = category1
        this.category2 = category2
        this.category3 = category3
        this.category4 = category4
        this.unit = unit
        this.order_number = order_number
        this.tip = tip
        this.comment = comment
        this.good = good
    }
}