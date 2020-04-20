package com.example.rihno_cook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Retrofit.IMenu2API
import com.example.rihno_cook.Retrofit.IUploadAPI
import com.example.rihno_cook.Utils.ProgressRequestBody
import com.google.gson.JsonObject
import com.ipaulpro.afilechooser.utils.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.menu6.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class Menu6 : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    internal var compositeDisposable3 = CompositeDisposable()
    internal lateinit var iMenu2API: IMenu2API
    internal lateinit var mService: IUploadAPI

    private val PICK_IMAGE_REQUEST:Int = 1001
    private val PICK_PHOTO_REQUEST:Int = 1002
    var fileUri: Uri? = null

    override fun onProgressUpdate(percentage: Int) {

    }

    override fun onStop() {
        compositeDisposable3.clear()
        super.onStop()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu6)

        iMenu2API = Common.api
        mService = Common.apiUpload

        // toolbar
        profile_toolbar.title = "내 정보"
        setSupportActionBar(profile_toolbar)

        // 유저 정보 읽기
        compositeDisposable3.add(iMenu2API.Menu6_User_Info(Common.selected_fame_user!!.name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ menu2List ->
                Log.d("json1",menu2List.toString())
                Log.d("json2",menu2List.get(0).toString())
                Log.d("json2",menu2List.get(0).get(0).toString())
                Log.d("json5",menu2List.get(0).get(0).asJsonObject.get("name").toString())
            },
                {thr ->
                    Toast.makeText(this,""+thr.message,Toast.LENGTH_SHORT).show()
                }))

        // 유저 이름
        profil_name.setText(Common.selected_fame_user!!.name)

        // 프로필 사진을 눌렀을 경우.
        profil_imageView.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("사진 선택")
            dialog.setMessage("사진을 촬영하시거나, 갤러리에서 원하시는 사진을 선택해주세요.")
            // 갤러리 선택
            fun d_p() {
                val getCountIntent = FileUtils.createGetContentIntent()
                val intent = Intent.createChooser(getCountIntent, "Select a file")
                startActivityForResult(intent,PICK_IMAGE_REQUEST)
            }
            // 카메라 선택
            fun d_n() {
                val values = ContentValues(1)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                fileUri = contentResolver
                    .insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if(intent.resolveActivity(packageManager) != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    startActivityForResult(intent, PICK_PHOTO_REQUEST)
                }
            }
            val dialog_listner = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which){
                        DialogInterface.BUTTON_POSITIVE ->
                            d_p()
                        DialogInterface.BUTTON_NEGATIVE ->
                            d_n()
                    }
                }
            }
            dialog.setPositiveButton("갤러리",dialog_listner)
            dialog.setNegativeButton("사진 촬영",dialog_listner)
            dialog.setNeutralButton("Cancel",null)
            dialog.show()
        }

        // 유저의 관심이나, 레시피 정보들 불러오기
        compositeDisposable3.add(
            iMenu2API.Menu6_Info(Common.selected_fame_user!!.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ menu6List ->
                    profil_s1_number.setText("" + menu6List[0].cnt)
                    profil_s2_number.setText("" + menu6List[1].cnt)
                    profil_s3_number.setText("" + menu6List[2].cnt)
                    //profil_s4_number.setText("" + menu6List[3].cnt)
                    profil_s5_number.setText("" + menu6List[4].cnt)
                    //Toast.makeText(this,"첫 : "+menu6List[0].cnt+" 둘 : "+menu6List[1].cnt,Toast.LENGTH_SHORT).show()

                    // 레시피
                    save1.setOnClickListener {
                        Menu6_list_Info(menu6List[0].cnt,1)
                    }
                    // 쿡tv
                    save2.setOnClickListener {
                        Menu6_list_Info(menu6List[1].cnt,2)
                    }
                    // 토크
                    save3.setOnClickListener {
                        Menu6_list_Info(menu6List[2].cnt,3)
                    }
                    // 댓글

/*                    save4.setOnClickListener {
                        Menu6_list_Info(menu6List[3].cnt,4)
                    }*/
                    // 나의관심
                    save5.setOnClickListener {
                        Menu6_list_Info(menu6List[4].cnt,5)
                    }
                },
                    { thr ->
                        Toast.makeText(this, "" + thr.message, Toast.LENGTH_SHORT).show()
                    })
        )

        // 나의소개 이미지를 눌렀다면
        profil_create_text.setOnClickListener {
            val enter_name_view = LayoutInflater.from(this@Menu6)
                .inflate(R.layout.menu6_dialog, null)

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("나의소개 변경")
            dialog.setView(enter_name_view)
            //삭제
            fun d_p() {
                val edit_name = enter_name_view.findViewById<View>(R.id.menu6_list_editText1) as EditText
                profil_text.setText(edit_name.text.toString())
            }

            val dialog_listner = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which){
                        DialogInterface.BUTTON_POSITIVE ->
                            d_p()
                    }
                }
            }
            dialog.setPositiveButton("확인",dialog_listner)
            dialog.setNeutralButton("취소",null)
            dialog.show()
        }
    }
    // 메인끝.

    private fun uploadFile() {
            val file = FileUtils.getFile(this, fileUri)
            val requestFile = ProgressRequestBody(file, this)
            val body = MultipartBody.Part.createFormData("profilefile", file.name, requestFile)

            Thread(Runnable {
                mService.ProfilFile(body, Common.selected_fame_user!!.name!!).enqueue(object: retrofit2.Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@Menu6, t.message, Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        Toast.makeText(this@Menu6, "프로필 이미지 업로딩 성공!! "+response.body(), Toast.LENGTH_LONG).show()
                    }
                })
            }).start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST) {
            //display the photo on the imageview
            fileUri = data?.data
            Glide.with(this).load(fileUri).into(profil_imageView)
            uploadFile()
        }
        else if(resultCode == Activity.RESULT_OK
            && requestCode == PICK_PHOTO_REQUEST) {
            Glide.with(this).load(fileUri).into(profil_imageView)
            uploadFile()
        }
    }

    // 나의정보창에서 각버튼을 눌렀을때 화면에 맞게 나오기위해.
    fun Menu6_list_Info(cnt:Int, a:Int) {
        Common.selected_menu6 = a
        if(cnt > 0) {
            val nextIntent = Intent(this, Menu6_list::class.java)
            startActivity(nextIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val enter_name_view = LayoutInflater.from(this@Menu6)
            .inflate(R.layout.menu6_dialog, null)

        when(item?.itemId ) {
            R.id.profil_toolbar_NickUpate -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("닉네임 변경")
                dialog.setView(enter_name_view)
                //삭제
                fun d_p() {
                    val edit_name = enter_name_view.findViewById<View>(R.id.menu6_list_editText1) as EditText
                    profil_name.setText(edit_name.text.toString())
                }
                val dialog_listner = object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE ->
                                d_p()
                        }
                    }
                }
                dialog.setPositiveButton("확인",dialog_listner)
                dialog.setNeutralButton("취소",null)
                dialog.show()
                //Toast.makeText(this, "닉네임 수정", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.profil_toolbar_logout -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("로그아웃")
                dialog.setMessage("로그아웃 하시겠습니까?")
                //삭제
                fun d_p() {
                    val nextIntent = Intent(this, Login::class.java)
                    startActivity(nextIntent)
                }
                val dialog_listner = object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE ->
                                d_p()
                        }
                    }
                }
                dialog.setPositiveButton("확인",dialog_listner)
                dialog.setNeutralButton("취소",null)
                dialog.show()
                //Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.profil_toolbar_destroy -> {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("회원탈퇴")
                dialog.setMessage("정말 회원탈퇴를 하시겠습니까?")
                //삭제
                fun d_p() {
                    val nextIntent = Intent(this, Login::class.java)
                    startActivity(nextIntent)
                }
                val dialog_listner = object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE ->
                                d_p()
                        }
                    }
                }
                dialog.setPositiveButton("확인",dialog_listner)
                dialog.setNeutralButton("취소",null)
                dialog.show()
                //Toast.makeText(this, "회원탈퇴", Toast.LENGTH_LONG).show()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
