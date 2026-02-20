package com.test.memoapp

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.test.memoapp.core.component.MainScreenBottomBar
import com.test.memoapp.core.navigatiton.AppUiStateManager
import com.test.memoapp.core.navigatiton.MainNavHost
import com.test.memoapp.core.navigatiton.MainRoute

@Composable
fun MemoApp(
) {
    val navController = rememberNavController()
    val uiStateManager: AppUiStateManager = hiltViewModel()
    val uiState by uiStateManager.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            if (uiState.showBottomBar) {
                MainScreenBottomBar(navController)
            }
        },
        floatingActionButton = {
            uiState.fabAction?.let { fab ->
                FloatingActionButton(onClick = fab.onclick) {
                    fab.icon.invoke()
                }
            }
        }) { paddingValues ->
        MainNavHost(navController, MainRoute.Home, padding = paddingValues, uiStateManager)
    }
}