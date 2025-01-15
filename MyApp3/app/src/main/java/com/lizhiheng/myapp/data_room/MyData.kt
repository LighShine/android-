package com.lizhiheng.myapp.data_room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mydata")
data class MyData(
    @PrimaryKey(autoGenerate = true)val id:Int=0,
    val content:String,
    val time: String,
    val kind:String,
    //val userId:String
)