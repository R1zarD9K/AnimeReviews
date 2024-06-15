package org.d3if3136.mobpro1.asessmen3rahmat.system.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.model.Anime
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.FormUrlEncoded as FormUrlEncoded1

private const val BASE_URL = "https://fenris-api-host.000webhostapp.com/files/Rahmat/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

data class AnimeResponse(
    val results: List<Anime>
)

interface AnimeServices {
    @GET("getAllAnime.php") // Replace with the actual endpoint
    suspend fun getAllAnime(): AnimeResponse

    @Multipart
    @POST("addAnime.php") // Replace with the actual endpoint
    suspend fun addAnime(
        @Part("email") email: RequestBody,
        @Part("animeTitle") animeTitle: RequestBody,
        @Part("animeReview") animeReview: RequestBody,
        @Part cover: MultipartBody.Part?
    ): AnimeResponse

    @FormUrlEncoded1
    @POST("deleteAnime.php") // Replace with the actual endpoint
    suspend fun deleteAnime(
        @Field("id") id: String
    ): AnimeResponse
}

object AnimeAPI {
    val retrofitService: AnimeServices by lazy {
        retrofit.create(AnimeServices::class.java)
    }
    fun imgUrl(imageId: String): String {
        return "$BASE_URL$imageId"
    }
}

enum class AnimeStatus { LOADING, SUCCESS, FAILED }
