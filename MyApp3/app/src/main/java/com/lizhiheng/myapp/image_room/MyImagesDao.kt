package com.lizhiheng.myapp.image_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface MyImagesDao {
    @Insert
    suspend fun insert(myImage: MyImage)

    @Delete
    suspend fun delete(myImage: MyImage)

    @Update
    suspend fun update(myImage: MyImage)

    @Query("SELECT * FROM images  ORDER BY id DESC")
    fun getAllImages(): Flow<List<MyImage>>
}
