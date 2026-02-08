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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.test.memoapp.core.navigatiton.BottomNavItem

@Composable
fun MainScreenBottomBar(navController : NavHostController) {
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
            BottomNavItemView(
                modifier = Modifier.weight(1f),
                imageVector = BottomNavItem.Home.icon,
                isSelected = (nowRoute == BottomNavItem.Home.route),
                navigate = {
                    navController.navigate(BottomNavItem.Home.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                    nowRoute = BottomNavItem.Home.route
                },
                text = BottomNavItem.Home.title
            )
            BottomNavItemView(
                modifier = Modifier.weight(1f),
                imageVector = BottomNavItem.Calendar.icon,
                isSelected = (nowRoute == BottomNavItem.Calendar.route),
                navigate = {
                    navController.navigate(BottomNavItem.Calendar.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                    nowRoute = BottomNavItem.Calendar.route
                },
                text = BottomNavItem.Calendar.title
            )
            BottomNavItemView(
                modifier = Modifier.weight(1f),
                imageVector = BottomNavItem.Settings.icon,
                isSelected = (nowRoute == BottomNavItem.Settings.route),
                navigate = {
                    navController.navigate(BottomNavItem.Settings.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                    nowRoute = BottomNavItem.Settings.route
                },
                text = BottomNavItem.Settings.title
            )
        }
    }
}

@Composable
private fun BottomNavItemView(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    navigate : () -> Unit,
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