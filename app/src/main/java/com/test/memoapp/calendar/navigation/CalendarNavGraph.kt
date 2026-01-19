package com.test.memoapp.calendar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.test.memoapp.calendar.screen.CalendarScreen
import com.test.memoapp.core.navigatiton.CalendarRoute


fun NavGraphBuilder.calendarGraph(navController: NavHostController) {
    navigation<CalendarRoute.Graph>(startDestination = CalendarRoute.Calendar::class) {
        composable<CalendarRoute.Calendar> {
            CalendarScreen()
        }
    }
}