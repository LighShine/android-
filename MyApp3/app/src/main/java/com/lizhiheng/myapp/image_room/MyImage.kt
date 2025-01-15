package com.lizhiheng.myapp.image_room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "images")
data class MyImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val imageData: List<String>,  // Base64 编码的图片数据列表
    //val userId: String            // 添加 userId 字段
    val timestamp:Long
)

