package com.example.rihno_cook.Retrofit

import com.example.rihno_cook.Model.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.*

interface IMenu2API {
    @get:GET("menu2")
    val menu2List:Observable<List<Recipe>>

    @POST("order_get")
    @FormUrlEncoded
    fun getMenu2_OrderList(@Field("recipe_id") recipe_id:Int):Observable<List<Dorder>>

    @POST("order_get")
    @FormUrlEncoded
    fun UpdateOrderList(@Field("recipe_id") recipe_id:Int):Observable<ArrayList<Order>>

    @POST("login_name")
    fun LoginUserList():Observable<List<LoginUser>>

    @POST("menu2_comment")
    @FormUrlEncoded
    fun commentList(@Field("recipe_id") recipe_id:Int):Observable<ArrayList<Comment>>

    @POST("fame_menu2")
    @FormUrlEncoded
    fun Menu1_fameList(@Field("recipe_id1") recipe_id1:Int,
                       @Field("recipe_id2") recipe_id2:Int,
                       @Field("recipe_id3") recipe_id3:Int):Observable<List<Recipe>>

    @POST("commend_menu2")
    @FormUrlEncoded
    fun Menu1_commendList(@Field("id1") id1:Int,
                       @Field("id2") id2:Int,
                       @Field("id3") id3:Int):Observable<List<Recipe>>

    @get:GET("menu3")
    val menu3List:Observable<ArrayList<Video>>

    @POST("menu3_comment")
    @FormUrlEncoded
    fun commentList3(@Field("recipe_id") recipe_id:Int):Observable<ArrayList<Comment>>

    @get:GET("menu4")
    val menu4List:Observable<ArrayList<Talk>>

    @POST("menu4_comment")
    @FormUrlEncoded
    fun commentList4(@Field("recipe_id") recipe_id:Int):Observable<ArrayList<Comment>>

    @POST("menu6_user")
    @FormUrlEncoded
    fun Menu6_User_Info(@Field("user") user:String?):Observable<ArrayList<JsonArray>>

    @POST("menu6_get")
    @FormUrlEncoded
    fun Menu6_Info(@Field("user") user:String?):Observable<ArrayList<Info>>

    @POST("menu6_list2")
    @FormUrlEncoded
    fun Menu6_list0(@Field("user") user:String?):Observable<ArrayList<JsonArray>>

    @POST("menu6_list3")
    @FormUrlEncoded
    fun Menu6_list_good(@Field("user") user:String?):Observable<ArrayList<JsonArray>>

    @POST("menu6_list4")
    @FormUrlEncoded
    fun Menu6_list_good_list(@Field("id") id:Int):Observable<ArrayList<JsonArray>>

}