package com.test.memoapp.core.navigatiton

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.test.memoapp.calendar.navigation.calendarGraph
import com.test.memoapp.core.di.AppUiStateManager
import com.test.memoapp.memo.navigation.homeGraph
import com.test.memoapp.settings.navigation.settingsGraph
import kotlin.reflect.KClass

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: Route,
    padding: PaddingValues,
    appUiStateManager: AppUiStateManager,
) {

    val startDestination: KClass<out Any> = MainRoute.Graph::class

    NavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = startDestination
    ) {
        homeGraph(navController, appUiStateManager)
        calendarGraph(navController)
//        settingsGraph(navController)
//        navigation<MainRoute.Graph>(startDestination = MainRoute.Home::class) {
//            composable<CalendarRoute.Calendar> { CalendarScreen() }
////            composable<MainRoute.Home> { CalendarScreen() }
//            composable<SettingsRoute.Home> { SettingScreen() }
//        }
    }
}