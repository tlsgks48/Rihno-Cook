package com.example.rihno_cook.Model

class Unit (var title:String, var amount:String) {

}

fun Unit.setTitle(title: String) {
    this.title = title
}

val Unit.title: String
    get() {
        return title
    }

fun Unit.setAmount(amount: String) {
    this.amount = amount
}

/*val Unit.amount: String
    get() {
        return amount
    }*/

