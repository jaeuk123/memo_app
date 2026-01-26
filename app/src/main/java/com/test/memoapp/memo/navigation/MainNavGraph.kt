package com.test.memoapp.memo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material3.Icon
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.test.memoapp.core.di.AppUiStateManager
import com.test.memoapp.core.di.FabAction
import com.test.memoapp.core.navigatiton.MainRoute
import com.test.memoapp.memo.list.HomeScreen
import com.test.memoapp.memo.write.WriteScreen

fun NavGraphBuilder.homeGraph(navController: NavHostController, uiStateManager: AppUiStateManager) {
    navigation<MainRoute.Graph>(startDestination = MainRoute.Home::class) {
        composable<MainRoute.Home> {
            DisposableEffect(Unit) {
                uiStateManager.updateBottomBarVisibility(true)

                uiStateManager.updateFab(
                    FabAction(
                        icon = { Icon(Icons.Filled.BorderColor, contentDescription = "Write") },
                        onclick = { navController.navigate(MainRoute.Write()) }
                    )
                )

                onDispose {
                    uiStateManager.updateFab(null)
                }
            }
            HomeScreen({ memoId ->
                println("memoId = ${memoId}")
                navController.navigate(MainRoute.Write(memoId))
            })
        }

        composable<MainRoute.Write> { backStackEntry ->
            DisposableEffect(Unit) {
                uiStateManager.updateBottomBarVisibility(false)
                uiStateManager.updateFab(null)
                onDispose { }
            }
            val itemId = backStackEntry.toRoute<MainRoute.Write>().memoId ?: -1L
            WriteScreen(onBackClick = { navController.popBackStack() }, itemId = itemId)
        }
    }
}