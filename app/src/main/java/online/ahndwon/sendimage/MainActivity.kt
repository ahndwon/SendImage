package online.ahndwon.sendimage

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val BASE_URL = "http://10.0.2.2:3000"
        const val REQUEST_GALLERY = 111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GALLERY)
        }

        getImageButton.setOnClickListener { _ ->

            if(imageEditText.text.isNullOrBlank()) {
                Toast.makeText(this, "파일명을 입력하셈!", Toast.LENGTH_SHORT).show()
            }

            val fileName = imageEditText.text.toString()
            provideImageApi().getImage(fileName).enqueue({
                Glide.with(this)
                    .load(getRedirectedUrl(fileName))
                    .into(imageView)
                Toast.makeText(this, "겟이미지 성공 ^ㅡ^", Toast.LENGTH_SHORT).show()
            }, {
                it.printStackTrace()
                Toast.makeText(this, "겟이미지 실패 ㅠㅡㅠ", Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) {
            Log.d("TAG", data?.data.toString())

            provideImageApi().postImage().enqueue({ response ->
                Log.d("TAG", response.body()?.signedUrl)

                val path = data?.data ?: return@enqueue

                val tempFile = createTempFile()
                val inputStream = contentResolver.openInputStream(path) ?: return@enqueue
                inputStream.toFile(tempFile)

                val contentType = contentResolver.getType(path) ?: return@enqueue
                val requestBody = RequestBody.create(MediaType.parse(contentType), tempFile)

                response.body()?.signedUrl?.let { url ->
                    provideImageApi().putImage(url, contentType, requestBody).enqueue({
                        Toast.makeText(this, "전송 성공 ^ㅡ^", Toast.LENGTH_SHORT).show()
                        tempFile.delete()

                    }, {
                        it.printStackTrace()
                        Toast.makeText(this, "전송 실패 ㅠㅡㅠ", Toast.LENGTH_SHORT).show()

                    })
                }

            }, {

            })
        }
    }

    fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit, failure: (t: Throwable) -> Unit) {
        enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) = failure(t)
            override fun onResponse(call: Call<T>, response: Response<T>) = success(response)
        })
    }

    fun InputStream.toFile(file: File) {
        file.outputStream().use { this.copyTo(it) }
    }

    fun getRedirectedUrl(fileName: String) : String{
        return "$BASE_URL/images/$fileName"
    }
}
