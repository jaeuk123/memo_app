package com.test.memoapp.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.test.memoapp.core.navigatiton.SettingsRoute
import com.test.memoapp.settings.main.SettingMainScreen


fun NavGraphBuilder.settingsGraph(navController: NavHostController){
    navigation<SettingsRoute.Graph>(startDestination = SettingsRoute.SettingsHome::class) {
        composable<SettingsRoute.SettingsHome> {
            SettingMainScreen()
        }
    }
}