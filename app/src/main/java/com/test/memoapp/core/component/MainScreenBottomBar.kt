package com.test.memoapp.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.test.memoapp.core.navigatiton.BottomNavItem
import com.test.memoapp.core.navigatiton.bottomNavItem

@Composable
fun MainScreenBottomBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        contentPadding = PaddingValues(0.dp),
        windowInsets = WindowInsets.navigationBars
    ) {
        var nowRoute by remember { mutableStateOf(BottomNavItem.Home.route) }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItem.forEach { bottomNavItem ->
                val isSelected = currentDestination?.hierarchy?.any { destination ->
                    destination.hasRoute(bottomNavItem.route::class)
                } == true

                BottomNavItemView(
                    modifier = Modifier.weight(1f),
                    imageVector = bottomNavItem.icon,
                    isSelected = isSelected,
                    text = bottomNavItem.title,
                    navigate = {
                        if (!isSelected) {
                            navController.navigate(bottomNavItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItemView(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    navigate: () -> Unit,
    imageVector: ImageVector,
    text: String
) {
    val colorSelected = if (isSelected) Color.Black else Color.Gray
    Column(modifier = modifier.clickable {
        if (!isSelected) {
            navigate.invoke()
        }
    }, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = imageVector, tint = colorSelected, contentDescription = "")
        Text(text, color = colorSelected)
    }
}

@Composable
@Preview
private fun PreviewBottomNaviBar() {
    MainScreenBottomBar(rememberNavController())
}