package com.example.rihno_cook.Model

import android.net.Uri

class Order (var text:String, var image:String) {
    var uri: Uri? = null

}

fun Order.setOrderText(orderText: String) {
    this.text = orderText
}

fun Order.setOrderImage(orderImage: String) {
    this.image = orderImage
}