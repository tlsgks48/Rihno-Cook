package com.example.rihno_cook.Model

class Comment {
    var id:Int=0
    var user:String?=null
    var recipe_id:Int=0
    var text:String?=null
    var date:String?=null

    constructor()
    constructor(user:String,recipe_id:Int,text:String, date:String){
        this.user = user
        this.recipe_id = recipe_id
        this.text = text
        this.date = date
    }
    constructor(id:Int,user:String,recipe_id:Int,text:String, date:String){
        this.id = id
        this.user = user
        this.recipe_id = recipe_id
        this.text = text
        this.date = date
    }
}