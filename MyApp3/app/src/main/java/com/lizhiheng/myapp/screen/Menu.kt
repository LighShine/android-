package com.lizhiheng.myapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lizhiheng.myapp.R
import com.lizhiheng.myapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun MenuScreen() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "menu") {
        composable("menu") { Menu(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("switch_account") { SwitchAccountScreen(navController) }
        composable("punch_in") { PunchInScreen(navController) }
    }
}
@Composable
fun Menu(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val username by viewModel.usernameLiveData.observeAsState()
    val punchInDays = username?.let { viewModel.getPunchInDays(it) } ?: 0
    val effortPoints = username?.let { viewModel.getEffortPoints(it) } ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("个人中心", fontSize = 36.sp, fontWeight = FontWeight.Bold,color=Color(0xFF472816))

        Spacer(modifier = Modifier.height(26.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clickable {
                    if (username != null) {
                        navController.navigate("switch_account")
                    } else {
                        navController.navigate("login")
                    }
                }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = username ?: "点我登录",
            fontSize = 25.sp,
            modifier = Modifier.clickable {
                if (username == null) {
                    navController.navigate("login")
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
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
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                ) {
                    androidx.compose.material3.Text(
                        "累计打卡小习惯",
                        fontSize = 16.sp,
                        color = Color(0xFF856404)
                    )
                    androidx.compose.material3.Text(
                        "${punchInDays}天",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF856404)
                    )
                }
            }
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
                    androidx.compose.material3.Text(
                        "积累了:",
                        fontSize = 16.sp,
                        color = Color(0xFF856404)
                    )
                    androidx.compose.material3.Text(
                        "${effortPoints}个努力值",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF856404)
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        MainFeaturesGrid(navController, username)
    }
}
@Composable
fun PunchInScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val username by viewModel.usernameLiveData.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // 获取当前时间并动态更新
    val currentTime = remember { mutableStateOf("") }
    val currentDate = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Calendar.getInstance().time
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val dateFormat = SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.getDefault())
            currentTime.value = timeFormat.format(now)
            currentDate.value = dateFormat.format(now)
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0E2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部栏
        androidx.compose.material.TopAppBar(
            title = { Text("打卡", color = Color.White) },
            backgroundColor = Color(0xFF97B5DB)
        )

        Spacer(modifier = Modifier.height(100.dp))

        // 显示当前时间和日期
        Text(
            text = currentTime.value,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF754E2A)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = currentDate.value,
            fontSize = 18.sp,
            color = Color(0xFF7C5635)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 打卡按钮
        androidx.compose.material.Card(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFFCEDDA4),
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (username != null) {
                    Text(
                        text = "每日打卡",
                        fontSize = 36.sp,
                        color = Color(0xFF7C5635),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            viewModel.punchIn { success, alreadyPunchedInToday ->
                                if (success) {
                                    dialogMessage = "打卡成功！"
                                    showDialog = true
                                } else {
                                    dialogMessage = if (alreadyPunchedInToday) {
                                        "你今天已经打卡了，不能再打卡了哦"
                                    } else {
                                        "打卡失败，请重试。"
                                    }
                                    showDialog = true
                                }
                            }
                        },
                        colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(
                            0xFF7FA394
                        )
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text("打卡", fontSize = 18.sp, color = Color.White)
                    }
                } else {
                    Text(
                        text = "请先登录",
                        fontSize = 16.sp,
                        color = Color(0xFF0288D1)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            navController.navigate("login")
                        },
                        colors =androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0288D1)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text("登录", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("确定")
                    }
                },
                title = { Text("提示") },
                text = { Text(dialogMessage) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigateUp() },
            colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(
                0xFFCEBBAB
            )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("返回", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun MainFeaturesGrid(navController: NavController, username: String?) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FeatureCard(R.drawable.ic_card, "打卡", navController, username)
            FeatureCard(R.drawable.ic_phone, "分享", navController, username)
            FeatureCard(R.drawable.ic_xun, "勋章", navController, username)
        }

    }
}


@Composable
fun FeatureCard(iconRes: Int, title: String, navController: NavController, username: String?) {
    Column(
        modifier = Modifier
            .size(120.dp)
            .padding(8.dp)
            .clickable {
                when (title) {
                    "打卡" -> {
                        if (username != null) {
                            navController.navigate("punch_in")
                        } else {
                            navController.navigate("login")
                        }
                    }
                    // Handle other cases if needed
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Text(title, fontSize = 20.sp)
    }
}