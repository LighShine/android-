package com.lizhiheng.myapp.image_room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lizhiheng.myapp.util.Converters

@Database(entities = [MyImage::class], version = 10, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyImagesDataBase : RoomDatabase() {
    abstract fun myImagesDao(): MyImagesDao
}
