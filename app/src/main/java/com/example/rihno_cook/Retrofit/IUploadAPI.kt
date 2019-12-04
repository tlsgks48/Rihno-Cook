package com.example.rihno_cook.Retrofit

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IUploadAPI {
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

    @Multipart
    @POST("order_update") // 순서 이미지가 없는 경우
    fun uploadUpdateOrderFile(@Part ("recipe_id") recipe_id:Int,
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

}