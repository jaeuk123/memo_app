package com.test.memoapp.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.test.memoapp.core.navigatiton.AppUiStateManager
import com.test.memoapp.core.navigatiton.SettingsRoute
import com.test.memoapp.settings.login.LoginScreen
import com.test.memoapp.settings.main.SettingMainScreen
import com.test.memoapp.settings.sign_up.SignUpScreen


fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    appUiStateManager: AppUiStateManager
) {
    navigation<SettingsRoute.Graph>(startDestination = SettingsRoute.SettingsHome::class) {
        composable<SettingsRoute.SettingsHome> {
            appUiStateManager.updateBottomBarVisibility(true)
            SettingMainScreen({ navController.navigate(SettingsRoute.Login) })
        }

        composable<SettingsRoute.Login> {
            appUiStateManager.updateBottomBarVisibility(false)
            LoginScreen(
                navSignUp = { navController.navigate(SettingsRoute.SignUp) },
                navBackPress = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
            )
        }

        composable<SettingsRoute.SignUp> {
            SignUpScreen(navBackPress = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },)
        }
    }
}
