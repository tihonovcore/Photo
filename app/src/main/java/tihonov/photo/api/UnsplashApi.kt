package tihonov.photo.api

import retrofit2.Call
import retrofit2.http.GET

interface UnsplashApi {
    //TODO add Query
    @GET("random/?query=beautiful-girls&count=30&client_id=d3b053ebcb9d702a5612e128b366cfb4f14dbfd72b2d08fd995a990c08611719")
    fun getPhotoList(): Call<List<Photo>>
}