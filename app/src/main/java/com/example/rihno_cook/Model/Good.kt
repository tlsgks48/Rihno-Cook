package com.example.rihno_cook.Model

class Good {
    var id:Int=0
    var user:String?=null
    var recipe_id:Int=0
    var good:Int=0

    constructor()
    constructor(id:Int,user:String,recipe_id:Int,good:Int){
        this.id = id
        this.user = user
        this.recipe_id = recipe_id
        this.good = good
    }
}