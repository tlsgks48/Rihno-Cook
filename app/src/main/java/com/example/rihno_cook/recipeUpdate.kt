package com.example.rihno_cook

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.rihno_cook.Adapter.Menu2OrderAdapter
import com.example.rihno_cook.Adapter.Menu2UnitAdapter
import com.example.rihno_cook.Common.Common
import com.example.rihno_cook.Model.Order
import com.example.rihno_cook.Model.Unit
import com.example.rihno_cook.Retrofit.*
import com.example.rihno_cook.Utils.ProgressRequestBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ipaulpro.afilechooser.utils.FileUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recipe_update.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import java.util.*

class recipeUpdate : AppCompatActivity(), ProgressRequestBody.UploadCallbacks {
    override fun onProgressUpdate(percentage: Int) {
        Toast.makeText(this,"업로딩 진행중", Toast.LENGTH_SHORT).show()
    }

    val BASE_URL = "http://10.0.3.2:3000/"

    val apiUpload: IUploadAPI
        get() = RetrofitClient3.getClient(BASE_URL).create(IUploadAPI::class.java)

    lateinit var mService: IUploadAPI

    private val PERMISSION_REQUEST: Int = 1000

    private val PICK_IMAGE_REQUEST:Int = 1001

    var fileUri: Uri? = null

    // 요리순서 이미지 uri
    var OfileUri: Uri? = null

    // 재료와 요리순서 리스트
    var UnitList = arrayListOf<Unit>()
    var OrderList = arrayListOf<Order>()

    var json2:String = ""

    // 스피너 리스트 담을 스트링 변수
    var R_U_spinner1:String = ""
    var R_U_spinner2:String = ""
    var R_U_spinner3:String = ""
    var R_U_spinner4:String = ""
    var R_U_spinner5:String = ""

    var r_i : Int = 0

    var overNumber : Int = 0

    var ob : Objects? = null

    var j : Int = 0

    // 유저 이름 호출 부분
    internal var compositeDisposable = CompositeDisposable()
    lateinit var myAPI: INodeJS
    internal lateinit var iMenu2API: IMenu2API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_update)
        //Service
        mService = apiUpload

        //Inot API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(INodeJS::class.java)

        // retrofit
        iMenu2API = Common.api

        //Toast.makeText(this, "id는 "+ Common.selected_recipe!!.id+", 종류는 "+ Common.selected_recipe!!.category1, Toast.LENGTH_LONG).show()

        overNumber = Common.selected_recipe!!.order_number

        // 레시피 수정 초기 값들 설정
        Recipe_update_title.setText(Common.selected_recipe!!.name)
        Picasso.get().load(Common.selected_recipe!!.image).into(recipe_update_upload_image1)
        Recipe_update_text.setText(Common.selected_recipe!!.text)

        Recipe_update_Tip.setText(Common.selected_recipe!!.tip)
        //

        // 스피너시작
        val colors = arrayOf("종류별","한식","양식","중식","면요리","빵","디저트","기타")
        val colors2 = arrayOf("선택","1인분","2인분","3인분","4인분","5인분","6인분","기타")
        val colors3 = arrayOf("선택","5분이내","10분이내","15분이내","30분이내","1시간이내","2시간이내","2시간이상")
        val colors4 = arrayOf("선택","상","중","하")
        val colors5 = arrayOf("단위","큰술","작은술","종이컵(1컵=180ml)","g","ml","기타")

        // Initializing an ArrayAdapter spinner
        val adapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            colors // Array
        )
        val adapter2 = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            colors2 // Array
        )
        val adapter3 = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            colors3 // Array
        )
        val adapter4 = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            colors4 // Array
        )
        val adapter5 = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            colors5 // Array
        )
        var i : Int

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        // Finally, data bind the spinner object with dapter
        spinner.adapter = adapter;
        if(Common.selected_recipe!!.category1.equals("한식")) {
            i=1
        }else if(Common.selected_recipe!!.category1.equals("양식")) {
            i=2
        }else if(Common.selected_recipe!!.category1.equals("중식")) {
            i=3
        }else if(Common.selected_recipe!!.category1.equals("면요리")) {
            i=4
        }else if(Common.selected_recipe!!.category1.equals("빵")) {
            i=5
        }else if(Common.selected_recipe!!.category1.equals("디저트")) {
            i=6
        }else {
            i=7
        }
        spinner.setSelection(i)
        j = i
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                // text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                R_U_spinner1 = parent.getItemAtPosition(position).toString()
                j = position
            }
            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }

        // Set the drop down view resource
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        // Finally, data bind the spinner object with dapter
        Category_spinner1.adapter = adapter2; // "1인분","2인분","3인분","4인분","5인분","6인분","기타"
        if(Common.selected_recipe!!.category2.equals("1인분")) {
            i=1
        }else if(Common.selected_recipe!!.category2.equals("2인분")) {
            i=2
        }else if(Common.selected_recipe!!.category2.equals("3인분")) {
            i=3
        }else if(Common.selected_recipe!!.category2.equals("4인분")) {
            i=4
        }else if(Common.selected_recipe!!.category2.equals("5인분")) {
            i=5
        }else if(Common.selected_recipe!!.category2.equals("6인분")) {
            i=6
        }else {
            i=7
        }
        Category_spinner1.setSelection(i)
        Category_spinner1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                // text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                R_U_spinner2 = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        // Set the drop down view resource
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        // Finally, data bind the spinner object with dapter
        Category_spinner2.adapter = adapter3; // "5분이내","10분이내","15분이내","30분이내","1시간이내","2시간이내","2시간이상"
        if(Common.selected_recipe!!.category3.equals("5분이내")) {
            i=1
        }else if(Common.selected_recipe!!.category3.equals("10분이내")) {
            i=2
        }else if(Common.selected_recipe!!.category3.equals("15분이내")) {
            i=3
        }else if(Common.selected_recipe!!.category3.equals("30분이내")) {
            i=4
        }else if(Common.selected_recipe!!.category3.equals("1시간이내")) {
            i=5
        }else if(Common.selected_recipe!!.category3.equals("2시간이내")) {
            i=6
        }else if(Common.selected_recipe!!.category3.equals("2시간이상")) {
            i=7
        }else {
            i=8
        }
        Category_spinner2.setSelection(i)
        Category_spinner2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                // text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                R_U_spinner3 = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        // Set the drop down view resource
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        // Finally, data bind the spinner object with dapter
        Category_spinner3.adapter = adapter4;
        if(Common.selected_recipe!!.category4.equals("상")) {
            i=1
        }else if(Common.selected_recipe!!.category4.equals("중")) {
            i=2
        }else if(Common.selected_recipe!!.category4.equals("하")) {
            i=3
        }
        Category_spinner3.setSelection(i)
        Category_spinner3.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                // text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                R_U_spinner4 = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        // Set the drop down view resource
        adapter5.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        // Finally, data bind the spinner object with dapter
        Material_spinner.adapter = adapter5;
        Material_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                // text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                R_U_spinner5 = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        // 스피너끝
        //


        // Gson
        val gson = Gson()

        val json : String? = Common.selected_recipe!!.unit
        if(json != null && json !="") {
            UnitList = gson.fromJson(json,object : TypeToken<ArrayList<Unit>>() {}.type)
        }


        Recipe_update_material_recycler.setHasFixedSize(true)
        Recipe_update_material_recycler.layoutManager = GridLayoutManager(this,1)
        Recipe_update_material_recycler.adapter = Menu2UnitAdapter(this,UnitList)

        json2 = gson.toJson(UnitList)

        // ....................
        // 재료 추가 버튼을 누른다면......................
        Recipe_update_material_add.setOnClickListener {

            // 재료 리싸이클 리스트 추가 부분...
            UnitList.add(Unit(Recipe_update_material_title.text.toString(),Recipe_update_material_unit.text.toString()+R_U_spinner5))
            Recipe_update_material_recycler.adapter?.notifyDataSetChanged()
            //(Recipe_material_recycler.adapter as Menu2UnitAdapter).notifyDataSetChanged()

            json2 = gson.toJson(UnitList)

            // from json
            var UnitList2 = arrayListOf<Unit>()
            UnitList2 = gson.fromJson(json,object : TypeToken<ArrayList<Unit>>() {}.type)
            //Toast.makeText(this,R_U_spinner1+" 선택 "+R_U_spinner2+" 재료 리스트 크기는 "+UnitList.size+", title은 "+UnitList.get(0).title+", mount는 "+UnitList.get(0).amount+", json : "+json+", fromJson.title은 "+UnitList2.get(0).title,Toast.LENGTH_SHORT).show()
        }



        // ....................
        // 요리순서 추가 버튼을 누른다면.....................
        Recipe_update_order_recycler.setHasFixedSize(true)
        Recipe_update_order_recycler.layoutManager = GridLayoutManager(this,1)
        Recipe_update_order_recycler.adapter = Menu2OrderAdapter(this,this,OrderList)

        // 요리 순서 불러오기
        compositeDisposable.add(iMenu2API.UpdateOrderList(Common.selected_recipe!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ DorderList ->
                for(i in 0..Common.selected_recipe!!.order_number-1) {
                    OrderList.add(Order(DorderList.get(i).text, DorderList.get(i).image))
                }
                Recipe_update_order_recycler.adapter?.notifyDataSetChanged()
                //Recipe_update_order_recycler.adapter = Menu2OrderAdapter(this,this,DorderList)
                //Toast.makeText(this@Login,message,Toast.LENGTH_SHORT).show()

            })
        // 요리 순서 불러오기 끝
        Recipe_update_order_add_button.setOnClickListener {
            //
            // 순서 리싸이클러뷰 리스트 추가
            OrderList.add(Order("","http://192.168.56.1:3000/add_black.png")) // "http://192.168.56.1:3000/add_black.png" , fileUri.toString()
            Recipe_update_order_recycler.adapter?.notifyDataSetChanged()
            //Toast.makeText(this,DorderList.get(0).image, Toast.LENGTH_SHORT).show()
            Toast.makeText(this,OrderList.get(0).image, Toast.LENGTH_SHORT).show()
        }

       // Recipe_update_order_recycler.adapter = Menu2OrderAdapter(this,this,OrderList)



        //Request runtime permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PERMISSION_REQUEST)


        // 사진 이미지 선택
        recipe_update_upload_image1.setOnClickListener {

            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("사진 선택")
            dialog.setMessage("사진을 촬영하시거나, 갤러리에서 원하시는 사진을 선택해주세요.")
            //pick a photo from gallery
            fun d_p() {
                // Dev꺼 갤러리
                val getCountIntent = FileUtils.createGetContentIntent()
                val intent = Intent.createChooser(getCountIntent, "Select a file")
                startActivityForResult(intent,PICK_IMAGE_REQUEST)

                // Dev꺼 갤러리
                Toast.makeText(this,"갤러리를 선택했습니다", Toast.LENGTH_SHORT).show()
            }
            //ask for permission to take photo
            fun d_n() {
                Toast.makeText(this,"사진 촬영을 선택했습니다", Toast.LENGTH_SHORT).show()
                Dexter.withActivity(this)
                    .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                            if(report.areAllPermissionsGranted()){
                                //once permissions are granted, launch the camera
                                launchCamera()
                            }else{
                                Toast.makeText(this@recipeUpdate, "All permissions need to be granted to take photo", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                            token: PermissionToken?
                        ) {/* ... */
                            //show alert dialog with permission options
                            AlertDialog.Builder(this@recipeUpdate)
                                .setTitle(
                                    "Permissions Error!")
                                .setMessage(
                                    "Please allow permissions to take photo with camera")
                                .setNegativeButton(
                                    android.R.string.cancel,
                                    { dialog, _ ->
                                        dialog.dismiss()
                                        token?.cancelPermissionRequest()
                                    })
                                .setPositiveButton(android.R.string.ok,
                                    { dialog, _ ->
                                        dialog.dismiss()
                                        token?.continuePermissionRequest()
                                    })
                                .setOnDismissListener({
                                    token?.cancelPermissionRequest() })
                                .show()
                        }

                    }).check()
            }

            var dialog_listner = object : DialogInterface.OnClickListener{
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
        //
        // 작성하기 버튼을 눌럿을때
        recipe_update_submit.setOnClickListener { uploadFile() }
        // 작성하기 버튼 끝
    }

    // .......................................
    // 업로드 파일, 실질적으로 서버에 데이터를 보내는 부분....
    private fun uploadFile() {
        if (fileUri != null)
        {
            val file = FileUtils.getFile(this,fileUri)
            val requestFile = ProgressRequestBody(file,this)

            val body = MultipartBody.Part.createFormData("userfile",file.name,requestFile)

            Thread(Runnable {
                mService.uploadUpdateFile(body,Recipe_update_title.text.toString(),Recipe_update_text.text.toString(),json2,OrderList.size,Recipe_update_Tip.text.toString(),j,R_U_spinner1,R_U_spinner2,R_U_spinner3,R_U_spinner4,
                    Common.selected_recipe!!.id)
                    .enqueue(object: retrofit2.Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@recipeUpdate, t.message, Toast.LENGTH_LONG).show()
                        }
                        override fun onResponse(call: Call<String>, response: Response<String>) { // 업로드 완료
                            Toast.makeText(this@recipeUpdate, "업로딩 성공!!", Toast.LENGTH_LONG).show()
                            val nextIntent = Intent(this@recipeUpdate, MainActivity::class.java)
                            startActivity(nextIntent)
                            // 요리순서 업로드
                            uploadOrderFile()
                            // 요리순서 업로드
                        }

                    })
            }).start()
        }
        else {
            //Toast.makeText(this, "요리를 대표할 이미지를 선택해 주세요", Toast.LENGTH_LONG).show()
            Thread(Runnable {
                mService.uploadUpdateFile2(Recipe_update_title.text.toString(),Recipe_update_text.text.toString(),json2,OrderList.size,Recipe_update_Tip.text.toString(),j,R_U_spinner1,R_U_spinner2,R_U_spinner3,R_U_spinner4,
                    Common.selected_recipe!!.id)
                    .enqueue(object: retrofit2.Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@recipeUpdate, t.message, Toast.LENGTH_LONG).show()
                        }
                        override fun onResponse(call: Call<String>, response: Response<String>) { // 업로드 완료
                            Toast.makeText(this@recipeUpdate, "업로딩 성공!!", Toast.LENGTH_LONG).show()
                            val nextIntent = Intent(this@recipeUpdate, MainActivity::class.java)
                            startActivity(nextIntent)
                            // 요리순서 업로드
                            uploadOrderFile()
                            // 요리순서 업로드
                        }

                    })
            }).start()
        }
    }

    // 요리 순서 집어넣기.
    private fun uploadOrderFile() {
        // 업로드 된 마지막숫자 값
        compositeDisposable.add(myAPI.last_number()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ message ->
                r_i = message.toInt()
                for(i in 0..OrderList.size-1) {
                    if(i >= overNumber) { // 오더순서 수정에서 크기가 원래보다 작을 경우에는 추가를
                        if (OrderList[i].uri != null) { // 요리 순서란에서 이미지가 있을 경우.....
                            OfileUri = OrderList[i].uri

                            val file = FileUtils.getFile(this, OfileUri)
                            val requestFile = ProgressRequestBody(file, this)

                            val body = MultipartBody.Part.createFormData("orderfile", file.name, requestFile)

                            Thread(Runnable {
                                mService.uploadOrderFile(body, Common.selected_recipe!!.id, i + 1, OrderList[i].text)
                                    .enqueue(object : retrofit2.Callback<String> {
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Toast.makeText(this@recipeUpdate, t.message, Toast.LENGTH_LONG).show()
                                        }
                                        override fun onResponse(call: Call<String>, response: Response<String>) { // 업로드 완료
                                            Toast.makeText(this@recipeUpdate, "요리 순서 업로딩 성공", Toast.LENGTH_LONG).show()
                                        }
                                    })
                            }).start()
                        }
                        else { // 요리 순서란에서 이미지가 없는 경우.....
                            Thread(Runnable {
                                mService.uploadOrderFile(Common.selected_recipe!!.id, i + 1, OrderList[i].text)
                                    .enqueue(object : retrofit2.Callback<String> {
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Toast.makeText(this@recipeUpdate, t.message, Toast.LENGTH_LONG).show()
                                        }
                                        override fun onResponse(call: Call<String>, response: Response<String>) { // 업로드 완료
                                            Toast.makeText(this@recipeUpdate, "요리 순서 업로딩 성공", Toast.LENGTH_LONG).show()
                                        }
                                    })
                            }).start()
                        }
                    }
                    else { // 오더순서 수정에서 크기가 원래보다 작을 경우에는 수정을
                        if (OrderList[i].uri != null) { // 요리 순서란에서 이미지가 있을 경우.....
                            OfileUri = OrderList[i].uri

                            val file = FileUtils.getFile(this, OfileUri)
                            val requestFile = ProgressRequestBody(file, this)

                            val body = MultipartBody.Part.createFormData("orderfile", file.name, requestFile)

                            Thread(Runnable {
                                mService.uploadUpateOrderFile(body, Common.selected_recipe!!.id, i + 1, OrderList[i].text)
                                    .enqueue(object : retrofit2.Callback<String> {
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Toast.makeText(this@recipeUpdate, t.message, Toast.LENGTH_LONG).show()
                                        }
                                        override fun onResponse(call: Call<String>, response: Response<String>) { // 업로드 완료
                                            Toast.makeText(this@recipeUpdate, "요리 순서 업로딩 성공", Toast.LENGTH_LONG).show()
                                        }
                                    })
                            }).start()
                        }
                        else { // 요리 순서란에서 이미지가 없는 경우.....
                            Thread(Runnable {
                                mService.uploadUpdateOrderFile2(Common.selected_recipe!!.id, i + 1, OrderList[i].text)
                                    .enqueue(object : retrofit2.Callback<String> {
                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            Toast.makeText(this@recipeUpdate, t.message, Toast.LENGTH_LONG).show()
                                        }
                                        override fun onResponse(call: Call<String>, response: Response<String>) { // 업로드 완료
                                            Toast.makeText(this@recipeUpdate, "요리 순서 업로딩 성공", Toast.LENGTH_LONG).show()
                                        }
                                    })
                            }).start()
                        }
                    }

                } // for문 끝
            })
        // 업로드 된 마지막숫자 값



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

    //launch the camera to take photo via intent
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, AppConstants.TAKE_PHOTO_REQUEST)
        }
    }

    //override function that is called once the photo has been taken
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.TAKE_PHOTO_REQUEST) {
            //photo from camera 카메라선택
            //display the photo on the imageview
            recipe_update_upload_image1.setImageURI(fileUri)
        }else if(resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.PICK_PHOTO_REQUEST){
            //photo from gallery 갤러리 선택
            fileUri = data?.data
            recipe_update_upload_image1.setImageURI(fileUri)
        }else if(resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST){
            if(data != null)
            {
                fileUri = data.data
                if (fileUri != null && !fileUri!!.path.isEmpty())
                    recipe_update_upload_image1.setImageURI(fileUri)

            }
        }
        else if(resultCode == Activity.RESULT_OK
            && requestCode >= 3000 ){
            OfileUri = data?.data
            var i = requestCode-3000
            OrderList[i].image = OfileUri.toString()
            OrderList[i].uri = OfileUri
            Recipe_update_order_recycler.adapter?.notifyDataSetChanged()
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}
