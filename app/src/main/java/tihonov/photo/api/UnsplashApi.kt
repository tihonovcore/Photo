package tihonov.photo.api

import retrofit2.Call
import retrofit2.http.*

interface UnsplashApi {
    //TODO add Query
    @GET("random")
    fun getPhotoList(@QueryMap query: Map<String, String>): Call<List<Photo>>

//    @GET("repos/square/{name}/contributors")
//    fun getContributors(@Path("name") repoName: String, @Query("apiKey") apiKey: String): Call<List<User>>
//
//    @GET("users/{name}/repos")
//    fun getRepos(@Path("name") userName: String): Call<List<Repo>>
}