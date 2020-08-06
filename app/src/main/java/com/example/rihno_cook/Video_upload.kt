package com.example.rihno_cook

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.INodeJS
import com.example.rihno_cook.Retrofit.IUploadAPI
import com.example.rihno_cook.Retrofit.RetrofitClinet
import com.example.rihno_cook.Utils.ProgressRequestBody
import com.ipaulpro.afilechooser.utils.FileUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_video_upload.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class Video_upload : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
       // Toast.makeText(this,"업로딩 진행중", Toast.LENGTH_SHORT).show()
    }
    lateinit var mService: IMenu2API
    internal var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: INodeJS

    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST:Int = 1001

    var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_upload)

        //Service
        mService = Common.api

        //Inot API
        val retrofit = RetrofitClinet.instance
        myAPI = retrofit.create(INodeJS::class.java)

        //Request runtime permission
/*        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSION_REQUEST)*/

/*        // 비디오 파일 눌럿을때
        Video_upload_video1.setOnClickListener {
            Toast.makeText(this, "Denied1", Toast.LENGTH_LONG).show()
            val getCountIntent = FileUtils.createGetContentIntent()
            val intent = Intent.createChooser(getCountIntent, "Select a file")
            startActivityForResult(intent,PICK_IMAGE_REQUEST)

        }*/

        Video_upload_image1.setOnClickListener {
            Toast.makeText(this, "Denied2", Toast.LENGTH_LONG).show()
            val getCountIntent = FileUtils.createGetContentIntent()
            val intent = Intent.createChooser(getCountIntent, "Select a file")
            startActivityForResult(intent,PICK_IMAGE_REQUEST)
        }

        // 작성하기 눌렀을때
        Video_submit.setOnClickListener {VideoUploadFile()}
    }

    private fun VideoUploadFile() {
        if (fileUri != null) {
            val file = FileUtils.getFile(this, fileUri)
            val requestFile = ProgressRequestBody(file, this)
            val body = MultipartBody.Part.createFormData("videofile", file.name, requestFile)
            Toast.makeText(this,"업로딩 진행중", Toast.LENGTH_SHORT).show()

            Thread(Runnable {
                mService.VideoUploadFile(body, Common.selected_fame_user!!.name!!,Video_text.text.toString(),Video_title.text.toString()).enqueue(object: retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@Video_upload, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Toast.makeText(this@Video_upload, "업로딩 성공!!", Toast.LENGTH_LONG).show()
                        val nextIntent = Intent(this@Video_upload, MainActivity::class.java)
                        nextIntent.putExtra("번호",2)
                        startActivity(nextIntent)
                    }

                })
            }).start()

        }
        else {
            Toast.makeText(this, "요리를 대표할 비디오를 선택해 주세요", Toast.LENGTH_LONG).show()
        }
    }

    //Ctrl+O
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            PERMISSION_REQUEST -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this, "Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            //display the photo on the imageview
            fileUri = data?.data
          //  Video_upload_video1.setVideoURI(fileUri)
          //  Video_upload_video1.seekTo(100)
            Glide.with(this).load(fileUri).into(Video_upload_image1)

    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}
