package com.test.memoapp.memo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material3.Icon
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.test.memoapp.core.navigatiton.AppUiStateManager
import com.test.memoapp.core.navigatiton.FabAction
import com.test.memoapp.core.navigatiton.MainRoute
import com.test.memoapp.memo.detail.MemoDetailScreen
import com.test.memoapp.memo.lastwriteList.LastWriteMemoListScreen
import com.test.memoapp.memo.list.HomeScreen
import com.test.memoapp.memo.memolist.MemoListScreen
import com.test.memoapp.memo.write.WriteScreen

fun NavGraphBuilder.homeGraph(navController: NavHostController, uiStateManager: AppUiStateManager) {
    navigation<MainRoute.Graph>(startDestination = MainRoute.Home::class) {
        composable<MainRoute.Home> {
            DisposableEffect(Unit) {
                uiStateManager.updateBottomBarVisibility(true)
                uiStateManager.updateFab(
                    FabAction(
                        icon = { Icon(Icons.Filled.BorderColor, contentDescription = "Write") },
                        onclick = {
                            navController.navigate(MainRoute.Write()) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        }
                    )
                )

                onDispose {
                    uiStateManager.updateFab(null)
                }
            }
            HomeScreen(modifyMemoNavigate = { memoId ->
                navController.navigate(MainRoute.Write(memoId))
            }, detailMemoNavigate = { memoId ->
                navController.navigate(MainRoute.Detail(memoId))
            }, tagMemoNavigate = { tagId ->
                navController.navigate(MainRoute.TagMemoList(tagId))
            }, lastWriteNavigate = { navController.navigate(MainRoute.LastWriteList) })
        }

        composable<MainRoute.LastWriteList> { backStackEntry ->
            uiStateManager.updateBottomBarVisibility(false)

            LastWriteMemoListScreen(navigateDetail = { memoId ->
                navController.navigate(MainRoute.Detail(memoId))
            }, onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            })
        }

        composable<MainRoute.Write> { backStackEntry ->
            DisposableEffect(Unit) {
                uiStateManager.updateBottomBarVisibility(false)
                uiStateManager.updateFab(null)
                onDispose { }
            }
            val memoId = backStackEntry.toRoute<MainRoute.Write>().memoId
            WriteScreen(onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            }, memoId = memoId)
        }

        composable<MainRoute.TagMemoList> { backStackEntry ->
            DisposableEffect(Unit) {
                uiStateManager.updateBottomBarVisibility(false)
                uiStateManager.updateFab(null)
                onDispose { }
            }

            val tagId = backStackEntry.toRoute<MainRoute.TagMemoList>().tagId!!

            MemoListScreen(
                onBackClick = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                navigateModify = { id ->
                    navController.navigate(
                        MainRoute.Write(id)
                    )
                },
                navigateDetail = { id ->
                    navController.navigate(
                        MainRoute.Detail(id)
                    )
                },
                tagId = tagId
            )

        }

        composable<MainRoute.Detail> { backStackEntry ->
            DisposableEffect(Unit) {
                uiStateManager.updateBottomBarVisibility(false)
                uiStateManager.updateFab(null)
                onDispose { }
            }

            val memoId = backStackEntry.toRoute<MainRoute.Detail>().memoId!!
            MemoDetailScreen(
                onBackClick = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                navigateModify = { modifyMemoId ->
                    navController.navigate(
                        MainRoute.Write(modifyMemoId)
                    )
                },
                memoId = memoId
            )
        }
    }
}