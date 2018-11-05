package online.ahndwon.sendimage

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ImageApi {

    @POST("/images")
    fun postImage(): Call<SignedUrl>

    @GET("/images/{filename}")
    fun getImage(@Path("filename") fileName: String ): Call<ResponseBody>

    @PUT
    fun putImage(
        @Url url: String,
        @Header("Content-Type") contentType: String,
        @Body file: RequestBody
    ): Call<Void>
}