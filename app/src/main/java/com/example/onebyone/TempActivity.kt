package com.example.onebyone

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream


open class TempActivity : AppCompatActivity() {

    val REQUEST_CODE = 2

    var imageView // 갤러리에서 가져온 이미지를 보여줄 뷰
            : ImageView? = null
    var uri // 갤러리에서 가져온 이미지에 대한 Uri
            : Uri? = null
    var bitmap // 갤러리에서 가져온 이미지를 담을 비트맵
            : Bitmap? = null
    var image // ML 모델이 인식할 인풋 이미지
            : InputImage? = null
    var text_info // ML 모델이 인식한 텍스트를 보여줄 뷰
            : TextView? = null
    var btn_get_image: Button? = null
    var btn_detection_image: Button? = null
    var recognizer //텍스트 인식에 사용될 모델
            : TextRecognizer? = null

    var list: List<String> = emptyList<String>() // 모든 인식된 텍스트 저장
    var list_name: ArrayList<String> = ArrayList() // 인식된 텍스트 중 상품명 저장
    var list_price: ArrayList<Int> = ArrayList() // 인식된 텍스트 중 가격 저장

    var resultText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        imageView = findViewById(R.id.imageView);
        text_info = findViewById(R.id.text_info);
        recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        btn_get_image = findViewById(R.id.btn_get_image);

        btn_get_image!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_CODE)
        }

        btn_detection_image = findViewById(R.id.btn_detection_image);
        btn_detection_image!!.setOnClickListener {
            TextRecognition(recognizer!!)
            list = resultText.split("\n")
            for (i in list) {
                Log.d("hyerm", i)
                if (i.startsWith("*")) {
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


    }





    @Override
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            // 갤러리에서 선택한 사진에 대한 uri를 가져온다.
            uri = data!!.data

            // 사용자가 이미지를 선택했으면(null이 아니면) crop!
            if (uri != null) {
                cropImage(uri)
            }

            setImage(uri!!)
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

            imageView!!.setImageBitmap(newBitmap) // 제대로된 방향으로!! : hyerm
            image = InputImage.fromBitmap(newBitmap!!, 0)
            Log.e("setImage", "이미지 to 비트맵")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun TextRecognition(recognizer: TextRecognizer) {
        val result: Task<Text> = recognizer.process(image!!) // 이미지 인식에 성공하면 실행되는 리스너
            .addOnSuccessListener { visionText ->
                Log.e("텍스트 인식", "성공")
                // Task completed successfully
                resultText = visionText.text
                text_info!!.text = resultText // 인식한 텍스트를 TextView에 세팅
            } // 이미지 인식에 실패하면 실행되는 리스너
            .addOnFailureListener { e -> Log.e("텍스트 인식", "실패: " + e.message) }
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

    // 이미지 회전하기 : hyerm
    @Throws(Exception::class)
    private fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    // 사진 크롭하기
    private fun cropImage(uri: Uri?) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)  // 크롭 위한 가이드 열어서 크롭할 이미지 받아오기
            .setCropShape(CropImageView.CropShape.RECTANGLE)            // 사각형으로 자르기
            .start(this@TempActivity)
        // 프레그먼트에서 사용할 땐 .start(activity as 프레그먼트의 부모 Activity, this@형재 프레그먼트 이름)
    }
}