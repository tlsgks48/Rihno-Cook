package com.example.rihno_cook.Retrofit

import com.example.rihno_cook.Model.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
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

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 예전 UploadAPI 구간

    @Multipart
    @POST("upload")
    fun uploadFile(@Part file: MultipartBody.Part,
                   @Part ("user") user:String,
                   @Part ("text") text:String,
                   @Part ("unit") unit:String,
                   @Part ("order_number") order_number:Int,
                   @Part ("tip") tip:String,
                   @Part ("category0") category0:Int,
                   @Part ("category1") category1:String,
                   @Part ("category2") category2:String,
                   @Part ("category3") category3:String,
                   @Part ("category4") category4:String,
                   @Part ("title") title:String): Call<String>

    @Multipart
    @POST("order") // 순서 이미지가 있는 경우
    fun uploadOrderFile(@Part file: MultipartBody.Part,
                        @Part ("recipe_id") recipe_id:Int,
                        @Part ("number") number:Int,
                        @Part ("text") text:String): Call<String>

    @Multipart
    @POST("order") // 순서 이미지가 없는 경우
    fun uploadOrderFile(@Part ("recipe_id") recipe_id:Int,
                        @Part ("number") number:Int,
                        @Part ("text") text:String): Call<String>

    @Multipart
    @POST("upload_update") // 레시피 수정
    fun uploadUpdateFile(@Part file: MultipartBody.Part,
                         @Part ("title") title:String,
                         @Part ("text") text:String,
                         @Part ("unit") unit:String,
                         @Part ("order_number") order_number:Int,
                         @Part ("tip") tip:String,
                         @Part ("category0") category0:Int,
                         @Part ("category1") category1:String,
                         @Part ("category2") category2:String,
                         @Part ("category3") category3:String,
                         @Part ("category4") category4:String,
                         @Part ("recipe_id") recipe_id:Int): Call<String>


    @POST("upload_update2") // 레시피 수정 2 (대표 이미지 파일 안 바꿀 경우)
    @FormUrlEncoded
    fun uploadUpdateFile2(@Field("title") title:String,
                          @Field ("text") text:String,
                          @Field ("unit") unit:String,
                          @Field ("order_number") order_number:Int,
                          @Field ("tip") tip:String,
                          @Field ("category0") category0:Int,
                          @Field ("category1") category1:String,
                          @Field ("category2") category2:String,
                          @Field ("category3") category3:String,
                          @Field ("category4") category4:String,
                          @Field ("recipe_id") recipe_id:Int): Call<String>

    @Multipart
    @POST("order_update") // 순서 이미지가 있는 경우
    fun uploadUpateOrderFile(@Part file: MultipartBody.Part,
                             @Part ("recipe_id") recipe_id:Int,
                             @Part ("number") number:Int,
                             @Part ("text") text:String): Call<String>

    @POST("order_update2") // 순서 이미지가 없는 경우
    @FormUrlEncoded
    fun uploadUpdateOrderFile2(@Field ("recipe_id") recipe_id:Int,
                               @Field ("number") number:Int,
                               @Field ("text") text:String): Call<String>

    // 메뉴 3 비디오
    @Multipart
    @POST("menu3_upload")
    fun VideoUploadFile(@Part file: MultipartBody.Part,
                        @Part ("user") user:String,
                        @Part ("text") text:String,
                        @Part ("name") name:String): Call<String>

    // 메뉴3 수정 1(비디오파일 바꿀때)
    @Multipart
    @POST("menu3_update")
    fun VideoUpdateFile(@Part file: MultipartBody.Part,
                        @Part ("id") id:Int,
                        @Part ("text") text:String,
                        @Part ("name") name:String): Call<String>

    // 메뉴3 수정 2(비디오파일 바꿈x)
    @POST("menu3_update2")
    @FormUrlEncoded
    fun VideoUpdateFile2(@Field ("id") id:Int,
                         @Field ("text") text:String,
                         @Field ("name") name:String): Call<String>

    // 메뉴 4 토크 (사진 업로드)
    @Multipart
    @POST("menu4_upload")
    fun TalkUploadFile(@Part file: MultipartBody.Part,
                       @Part ("user") user:String,
                       @Part ("text") text:String,
                       @Part ("day") day:String): Call<String>

    @Multipart
    @POST("menu4_upload")
    fun TalkUploadFile2(
        @Part ("user") user:String,
        @Part ("text") text:String,
        @Part ("day") day:String): Call<String>

    @Multipart
    @POST("menu4_update")
    fun TalkUpdateFile(@Part file: MultipartBody.Part,
                       @Part ("id") id:Int,
                       @Part ("text") text:String,
                       @Part ("day") day:String): Call<String>

    @Multipart
    @POST("menu4_update")
    fun TalkUpdateFile2(
        @Part ("id") id:Int,
        @Part ("text") text:String,
        @Part ("day") day:String): Call<String>

    @Multipart
    @POST("menu6_image")
    fun ProfilFile(@Part file: MultipartBody.Part,
                   @Part ("user") user:String): Call<String>

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 예전 NodeJS API 구간

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
}