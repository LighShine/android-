package com.lizhiheng.myapp.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn


import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.OutlinedButton
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lizhiheng.myapp.R
import com.lizhiheng.myapp.data_room.MyData
import com.lizhiheng.myapp.viewmodel.AuthViewModel
import com.lizhiheng.myapp.viewmodel.MainViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun HomeNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "homeview_home") {
        composable("homeview_home") { Homeview(navController) }
        composable("all_data_view") { AllDataView(navController) }
        composable("future_data_view") { FutureDataView(navController) }
        composable("past_data_view") { PastDataView(navController) }
    }
}

@Composable
fun DateInput(date: MutableState<String>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
        Text(
            text = if (date.value.isEmpty()) "请选择一个日期" else "选定日期：${date.value}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = androidx.compose.ui.Modifier.padding(bottom = 16.dp)
        )
        IconButton(onClick = {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    date.value = dateFormat.format(calendar.time)
                },
                year,
                month,
                day
            ).show()
        }) {
            Icon(Icons.Filled.CalendarToday, contentDescription = "选择日期")
        }
    }
}

@Composable
fun AllDataView(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val dataList by viewModel.allDataList.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getAllData()
    }

    DataListScreen(navController, dataList, "所有数据", viewType = "all") { item ->
        coroutineScope.launch {
            viewModel.delete(item)
            viewModel.getAllData()
        }
    }
}

@Composable
fun FutureDataView(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val dataList by viewModel.upcomingDataList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getUpcomingData()
    }

    DataListScreen(navController, dataList, "即将到期",  "upcoming") { item ->
        coroutineScope.launch {
            viewModel.delete(item)
            viewModel.getUpcomingData()
        }
    }
}

@Composable
fun PastDataView(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val dataList by viewModel.expiredDataList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getExpiredData()
    }

    DataListScreen(navController, dataList, "过期事件", viewType = "expired") { item ->
        coroutineScope.launch {
            viewModel.delete(item)
            viewModel.getExpiredData()
        }
    }
}
@Composable
fun DataListScreen(navController: NavController, dataList: List<MyData>, title: String, viewType: String, onDelete: (MyData) -> Unit) {
    val listState = rememberLazyListState()
    val editDialog = remember { mutableStateOf(false) }
    val itemToEdit = remember { mutableStateOf<MyData?>(null) }

    androidx.compose.material.Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { androidx.compose.material.Text(title) },
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                backgroundColor = Color(0xFF97B5DB),
                contentColor = Color.White
            )
        },
        content = { innerPadding ->
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material.MaterialTheme.colors.background)
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                androidx.compose.material.Card(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = Color(0xFFCFCBA7)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = androidx.compose.ui.Modifier.padding(16.dp)
                    ) {
                        if(viewType =="all"){
                        androidx.compose.material.Text(
                            text = "你所有未完成的事件：${dataList.size}",
                            style = androidx.compose.material.MaterialTheme.typography.subtitle1,
                            color = androidx.compose.material.MaterialTheme.colors.onSurface
                        )}
                        if(viewType =="upcoming"){
                            androidx.compose.material.Text(
                                text = "你即将过期的事件：${dataList.size}",
                                style = androidx.compose.material.MaterialTheme.typography.subtitle1,
                                color = androidx.compose.material.MaterialTheme.colors.onSurface
                            )}
                        if(viewType =="expired"){
                            androidx.compose.material.Text(
                                text = "你已经过期的事件：${dataList.size}",
                                style = androidx.compose.material.MaterialTheme.typography.subtitle1,
                                color = androidx.compose.material.MaterialTheme.colors.onSurface
                            )}
                    }
                }
                Divider(
                    color = androidx.compose.material.MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = androidx.compose.ui.Modifier.padding(vertical = 8.dp)
                )
                LazyColumn(state = listState, contentPadding = PaddingValues(vertical = 8.dp)) {
                    itemsIndexed(dataList, key = { index, item -> item.id }) { index, item ->
                        ListItem(
                            index = index,
                            item = item,
                            navController = navController,
                            onDismissed = {
                                onDelete(item)
                            },
                            onEdit = {
                                itemToEdit.value = item
                                editDialog.value = true
                            },
                            viewType = viewType
                        )
                    }
                }
            }
        }
    )

    if (editDialog.value) {
        itemToEdit.value?.let {
            EditDataDialog(editDialog, hiltViewModel(), it)
        }
    }
}

@Composable
fun Homeview(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val dataList by viewModel.todayDataList.collectAsState()
    val expiredCount by viewModel.expiredCount.collectAsState()
    val todayCount by viewModel.todayCount.collectAsState()
    val upcomingCount by viewModel.upcomingCount.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val editDialog = remember { mutableStateOf(false) }
    val itemToEdit = remember { mutableStateOf<MyData?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getTodayData()
        viewModel.getExpiredCount()
        viewModel.getTodayCount()
        viewModel.getUpcomingCount()
    }

    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { Text("我的计划") },
                actions = {
                    IconButton(onClick = { openDialog.value = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "添加")
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "更多选项")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        androidx.compose.material.DropdownMenuItem(onClick = {
                            expanded = false
                            navController.navigate("all_data_view")
                        }) {
                            Text("所有事件")
                        }
                        androidx.compose.material.DropdownMenuItem(onClick = {
                            expanded = false
                            navController.navigate("future_data_view")
                        }) {
                            Text("即将到期")
                        }
                        androidx.compose.material.DropdownMenuItem(onClick = {
                            expanded = false
                            navController.navigate("past_data_view")
                        }) {
                            Text("过期事件")
                        }
                    }
                }
                , backgroundColor = Color(0xFF97B5DB)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = androidx.compose.ui.Modifier.fillMaxWidth()
            ) {
                Text(
                    "Today plans",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    //color = MaterialTheme.colors.primary
                )
            }
            Spacer(modifier = androidx.compose.ui.Modifier.height(32.dp))
            Row(
                modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                androidx.compose.material.Card(
                    backgroundColor = Color(0xFFF8D7DA),
                    shape = RoundedCornerShape(16.dp),
                    modifier = androidx.compose.ui.Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = androidx.compose.ui.Modifier.padding(16.dp)
                    ) {
                        Text("已过期", fontSize = 16.sp, color = Color(0xFF721C24))
                        Text(
                            "$expiredCount",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF721C24)
                        )
                    }
                }
                androidx.compose.material.Card(
                    backgroundColor = Color(0xFFD4EDDA),
                    shape = RoundedCornerShape(16.dp),
                    modifier = androidx.compose.ui.Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = androidx.compose.ui.Modifier.padding(16.dp)
                    ) {
                        Text("今天", fontSize = 16.sp, color = Color(0xFF155724))
                        Text(
                            "$todayCount",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF155724)
                        )
                    }
                }
                androidx.compose.material.Card(
                    backgroundColor = Color(0xFFFFF3CD),
                    shape = RoundedCornerShape(16.dp),
                    modifier = androidx.compose.ui.Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = androidx.compose.ui.Modifier.padding(13.dp)
                    ) {
                        Text("即将到期", fontSize = 16.sp, color = Color(0xFF856404))
                        Text(
                            "$upcomingCount",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF856404)
                        )
                    }
                }
            }
            Spacer(modifier =androidx.compose.ui. Modifier.height(16.dp))
            LazyColumn(
                modifier = androidx.compose.ui.Modifier.fillMaxSize()
            ) {
                itemsIndexed(dataList, key = { _, item -> item.id }) { index, item ->
                    ListItem(
                        index = index,
                        item = item,
                        navController = navController,
                        onDismissed = {
                            coroutineScope.launch {
                                viewModel.delete(item)
                                viewModel.getTodayData()
                            }
                        },
                        onEdit = {
                            itemToEdit.value = item
                            editDialog.value = true
                        }, viewType = "today"
                    )
                }
            }
        }
    }

    if (openDialog.value) {
        AddNewDataDialog(openDialog, viewModel)
    }

    if (editDialog.value) {
        itemToEdit.value?.let {
            EditDataDialog(editDialog, viewModel, it)
        }
    }
}
@Composable
fun EditDataDialog(openDialog: MutableState<Boolean>, viewModel: MainViewModel, item: MyData) {
    val content = remember { mutableStateOf(item.content) }
    val date = remember { mutableStateOf(item.time) }
    val kind = remember { mutableStateOf(item.kind) }
    val expanded = remember { mutableStateOf(false) }

    val kinds = listOf("学习", "工作", "生活","运动")

    androidx.compose.material.AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = { Text(text = "修改", style = androidx.compose.material.MaterialTheme.typography.h6) },
        text = {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                androidx.compose.material.TextField(
                    value = content.value,
                    onValueChange = { content.value = it },
                    label = { Text("内容") },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor =androidx.compose.material. MaterialTheme.colors.surface
                    )
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                DateInput(date)
                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                Box {
                    androidx.compose.material. TextField(
                        readOnly = true,
                        value = kind.value,
                        onValueChange = { },
                        label = { Text("类型") },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                androidx.compose.ui.Modifier.clickable {
                                    expanded.value = !expanded.value
                                }
                            )
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = androidx.compose.material.MaterialTheme.colors.surface
                        )
                    )
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        kinds.forEach { selection ->
                            androidx.compose.material.DropdownMenuItem(onClick = {
                                kind.value = selection
                                expanded.value = false
                            }) {
                                Text(text = selection)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            androidx.compose.material.Button(
                onClick = {
                    viewModel.update(item.id, content.value, date.value, kind.value)
                    openDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(backgroundColor =Color(0xFFE7C49E))
            ) {
                Text("保存", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { openDialog.value = false },
                colors = ButtonDefaults.buttonColors(backgroundColor =Color(0xFFBDDDD5))
            ) {
                Text("取消")
            }
        },
        backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
        contentColor = androidx.compose.material.MaterialTheme.colors.onBackground
    )
}


@Composable
fun AddNewDataDialog(openDialog: MutableState<Boolean>, viewModel: MainViewModel) {
    val content = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val kind = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    val kinds = listOf("学习", "工作", "生活","锻炼")

    androidx.compose.material.AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = { Text(text = "新增", style = androidx.compose.material.MaterialTheme.typography.h6) },
        text = {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                androidx.compose.material.TextField(
                    value = content.value,
                    onValueChange = { content.value = it },
                    label = { Text("内容") },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = androidx.compose.material.MaterialTheme.colors.surface
                    )
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                DateInput(date)
                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                Box {
                    androidx.compose.material.TextField(
                        readOnly = true,
                        value = kind.value,
                        onValueChange = { },
                        label = { Text("类型") },
                        trailingIcon = {
                            androidx.compose.material.Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                androidx.compose.ui.Modifier.clickable {
                                    expanded.value = !expanded.value
                                }
                            )
                        },
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = androidx.compose.material.MaterialTheme.colors.surface
                        )
                    )
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        kinds.forEach { selection ->
                            androidx.compose.material.DropdownMenuItem(onClick = {
                                kind.value = selection
                                expanded.value = false
                            }) {
                                Text(text = selection)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            androidx.compose.material.Button(
                onClick = {
                    viewModel.insert(content.value, date.value, kind.value)
                    openDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(backgroundColor =Color(0xFFE7C49E))
            ) {
                Text("添加", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { openDialog.value = false },
                colors = ButtonDefaults.buttonColors(backgroundColor =Color(0xFFE7C49E))
            ) {
                Text("取消")
            }
        },
        backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
        contentColor = androidx.compose.material.MaterialTheme.colors.onBackground
    )
}


@Composable
fun ListItem(index: Int, item: MyData, navController: NavController, onDismissed: () -> Unit, onEdit: () -> Unit,viewType:String) {
    var offsetX by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    // 计算时间差
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val itemDate = dateFormat.parse(item.time)
    val currentDate = Calendar.getInstance().time

    val diffInMillis = itemDate.time - currentDate.time
    val diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

    val gradientBackground = when {
        diffInDays < 0 -> Brush.horizontalGradient(listOf(Color(0xFFA991CA), Color(0xFF8F6DB1))) // 暗红色渐变
        diffInDays == 0 -> Brush.horizontalGradient(listOf(Color(0xFFC9BE9A), Color(0xFFD6D456))) // 暗橙色渐变
        diffInDays in 1..3 -> Brush.horizontalGradient(listOf(Color(0xFF9CC08E), Color(0xFF8FC070))) // 暗黄色渐变
        else -> Brush.horizontalGradient(listOf(Color(0xFF9EBBCF), Color(0xFF51978F))) // 暗绿色渐变
    }

    Box(
        modifier = androidx.compose.ui.Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        // 左滑删除按钮
        IconButton(
            onClick = {
                coroutineScope.launch {
                    onDismissed()
                }
            },
            modifier = androidx.compose.ui.Modifier
                .align(Alignment.CenterEnd)
                .offset(x = offsetX.dp + 100.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                modifier = androidx.compose.ui.Modifier.size(40.dp)
            )
        }

        // 右滑完成按钮
        IconButton(
            onClick = {
                coroutineScope.launch {
                    onDismissed()
                }
            },
            modifier =androidx.compose.ui.Modifier
                .align(Alignment.CenterStart)
                .offset(x = offsetX.dp - 100.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_finish),
                contentDescription = null,
                modifier = androidx.compose.ui.Modifier.size(40.dp)
            )
        }

        androidx.compose.material.Card(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .offset(x = offsetX.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        val newOffset = (offsetX + dragAmount).coerceIn(-100f, 100f)
                        offsetX = newOffset
                    }
                }
                .clickable {
                    onEdit()
                },
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier =androidx.compose.ui. Modifier
                    .background(gradientBackground)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = null,

                            modifier = androidx.compose.ui.Modifier.size(24.dp)
                        )
                        Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                        Text(
                            text = "计划: ${item.content}",
                            color = Color.White,
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.height(4.dp))
                    Row(
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_timetable),
                            contentDescription = null,

                            modifier = androidx.compose.ui.Modifier.size(24.dp)
                        )
                        Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                        Text(
                            text = "类型: ${item.kind}",
                            color = Color.White,
                            style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                        )
                    }
                    if (viewType == "all") {
                        Spacer(modifier = androidx.compose.ui.Modifier.height(4.dp))
                        Row(
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_phone),
                                contentDescription = null,

                                modifier = androidx.compose.ui.Modifier.size(24.dp)
                            )
                            Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                            Text(
                                text = "截止时间: ${item.time}",
                                color = Color.White,
                                style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                    if (viewType == "upcoming") {
                        Spacer(modifier = androidx.compose.ui.Modifier.height(4.dp))
                        Row(
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_phone),
                                contentDescription = null,
                                modifier = androidx.compose.ui.Modifier.size(24.dp)
                            )
                            Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                            Text(
                                text = "距离到期还有: ${diffInDays} 天",
                                color = Color.White,
                                style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                            )
                        }
                    }
                    if (viewType == "expired") {
                        Spacer(modifier = androidx.compose.ui.Modifier.height(4.dp))
                        Row(
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_plan),
                                contentDescription = null,

                                modifier =androidx.compose.ui. Modifier.size(24.dp)
                            )
                            Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                            Text(
                                text = "已过期: ${-diffInDays} 天",
                                color = Color.White,
                                style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                            )
                        }
                    }

                }
                androidx.compose.material.Button(
                    onClick = { /* Do Something */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    shape = RoundedCornerShape(50),
                    modifier = androidx.compose.ui.Modifier.padding(start=250.dp)
                ) {
                    Text(text = "开始", color = Color(0xFF228B22))  // 设置按钮文字颜色
                    }
                }
            }
        }
    }





