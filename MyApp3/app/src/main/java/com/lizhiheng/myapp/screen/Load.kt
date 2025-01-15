package com.lizhiheng.myapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lizhiheng.myapp.R
import com.lizhiheng.myapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: AuthViewModel = hiltViewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE2C9BB))
                .padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "登录",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF464945)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("用户名") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "用户名") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "密码") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.login(username, password) { success, usernameExists ->
                        if (success) {
                            navController.navigate("menu")
                        } else {
                            if (usernameExists) {
                                dialogMessage = "输入的密码不对"
                                showDialog = true
                            } else {
                                navController.navigate("register")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp) ,
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC6BAEB))
            ) {
                Text("登录", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC0BEBC))
            ) {
                Text("注册", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC0BEBC))
            ) {
                Text("返回", fontSize = 18.sp)
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("确定")
                        }
                    },
                    title = { Text("登录失败") },
                    text = { Text(dialogMessage) }
                )
            }
        }
    }
}
@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: AuthViewModel = hiltViewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFD0E4E9))
                .padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_userman),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "注册",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF464945)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("用户名") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "用户名") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "密码") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("确认密码") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "确认密码") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        password != confirmPassword -> {
                            dialogMessage = "两次输入的密码不一致，请重新输入"
                            showDialog = true
                        }
                        else -> {
                            viewModel.checkUsernameExists(username) { exists ->
                                if (exists) {
                                    dialogMessage = "错误，该用户名已被注册"
                                    showDialog = true
                                } else {
                                    viewModel.register(username, password) {
                                        navController.navigate("menu")
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFBAE2B6))
            ) {
                Text("注册", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC0BEBC))
            ) {
                Text("返回", fontSize = 18.sp)
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("确定")
                        }
                    },
                    title = { Text("错误") },
                    text = { Text(dialogMessage) }
                )
            }
        }
    }
}
@Composable
fun SwitchAccountScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val username by viewModel.usernameLiveData.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFD4EDDA))
                .padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_userman),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "当前账号",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDA9C6C)
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (username != null) {
                Text(text = "用户名: $username", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            androidx.compose.material.Button(
                onClick = {
                    viewModel.clearUsername()
                    navController.navigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEBCC9E))
            ) {
                Text("切换账号", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("menu") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEBCC9E))
            ) {
                Text("退出登录", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFC0BEBC))
            ) {
                Text("返回", fontSize = 18.sp)
            }
        }
    }
}
