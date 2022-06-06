package com.example.onebyone

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.onebyone.DlogUtil.DlogUtil
import com.example.onebyone.PermissionUtil.PermissionUtil
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraActivity"
    }

    /*CameraActivity 기존 코드 시작*/

    private lateinit var previewView: PreviewView
    private lateinit var imageViewPhoto: ImageView
    private lateinit var btn_get_image: ImageView
    private lateinit var frameLayoutShutter: FrameLayout
    private lateinit var frameLayoutPreview: FrameLayout
    private lateinit var imageViewPreview: ImageView
    private lateinit var imageViewCancel: ImageView



    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var cameraAnimationListener: Animation.AnimationListener

    private var savedUri: Uri? = null

    /*CameraActivity 기존 코드 끝*/

    val REQUEST_CODE = 2

    var uri // 갤러리에서 가져온 이미지에 대한 Uri
            : Uri? = null
    var bitmap // 갤러리에서 가져온 이미지를 담을 비트맵
            : Bitmap? = null
    var image // ML 모델이 인식할 인풋 이미지
            : InputImage? = null
    var text_info // ML 모델이 인식한 텍스트를 보여줄 뷰
            : TextView? = null
    var btn_finish: ImageView? = null
    var recognizer //텍스트 인식에 사용될 모델
            : TextRecognizer? = null

    var list: List<String> = emptyList<String>() // 모든 인식된 텍스트 저장
    var list_name: ArrayList<String> = ArrayList() // 인식된 텍스트 중 상품명 저장
    var list_price: ArrayList<Int> = ArrayList() // 인식된 텍스트 중 가격 저장

    var resultText: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        findView() // findViewById 진행
        permissionCheck() //카메라 권한 체크
        setListener() //리스너 등록
        setCameraAnimationListener()

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    @Override
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        Log.d("hyerrmrmrm", "a")

        // 앨범에서 사진 가져오기

        if (requestCode  == REQUEST_CODE) {

            Log.d("hyerrmrmrm", "b")
            if (Build.VERSION.SDK_INT >= 19) {

                Log.d("hyerrmrmrm", "c")
                uri = data!!.data // 선택한 이미지의 주소
                Log.d("hyerrmrmrm", "1")
                // 사용자가 이미지를 선택했으면(null이 아니면)
                if (uri != null) {
                    cropImage(uri)
                    Log.d("hyerrmrmrm", "2")
                } else {
                    Log.d("hyerrmrmrm", "3")
                }
            }
        }

        Log.d("hyerrmrmrm", "4")

        // 크롭해서 이미지 받아오기
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                result.uri?.let {
                    // 이미지 파일 읽어와서 설정하기
                    val bitmap = BitmapFactory.decodeStream(
                        contentResolver!!.openInputStream(result.uri!!)
                        // 프레그먼트명 activity?.contentResolver!!.openInputStream(result.uri!!)
                    )
                    setImage(result.uri)

                }
            }
        }

    }


    //findViewById 모음
    private fun findView() {
        previewView = findViewById(R.id.previewView)
        imageViewPhoto = findViewById(R.id.imageViewPhoto)

        frameLayoutShutter = findViewById(R.id.frameLayoutShutter)
        imageViewPreview = findViewById(R.id.imageViewPreview)
        frameLayoutPreview = findViewById(R.id.frameLayoutPreview)


        btn_get_image = findViewById(R.id.btn_get_image);
        btn_finish = findViewById(R.id.btn_finish);
        text_info = findViewById(R.id.text_info);

    }

    //카메라 권한 설정
    private fun permissionCheck() {

        var permissionList =
            listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!PermissionUtil.checkPermission(this, permissionList)) {
            PermissionUtil.requestPermission(this, permissionList)
        } else {
            openCamera() //권한 확인 되면 카메라 오픈
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            DlogUtil.d(TAG, "승인")
            openCamera()
        } else {
            DlogUtil.d(TAG, "승인 거부")
            onBackPressed()
        }
    }


    //클릭 리스너
    private fun setListener() {

        //카메라 촬옇하기 버튼 클릭
        imageViewPhoto.setOnClickListener {
            savePhoto()
        }

        //갤러리에서 사진 가져오기 버튼 클릭
        btn_get_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_CODE)
        }

        //완료 버튼 클릭
        btn_finish!!.setOnClickListener {

            //지현이가 여기서 액티비티 넘어갈 때 list_name, list_price 넘겨서 쓰면 될듯!

            val intent = Intent(this, CameraAddActivity::class.java)
            intent.putExtra("LIST_NAME",list_name)
            intent.putExtra("LIST_PRICE",list_price)
            startActivity(intent)
            finish()

        }

    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    //카메라 허용 후 카메라 보이기
    private fun openCamera() {
        DlogUtil.d(TAG, "openCamera")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                DlogUtil.d(TAG, "바인딩 성공")

            } catch (e: Exception) {
                DlogUtil.d(TAG, "바인딩 실패 $e")
            }
        }, ContextCompat.getMainExecutor(this))

    }

    private fun savePhoto() {
        imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yy-mm-dd", Locale.US).format(System.currentTimeMillis()) + ".png"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    DlogUtil.d(TAG, "savedUri : $savedUri")

                    val animation =
                        AnimationUtils.loadAnimation(this@CameraActivity, R.anim.camera_shutter)
                    animation.setAnimationListener(cameraAnimationListener)
                    frameLayoutShutter.animation = animation
                    frameLayoutShutter.visibility = View.VISIBLE
                    frameLayoutShutter.startAnimation(animation)


                    DlogUtil.d(TAG, "imageCapture")
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    onBackPressed()
                }

            })

    }

    //카메라 촬영 버튼 누르고 나서 실행되는 애니메이션 리스너
    private fun setCameraAnimationListener() {
        cameraAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                frameLayoutShutter.visibility = View.GONE


                //카메라로 찍은 사진 크롭하러 가기
                if (savedUri != null) {
                    cropImage(savedUri)
                    Log.d("hyerrmrmrm", "2")
                } else {
                    Log.d("hyerrmrmrm", "3")
                }

//                showCaptureImage()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        }
    }

    //촬영한 이미지 보여주기
    private fun showCaptureImage(): Boolean {
        if (frameLayoutPreview.visibility == View.GONE) {
            frameLayoutPreview.visibility = View.VISIBLE

//            // Uri to Bitmap
//            val bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), savedUri)
//            val bitmap2 : Bitmap = GetBinaryBitmap(bitmap)
//            savedUri = getImageUri(getApplicationContext(), bitmap2)
//

            imageViewPreview.setImageURI(savedUri)

            //uri를 비트맵으로 변경
//            imageViewPreview.setImageBitmap(bitmap2)
            return false
        }

        return true

    }



    private fun hideCaptureImage() {
        imageViewPreview.setImageURI(null)
        frameLayoutPreview.visibility = View.GONE

    }

    override fun onBackPressed() {
        if (showCaptureImage()) {
            DlogUtil.d(TAG, "CaptureImage true")
            hideCaptureImage()
        } else {
            onBackPressed()
            DlogUtil.d(TAG, "CaptureImage false")

        }
    }

    // uri를 비트맵으로 변환시킨후 이미지뷰에 띄워주고 InputImage를 생성하는 메서드
    private fun setImage(uri: Uri) {
        try {

            val `in`: InputStream? = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(`in`)

            // 사진의 회전 정보 가져오기 : hyerm
            val orientation = getOrientationOfImage(uri).toFloat()
            // 이미지 회전하기 : hyerm
            val newBitmap = getRotatedBitmap(bitmap, orientation)

            if (frameLayoutPreview.visibility == View.GONE) {
                frameLayoutPreview.visibility = View.VISIBLE
                imageViewPreview!!.setImageBitmap(newBitmap)
            }


            var binaryBitmap = newBitmap

            // 제대로된 방향으로!! : hyerm
            image = InputImage.fromBitmap(binaryBitmap!!, 0)

            TextRecognition(recognizer!!)
            getList()
            Log.e("setImage", "이미지 to 비트맵")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


    //텍스트 인식 -yeonji
    private fun TextRecognition(recognizer: TextRecognizer) {
        val result: Task<Text> = recognizer.process(image!!) // 이미지 인식에 성공하면 실행되는 리스너
            .addOnSuccessListener { visionText ->
                Log.e("텍스트 인식", "성공")
                // Task completed successfully
                resultText = visionText.text

                getList()

                text_info!!.text = list_name.toString() + list_price.toString() // 인식한 텍스트를 TextView에 세팅

            } // 이미지 인식에 실패하면 실행되는 리스너
            .addOnFailureListener { e -> Log.e("텍스트 인식", "실패: " + e.message) }
    }

    // 이미지 회전하기 : hyerm
    @Throws(Exception::class)
    private fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    // 이미지 회전 정보 가져오기 : hyerm
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getOrientationOfImage(uri: Uri): Int {
        // uri -> inputStream
        val inputStream = contentResolver.openInputStream(uri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

        // 회전된 각도 알아내기
        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    // 사진 크롭하기
    private fun cropImage(uri: Uri?) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)  // 크롭 위한 가이드 열어서 크롭할 이미지 받아오기
            .setCropShape(CropImageView.CropShape.RECTANGLE)            // 사각형으로 자르기
            .start(this@CameraActivity)
        // 프레그먼트에서 사용할 땐 .start(activity as 프레그먼트의 부모 Activity, this@형재 프레그먼트 이름)
    }

    //사진에서 가져온 텍스르 리스트로 분류하기
    private fun getList() {

        list_name.clear()
        list_price.clear()

        list = resultText.split("\n")
        for (i in list) {
            Log.d("hyerm", i)
            val regex = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")
            if (i.matches(regex)) {
                list_name.add(i.replace("*", ""))
                Log.d("hyerm-name", i)
            }
        }

        for (n in 0 until list_name.size) {
            val index = (list.size) - 1 - n
            list_price.add(list[(list.size) - 1 - n].replace("[^0-9]".toRegex(), "").toInt())
            Log.d("hyerm-price", list[(list.size) - 1 - n])
        }

        list_price.reverse()
        Log.d("hyerm-result-name", list_name.toString())
        Log.d("hyerm-result-price", list_price.toString())
    }


    // 이진화 함수 3개
//
//    private fun GetBinaryBitmap(bitmap_src: Bitmap): Bitmap? {
//        val bitmap_new = bitmap_src.copy(bitmap_src.config, true)
//        for (x in 0 until bitmap_new.width) {
//            for (y in 0 until bitmap_new.height) {
//                var color = bitmap_new.getPixel(x, y)
//                color = GetNewColor(color)
//                bitmap_new.setPixel(x, y, color)
//            }
//        }
//        return bitmap_new
//    }
//
//    private fun GetNewColor(c: Int): Int {
//        val dwhite: Double = GetColorDistance(c, Color.WHITE)
//        val dblack: Double = GetColorDistance(c, Color.BLACK) * 3
//        if (dwhite <= dblack) {
//            return Color.WHITE
//        } else {
//            return Color.BLACK
//        }
//    }
//
//    private fun GetColorDistance(c1: Int, c2: Int): Double {
//        val db = Color.blue(c1) - Color.blue(c2)
//        val dg = Color.green(c1) - Color.green(c2)
//        val dr = Color.red(c1) - Color.red(c2)
//        return Math.sqrt(
//            Math.pow(db.toDouble(), 2.0) + Math.pow(dg.toDouble(), 2.0) + Math.pow(
//                dr.toDouble(),
//                2.0
//            ), /* !!! Hit visitElement for element type: class org.jetbrains.kotlin.nj2k.tree.JKErrorExpression !!! */
//        )
//    }

    // bitmap to uri
    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }


}