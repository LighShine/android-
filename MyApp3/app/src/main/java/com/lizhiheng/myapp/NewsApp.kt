package com.lizhiheng.myapp

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lizhiheng.myapp.screen.BottomMenu
import com.lizhiheng.myapp.screen.HomeNavigator
import com.lizhiheng.myapp.screen.Homeview
import com.lizhiheng.myapp.screen.Menu
import com.lizhiheng.myapp.screen.MenuScreen
import com.lizhiheng.myapp.screen.Record
import com.lizhiheng.myapp.viewmodel.AuthViewModel
import com.lizhiheng.myapp.viewmodel.MainViewModel


@Composable
fun NewsApp() {
    //处理状态
    val scrollState= rememberScrollState()
    //获取NavHostController实例，支持重组的
    val navController = rememberNavController()
    //显示主页面
    MainScreen(navController,scrollState)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController,scrollState: ScrollState){
    Scaffold(bottomBar = {
        //显示底部导航栏
       BottomMenu(navController = navController)
    }) {
        //定义路由表，显示主页面
        Navigation(navController,scrollState)
    }
}
//搭建程序导航路线图（实际上就是路由表）
@Composable
fun Navigation(
    navController: NavHostController,
    scrollState:ScrollState
) {
    //关联上NavController实例，并且指明起始页
    NavHost(navController = navController, startDestination = "Home") {
        //composable()这是一个扩展函数，实际上就是定义了路由规则

        composable("Home") {
            // 使用 hiltViewModel 来获取 ViewModel 实例
            val viewModel: MainViewModel = hiltViewModel()
            HomeNavigator()
        }
        //展示如何定义与接收外部参数
        /*composable(
            "Detail/{newsId}",
            arguments = listOf(navArgument("newsId") {
                type = NavType.IntType
            })
        ) {
            //提取id值，按id值查找数据，再传给组合函数
            val newsId=it.arguments?.getInt("newsId")
            DetailScreen(navController = navController,
                MockData.getNews(newsId))
        }*/
        composable("Record") {
            val authViewModel: AuthViewModel = hiltViewModel()
            Record()
        }
        composable("Menu") {
            MenuScreen()
        }
    }
}

