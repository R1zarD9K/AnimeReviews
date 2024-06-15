package org.d3if3136.mobpro1.asessmen3rahmat.system.database

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.model.Anime
import org.d3if3136.mobpro1.asessmen3rahmat.system.network.AnimeAPI
import org.d3if3136.mobpro1.asessmen3rahmat.system.network.AnimeStatus
import java.io.ByteArrayOutputStream

class mainViewModel : ViewModel() {
    private val _animeData = MutableLiveData<List<Anime>>()
    val animeData: LiveData<List<Anime>> get() = _animeData

    var _status = MutableStateFlow(AnimeStatus.LOADING)
        private set


    init {
        getAllAnime()
    }

    fun getAllAnime() {
        viewModelScope.launch {
            _status.value = AnimeStatus.LOADING
            try {
                val response = AnimeAPI.retrofitService.getAllAnime()
                _animeData.value = response.results
                _status.value = AnimeStatus.SUCCESS
                Log.d("AnimeViewModel", "Success fetching anime data: ${response.results}")
            } catch (e: Exception) {
                _status.value = AnimeStatus.FAILED
                Log.e("AnimeViewModel", "Error fetching anime data: ${e.message}")
            }
        }
    }

    fun addAnime(email: String, animeTitle: String, animeReview: String, cover: Bitmap?) {
        viewModelScope.launch {
            _status.value = AnimeStatus.LOADING
            try {
                val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val animeTitlePart = animeTitle.toRequestBody("text/plain".toMediaTypeOrNull())
                val animeReviewPart = animeReview.toRequestBody("text/plain".toMediaTypeOrNull())

                val coverPart: MultipartBody.Part? = cover?.let {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val requestBody = byteArrayOutputStream.toByteArray().toRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("cover", "cover.jpg", requestBody)
                }

                val response = AnimeAPI.retrofitService.addAnime(emailPart, animeTitlePart, animeReviewPart, coverPart)
                _animeData.value = response.results
                _status.value = AnimeStatus.SUCCESS
                getAllAnime()
            } catch (e: Exception) {
                _status.value = AnimeStatus.FAILED
                Log.e("AnimeViewModel", "Error adding anime: ${e.message}")
            }
        }
    }


    fun deleteAnime(id: String) {
        viewModelScope.launch {
            _status.value = AnimeStatus.LOADING
            try {
                val response = AnimeAPI.retrofitService.deleteAnime(id)
                _animeData.value = response.results
                _status.value = AnimeStatus.SUCCESS
                getAllAnime()
            } catch (e: Exception) {
                _status.value = AnimeStatus.FAILED
                Log.e("AnimeViewModel", "Error deleting anime: ${e.message}")
            }
        }
    }

}
