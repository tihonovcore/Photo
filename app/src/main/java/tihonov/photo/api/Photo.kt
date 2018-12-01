package tihonov.photo.api

import com.squareup.moshi.Json

data class Urls(
        @field:Json(name = "full") var full: String?,
        @field:Json(name = "regular") var regular: String?
)

data class User(
        @field:Json(name = "name") var name: String?
)

data class Photo(
        @field:Json(name = "urls") var urls: Urls?,
        @field:Json(name = "user") var user: User?
)
