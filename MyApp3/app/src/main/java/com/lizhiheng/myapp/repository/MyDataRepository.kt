package com.lizhiheng.myapp.repository

import com.lizhiheng.myapp.data_room.MyData
import com.lizhiheng.myapp.data_room.MyDataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

//定义Repository，注入Room的Dao组件
class MyDataRepository @Inject constructor(private val dao: MyDataDao) {

    suspend fun getAllData(): List<MyData> {
        return withContext(Dispatchers.IO) {
            dao.getAll()
        }
    }
    suspend fun getById(id: Int): MyData? {
        return withContext(Dispatchers.IO) {
            dao.getById(id)
        }
    }
    suspend fun getTodayData(today: String): List<MyData> {
        return withContext(Dispatchers.IO) {
            dao.getTodayData(today)
        }
    }

    suspend fun getUpcomingData(today: String, threeDaysLater: String): List<MyData> {
        return withContext(Dispatchers.IO) {
            dao.getUpcomingData(today, threeDaysLater)
        }
    }

    suspend fun getExpiredData(today: String): List<MyData> {
        return withContext(Dispatchers.IO) {
            dao.getExpiredData(today)
        }
    }
    suspend fun insert(content:String,time:String,kind:String):MyData {
        val obj = MyData(content=content,time=time,kind=kind)
        return  withContext(Dispatchers.IO) {
            dao.insert(myData = obj)
            obj
        }
    }

    suspend fun clear(){
        withContext(Dispatchers.IO){
            dao.clearDB()
        }
    }
    suspend fun delete(myData: MyData){
        withContext(Dispatchers.IO){
            dao.delete(myData)
        }
    }
    suspend fun update(myData: MyData) {
        withContext(Dispatchers.IO) {
            dao.update(myData)
        }
    }
    suspend fun getExpiredCount(today: String): Int {
        return withContext(Dispatchers.IO) {
            dao.getExpiredCount(today)
        }
    }

    suspend fun getTodayCount(today: String): Int {
        return withContext(Dispatchers.IO) {
            dao.getTodayCount(today)
        }
    }

    suspend fun getUpcomingCount(today: String, threeDaysLater: String): Int {
        return withContext(Dispatchers.IO) {
            dao.getUpcomingCount(today, threeDaysLater)
        }
    }
}