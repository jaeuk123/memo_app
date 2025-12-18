package com.test.memoapp.core.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.test.memoapp.core.navigatiton.BottomNavItem
import com.test.memoapp.core.navigatiton.Route

@Composable
fun MainScreenBottomBar() {
    val navController = rememberNavController()
    BottomAppBar(
        contentPadding = PaddingValues(0.dp),
        windowInsets = WindowInsets.navigationBars
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItemView(
                modifier = Modifier.weight(1f),
                imageVector = BottomNavItem.Home.icon,
                isSelected = true,
                route = BottomNavItem.Home.route,
                navController = navController,
                text = BottomNavItem.Home.title
            )
            BottomNavItemView(
                modifier = Modifier.weight(1f),
                imageVector = BottomNavItem.Calendar.icon,
                isSelected = false,
                route = BottomNavItem.Calendar.route,
                navController = navController,
                text = BottomNavItem.Calendar.title
            )
            BottomNavItemView(
                modifier = Modifier.weight(1f),
                imageVector = BottomNavItem.Settings.icon,
                isSelected = false,
                route = BottomNavItem.Settings.route,
                navController = navController,
                text = BottomNavItem.Settings.title
            )
        }
    }
}

@Composable
private fun BottomNavItemView(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    route: Route,
    navController: NavController,
    imageVector: ImageVector,
    text: String
) {
    val colorSelected = if (isSelected) Color.Black else Color.Gray
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = imageVector, tint = colorSelected, contentDescription = "")
        Text(text, color = colorSelected)
    }
}

@Composable
@Preview
private fun PreviewBottomNaviBar() {
    MainScreenBottomBar()
}