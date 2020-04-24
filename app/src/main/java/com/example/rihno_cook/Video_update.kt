package com.example.rihno_cook

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.IUploadAPI
import com.example.rihno_cook.Retrofit.RetrofitClient
import com.example.rihno_cook.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_video_update.*
import kotlinx.android.synthetic.main.activity_video_upload.*
import retrofit2.Call
import retrofit2.Response
import okhttp3.MultipartBody

class Video_update : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
    }

    lateinit var mService: IUploadAPI
    internal var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: INodeJS

    private val PICK_IMAGE_REQUEST:Int = 1001

    var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_update)

        //Service
        mService = Common.apiUpload

        //Inot API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // 비디오 수정 초기 값들 설정
        Video_update_text.setText(Common.selected_video!!.text)
        Video_update_title.setText(Common.selected_video!!.name)
        Glide.with(this).load(Common.selected_video!!.video).into(Video_update_upload_image1)


        //

        Video_update_upload_image1.setOnClickListener {
            Toast.makeText(this, "Denied2", Toast.LENGTH_LONG).show()
            val getCountIntent = FileUtils.createGetContentIntent()
            val intent = Intent.createChooser(getCountIntent, "Select a file")
            startActivityForResult(intent,PICK_IMAGE_REQUEST)
        }

        // 작성하기 눌렀을때
        Video_update_submit.setOnClickListener {VideoUpdateFile()}
    }

    private fun VideoUpdateFile() {
        if (fileUri != null) {
            val file = FileUtils.getFile(this, fileUri)
            val requestFile = ProgressRequestBody(file, this)
            val body = MultipartBody.Part.createFormData("videofile", file.name, requestFile)
            Toast.makeText(this,"업로딩 진행중", Toast.LENGTH_SHORT).show()

            Thread(Runnable {
                mService.VideoUpdateFile(body, Common.selected_video!!.id,Video_update_text.text.toString(),Video_update_title.text.toString()).enqueue(object: retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@Video_update, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Toast.makeText(this@Video_update, "업로딩 성공!!", Toast.LENGTH_LONG).show()
                        val nextIntent = Intent(this@Video_update, MainActivity::class.java)
                        nextIntent.putExtra("번호",2)
                        startActivity(nextIntent)
                    }

                })
            }).start()

        }
        else {
            Toast.makeText(this,"업로딩 진행중", Toast.LENGTH_SHORT).show()
            Thread(Runnable {
                mService.VideoUpdateFile2(Common.selected_video!!.id,Video_update_text.text.toString(),Video_update_title.text.toString()).enqueue(object: retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@Video_update, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Toast.makeText(this@Video_update, "업로딩 성공!!", Toast.LENGTH_LONG).show()
                        val nextIntent = Intent(this@Video_update, MainActivity::class.java)
                        nextIntent.putExtra("번호",2)
                        startActivity(nextIntent)
                    }

                })
            }).start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //display the photo on the imageview
        fileUri = data?.data
        //  Video_upload_video1.setVideoURI(fileUri)
        //  Video_upload_video1.seekTo(100)
        Glide.with(this).load(fileUri).into(Video_update_upload_image1)

    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}
