package tihonov.photo.api

import retrofit2.Call
import retrofit2.http.*

interface UnsplashApi {
    @GET("random")
    fun getPhotoList(@QueryMap query: Map<String, String>): Call<List<Photo>>
}
