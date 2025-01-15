package com.lizhiheng.myapp.data_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDataDao {
    @Insert
    suspend fun insert(myData: MyData): Long

    @Query("SELECT * FROM mydata")
    suspend fun getAll(): List<MyData>

    @Query("SELECT * FROM mydata WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): MyData?

    @Query("DELETE FROM mydata")
    suspend fun clearDB()

    @Delete
    suspend fun delete(myData: MyData)

    @Query("SELECT * FROM mydata WHERE time = :today")
    suspend fun getTodayData(today: String): List<MyData>

    @Query("SELECT * FROM mydata WHERE time > :today AND time <= :threeDaysLater")
    suspend fun getUpcomingData(today: String, threeDaysLater: String): List<MyData>

    @Query("SELECT * FROM mydata WHERE time < :today")
    suspend fun getExpiredData(today: String): List<MyData>

    @Query("SELECT COUNT(*) FROM mydata WHERE time < :today")
    suspend fun getExpiredCount(today: String): Int

    @Query("SELECT COUNT(*) FROM mydata WHERE time = :today")
    suspend fun getTodayCount(today: String): Int

    @Query("SELECT COUNT(*) FROM mydata WHERE time > :today AND time <= :threeDaysLater")
    suspend fun getUpcomingCount(today: String, threeDaysLater: String): Int

    @Update
    suspend fun update(myData: MyData)
}