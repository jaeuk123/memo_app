package com.test.memoapp.navigatiton

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


sealed interface Route

//sealed interface Route {
//    @Serializable data object Write : Route //글 작성 화면
//    @Serializable data object Main : Route //앱 메인 화면
//}
@Serializable
sealed interface MainRoute : Route {
    @Serializable
    data object Graph : MainRoute

    @Serializable data object Home : MainRoute
    @Serializable data object Calendar : MainRoute
    @Serializable data object Settings : MainRoute
}

sealed class BottomNavItem(
    val route : Route,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(MainRoute.Home, "Home", Icons.Default.Home)
    object Calendar : BottomNavItem(MainRoute.Calendar, "Calendar", Icons.Default.DateRange)
    object Settings : BottomNavItem(MainRoute.Settings, "Settings", Icons.Default.Settings)
}

val bottomNavItem = listOf(
    BottomNavItem.Home,
    BottomNavItem.Calendar,
    BottomNavItem.Settings
)