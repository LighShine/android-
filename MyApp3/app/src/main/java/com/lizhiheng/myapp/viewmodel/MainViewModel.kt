package com.lizhiheng.myapp.viewmodel

import android.graphics.Insets.add
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizhiheng.myapp.data_room.MyData
import com.lizhiheng.myapp.repository.MyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MyDataRepository,
    //private val authViewModel: AuthViewModel // 获取当前登录用户信息
) : ViewModel() {
    private val _expiredCount = MutableStateFlow(0)
    val expiredCount: StateFlow<Int> get() = _expiredCount

    private val _todayCount = MutableStateFlow(0)
    val todayCount: StateFlow<Int> get() = _todayCount

    private val _upcomingCount = MutableStateFlow(0)
    val upcomingCount: StateFlow<Int> get() = _upcomingCount

    private val _myDataList = MutableStateFlow<List<MyData>>(emptyList())
    val myDataList: StateFlow<List<MyData>> get() = _myDataList

    private val _allDataList = MutableStateFlow<List<MyData>>(emptyList())
    val allDataList: StateFlow<List<MyData>> get() = _allDataList

    private val _todayDataList = MutableStateFlow<List<MyData>>(emptyList())
    val todayDataList: StateFlow<List<MyData>> get() = _todayDataList

    private val _upcomingDataList = MutableStateFlow<List<MyData>>(emptyList())
    val upcomingDataList: StateFlow<List<MyData>> get() = _upcomingDataList

    private val _expiredDataList = MutableStateFlow<List<MyData>>(emptyList())
    val expiredDataList: StateFlow<List<MyData>> get() = _expiredDataList

    init {
        getTodayData()
        getUpcomingData()
        getExpiredData()
        getAllData()
    }

    fun getAll() {
        viewModelScope.launch {
            _myDataList.value = repo.getAllData()
        }
    }

    fun getAllData() {
        viewModelScope.launch {
            _allDataList.value = repo.getAllData()
        }
    }

    fun getTodayData() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(calendar.time)
            _todayDataList.value = repo.getTodayData(today)
            _todayCount.value = _todayDataList.value.size
        }
    }

    fun getUpcomingData() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 3)
            val threeDaysLater = dateFormat.format(calendar.time)
            _upcomingDataList.value = repo.getUpcomingData(today, threeDaysLater)
            _upcomingCount.value = _upcomingDataList.value.size
        }
    }

    fun getExpiredData() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(calendar.time)
            _expiredDataList.value = repo.getExpiredData(today)
            _expiredCount.value = _expiredDataList.value.size
        }
    }

    fun insert(content: String, time: String, kind: String) {
        viewModelScope.launch {
            if (content.isNotBlank() && time.isNotBlank() && kind.isNotBlank()) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsedDate = dateFormat.parse(time)
                val formattedTime = dateFormat.format(parsedDate)
                val obj = MyData(content=content, time=formattedTime,kind= kind)
                repo.insert(obj.content, obj.time, obj.kind)
                // 刷新数据
                getTodayData()
                getUpcomingData()
                getExpiredData()
                getAllData()
            }
        }
    }

    fun clear() {
        viewModelScope.launch {
            repo.clear()
            _todayDataList.value = emptyList()
            _upcomingDataList.value = emptyList()
            _expiredDataList.value = emptyList()
            _allDataList.value = emptyList()
            _todayCount.value = 0
            _upcomingCount.value = 0
            _expiredCount.value = 0
            println("记录数：${_todayDataList.value.size}")
        }
    }

    fun delete(data: MyData) {
        viewModelScope.launch {
            repo.delete(data)
            // 刷新数据
            getTodayData()
            getUpcomingData()
            getExpiredData()
            getAllData()
            println("记录数：${_todayDataList.value.size}")
        }
    }

    fun getItemById(itemId: Int): MyData? {
        return runBlocking { repo.getById(itemId) }
    }

    fun update(itemId: Int, content: String, time: String, kind: String) {
        val item = runBlocking { repo.getById(itemId) }
        item?.let {
            val updatedItem = it.copy(content = content, time = time, kind = kind)
            viewModelScope.launch {
                repo.update(updatedItem)
                getTodayData()
                getUpcomingData()
                getExpiredData()
                getAllData()
            }
        }
    }
    fun getExpiredCount() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(calendar.time)
            _expiredCount.value = repo.getExpiredCount(today)
        }
    }

    fun getTodayCount() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(calendar.time)
            _todayCount.value = repo.getTodayCount(today)
        }
    }

    fun getUpcomingCount() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 3)
            val threeDaysLater = dateFormat.format(calendar.time)
            _upcomingCount.value = repo.getUpcomingCount(today, threeDaysLater)
        }
    }


}



