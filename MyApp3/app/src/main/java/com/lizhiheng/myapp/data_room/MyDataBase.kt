package com.lizhiheng.myapp.data_room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [MyData::class],
    version = 9,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDataDao(): MyDataDao
}
