package com.test.memoapp.core.navigatiton

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
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
    @Serializable data object Settings : MainRoute

    @Serializable data class Write(val memoId: Long? = null) : MainRoute
    @Serializable data class Detail(val memoId: Long? = null) : MainRoute
    @Serializable data class TagMemoList(val tagId : Long? = null) : MainRoute
    @Serializable data object LastWriteList : MainRoute
}

sealed class BottomNavItem(
    val route : Route,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(MainRoute.Home, "Home", Icons.Default.Home)
    object Calendar : BottomNavItem(CalendarRoute.Calendar , "Calendar", Icons.Default.DateRange)
//    object Settings : BottomNavItem(SettingsRoute.SettingsHome, "Settings", Icons.Default.Settings)
}

val bottomNavItem = listOf(
    BottomNavItem.Home,
    BottomNavItem.Calendar,
//    BottomNavItem.Settings
)