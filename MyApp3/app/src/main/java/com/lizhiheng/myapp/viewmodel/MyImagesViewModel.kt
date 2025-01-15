package com.lizhiheng.myapp.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lizhiheng.myapp.image_room.MyImage
import com.lizhiheng.myapp.image_room.MyImagesDao
import com.lizhiheng.myapp.repository.ImageRepository
import com.lizhiheng.myapp.util.ConvertImage
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MyImagesViewModel @Inject constructor(private val repository: ImageRepository) : ViewModel() {
    val images = repository.getAllImages().asLiveData()

    fun addImage(title: String, description: String, bitmaps: List<Bitmap>) {
        viewModelScope.launch {
            val currentTimestamp = System.currentTimeMillis()
            val imageData = bitmaps.map { ConvertImage.convertToString(it)!! }
            repository.insertImage(MyImage(title = title, description = description, imageData = imageData,timestamp=currentTimestamp))
        }
    }

    fun updateImage(id: Int, title: String, description: String, bitmaps: List<Bitmap>) {
        viewModelScope.launch {
            val currentTimestamp = System.currentTimeMillis()
            val imageData = bitmaps.map { ConvertImage.convertToString(it)!! }
            repository.updateImage(MyImage(id = id, title = title, description = description, imageData = imageData, timestamp =currentTimestamp))
        }
    }

    fun deleteImage(image: MyImage) {
        viewModelScope.launch {
            repository.deleteImage(image)
        }
    }
}

