package com.lizhiheng.myapp.repository

import com.lizhiheng.myapp.image_room.MyImage
import com.lizhiheng.myapp.image_room.MyImagesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
class ImageRepository @Inject constructor(private val myImagesDao: MyImagesDao) {

    fun getAllImages(): Flow<List<MyImage>> {
        return myImagesDao.getAllImages()
    }

    suspend fun insertImage(myImage: MyImage) {
        myImagesDao.insert(myImage)
    }

    suspend fun updateImage(myImage: MyImage) {
        myImagesDao.update(myImage)
    }

    suspend fun deleteImage(myImage: MyImage) {
        myImagesDao.delete(myImage)
    }
}


