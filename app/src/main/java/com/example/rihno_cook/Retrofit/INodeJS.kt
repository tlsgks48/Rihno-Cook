package com.example.rihno_cook.Retrofit

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface INodeJS {
    // @@@@@@@@@@@@@@@@@@@@@@@@  로그인 및 회원가입 유저 정보

    // 일단 로그인 부분부터 고쳐보자.
    @POST("register")
    @FormUrlEncoded
    fun registerUser(@Field("email") email:String,
                     @Field("name") name:String,
                     @Field("password") password:String):Observable<String>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email:String,
                     @Field("password") password:String):Observable<String>

    @POST("login_check")
    @FormUrlEncoded
    fun loginCheck(@Field("email") email:String):Observable<String>

    @POST("login_user")
    @FormUrlEncoded
    fun login_user(@Field("name") email:String,
                     @Field("email") name:String):Observable<String>

    @GET("login_name")
    fun login_name():Observable<String>

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 메뉴1~2 레시피부분

    @POST("last_number")
    fun last_number():Observable<String>

    @POST("recipe_delete")
    @FormUrlEncoded
    fun recipe_delete(@Field("id") id:Int):Observable<String>

    @POST("order_delete")
    @FormUrlEncoded
    fun recipe_order_delete(@Field("recipe_id") recipe_id:Int):Observable<String>

    @POST("comment")
    @FormUrlEncoded
    fun recipe_comment(@Field("user") user:String,
                       @Field("recipe_id") recipe_id:Int,
                       @Field("text") text:String,
                       @Field("date") date:String):Observable<Int>

    @POST("comment_delete")
    @FormUrlEncoded
    fun recipe_comment_delete(@Field("id") id:Int):Observable<String>

    // 카운트 조회 댓글
    @POST("comment_number")
    @FormUrlEncoded
    fun recipe_comment_number(@Field("recipe_id") recipe_id:Int):Observable<Int>

    // 카운트 조회 관심
    @POST("good_number")
    @FormUrlEncoded
    fun recipe_good_number(@Field("recipe_id") recipe_id:Int):Observable<Int>

    @POST("good")
    @FormUrlEncoded
    fun recipe_good(@Field("user") user:String,
                    @Field ("category0") category0:Int,
                       @Field("recipe_id") recipe_id:Int):Observable<String>

    @POST("good_delete")
    @FormUrlEncoded
    fun recipe_good_delete(@Field("user") user:String,
                           @Field("recipe_id") recipe_id:Int):Observable<String>

    @POST("good_first")
    @FormUrlEncoded
    fun recipe_good_first(@Field("user") user:String,
                          @Field("recipe_id") recipe_id:Int):Observable<Int>

    //
    //@@@@@@@@@@@@@ 메뉴1부분

    @POST("good_fame")
    fun good_fame():Observable<String>

    @POST("good_commend")
    @FormUrlEncoded
    fun good_commend(@Field("user") user:String):Observable<String>

    //
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 메뉴3 비디오 부분

    // 메뉴3 카운트 조회 댓글
    @POST("comment_number3")
    @FormUrlEncoded
    fun recipe_comment_number3(@Field("recipe_id") recipe_id:Int):Observable<Int>

    // 메뉴3 카운트 조회 관심
    @POST("good_number3")
    @FormUrlEncoded
    fun recipe_good_number3(@Field("recipe_id") recipe_id:Int):Observable<Int>

    // 관심부분
    @POST("good3")
    @FormUrlEncoded
    fun recipe_good3(@Field("user") user:String,
                    @Field("recipe_id") recipe_id:Int):Observable<String>

    @POST("good_delete3")
    @FormUrlEncoded
    fun recipe_good_delete3(@Field("user") user:String,
                           @Field("recipe_id") recipe_id:Int):Observable<String>

    @POST("good_first3")
    @FormUrlEncoded
    fun recipe_good_first3(@Field("user") user:String,
                          @Field("recipe_id") recipe_id:Int):Observable<Int>

    // 비디오,삭제, 댓글 부분

    @POST("menu3_delete")
    @FormUrlEncoded
    fun recipe_delete3(@Field("id") id:Int):Observable<String>

    @POST("comment3")
    @FormUrlEncoded
    fun recipe_comment3(@Field("user") user:String,
                       @Field("recipe_id") recipe_id:Int,
                       @Field("text") text:String,
                       @Field("date") date:String):Observable<Int>

    @POST("comment_delete3")
    @FormUrlEncoded
    fun recipe_comment_delete3(@Field("id") id:Int):Observable<String>

    //
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 메뉴4 토크 부분

    // 메뉴4 카운트 조회 댓글
    @POST("comment_number4")
    @FormUrlEncoded
    fun recipe_comment_number4(@Field("recipe_id") recipe_id:Int):Observable<Int>

    // 메뉴4 카운트 조회 관심
    @POST("good_number4")
    @FormUrlEncoded
    fun recipe_good_number4(@Field("recipe_id") recipe_id:Int):Observable<Int>

    // 관심부분
    @POST("good4")
    @FormUrlEncoded
    fun recipe_good4(@Field("user") user:String,
                     @Field("recipe_id") recipe_id:Int):Observable<String>

    @POST("good_delete4")
    @FormUrlEncoded
    fun recipe_good_delete4(@Field("user") user:String,
                            @Field("recipe_id") recipe_id:Int):Observable<String>

    @POST("good_first4")
    @FormUrlEncoded
    fun recipe_good_first4(@Field("user") user:String,
                           @Field("recipe_id") recipe_id:Int):Observable<Int>

    // 토크,삭제, 댓글 부분

    @POST("menu4_delete")
    @FormUrlEncoded
    fun recipe_delete4(@Field("id") id:Int):Observable<String>

    @POST("comment4")
    @FormUrlEncoded
    fun recipe_comment4(@Field("user") user:String,
                        @Field("recipe_id") recipe_id:Int,
                        @Field("text") text:String,
                        @Field("date") date:String):Observable<Int>

    @POST("comment_delete4")
    @FormUrlEncoded
    fun recipe_comment_delete4(@Field("id") id:Int):Observable<String>

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 메뉴 6 나의정보


}