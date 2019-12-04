package com.example.rihno_cook.Model

class Good_Fame {
    var recipe_id:Int=0
    var category0:Int=0
    var cnt:Int=0

    constructor()
    constructor(recipe_id:Int,cnt:Int){
        this.recipe_id = recipe_id
        this.cnt = cnt
    }
}