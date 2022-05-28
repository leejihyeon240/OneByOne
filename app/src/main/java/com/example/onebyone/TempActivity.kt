package com.example.onebyone

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface.LATIN_AND_KOREAN
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import java.io.FileNotFoundException
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
    var btn_detection_image : Button? = null
    var recognizer //텍스트 인식에 사용될 모델
            : TextRecognizer? = null

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
        }


    }

    @Override
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            // 갤러리에서 선택한 사진에 대한 uri를 가져온다.
            uri = data!!.data
            setImage(uri!!)
        }
    }

    // uri를 비트맵으로 변환시킨후 이미지뷰에 띄워주고 InputImage를 생성하는 메서드
    private fun setImage(uri: Uri) {
        try {
            val `in`: InputStream? = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(`in`)
            imageView!!.setImageBitmap(bitmap)
            image = InputImage.fromBitmap(bitmap!!, 0)
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
                val resultText = visionText.text
                text_info!!.text = resultText // 인식한 텍스트를 TextView에 세팅
            } // 이미지 인식에 실패하면 실행되는 리스너
            .addOnFailureListener { e -> Log.e("텍스트 인식", "실패: " + e.message) }
    }
}