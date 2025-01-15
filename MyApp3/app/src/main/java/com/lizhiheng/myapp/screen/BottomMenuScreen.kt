package com.lizhiheng.myapp.screen
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

//密封类
sealed class BottomMenuScreen(
    //路由
    val route:String,
    val icon: ImageVector,
    val title:String
){
    object Home:BottomMenuScreen("Home", Icons.Default.Home,"Home")
    object Record:BottomMenuScreen("Record", Icons.Default.Favorite,"Record")
    object Menu:BottomMenuScreen("Menu", Icons.Default.Menu,"Menu")
}
@Composable
fun BottomMenu(navController: NavController){
    val menuItems = listOf<BottomMenuScreen>(
        BottomMenuScreen.Home,
        BottomMenuScreen.Record,
        BottomMenuScreen.Menu
    )
    BottomNavigation(contentColor= Color.White, backgroundColor = Color(0xFFA0B1A6)){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            BottomNavigationItem(
                label = { Text(text = it.route) },
                alwaysShowLabel = true,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route)
                },
                icon = { Icon(imageVector = it.icon, contentDescription = it.title) }
            )
        }
    }
}