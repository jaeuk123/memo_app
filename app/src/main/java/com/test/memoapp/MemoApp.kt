package com.test.memoapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.test.memoapp.component.MainScreenBottomBar
import com.test.memoapp.screen.CalendarScreen
import com.test.memoapp.screen.HomeScreen
import com.test.memoapp.screen.SettingScreen
import com.test.memoapp.navigatiton.MainRoute
import com.test.memoapp.navigatiton.Route
import kotlin.reflect.KClass

@Composable
fun MemoApp() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        MainScreenBottomBar()
    }) { paddingValues ->
        MainNavHost(navController, MainRoute.Home, padding = paddingValues)
    }

}

@Composable
fun MainNavHost(navController: NavHostController, startDestination: Route, padding: PaddingValues) {
    val startDestination: KClass<out Any> = MainRoute.Graph::class

    NavHost(navController = navController, startDestination = startDestination) {
        navigation<MainRoute.Graph>(startDestination = MainRoute.Home::class) {
            composable<MainRoute.Home> { HomeScreen() }
            composable<MainRoute.Calendar> { CalendarScreen() }
            composable<MainRoute.Settings> { SettingScreen() }
        }
    }
}