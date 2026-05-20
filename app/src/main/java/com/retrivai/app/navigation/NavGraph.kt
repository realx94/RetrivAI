package com.retrivai.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.retrivai.app.ui.albums.AlbumsScreen
import com.retrivai.app.ui.gallery.GalleryScreen
import com.retrivai.app.ui.photo.PhotoDetailScreen
import com.retrivai.app.ui.privacy.PrivacyDashboardScreen
import com.retrivai.app.ui.search.SearchScreen
import com.retrivai.app.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    data object Gallery : Screen("gallery")
    data object Search : Screen("search")
    data object Albums : Screen("albums")
    data object Settings : Screen("settings")
    data object Privacy : Screen("privacy")
    data object PhotoDetail : Screen("photo_detail/{photoIndex}") {
        fun createRoute(photoIndex: Long) = "photo_detail/$photoIndex"
    }
}

data class BottomNavItem(val screen: Screen, val label: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Gallery, "Gallery", Icons.Default.Image),
    BottomNavItem(Screen.Search, "Search", Icons.Default.Search),
    BottomNavItem(Screen.Albums, "Albums", Icons.Default.Collections),
    BottomNavItem(Screen.Settings, "Settings", Icons.Default.Settings)
)

val bottomNavRoutes = bottomNavItems.map { it.screen.route }.toSet()

@Composable
fun RetrivNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(Screen.Gallery.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Gallery.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Gallery.route) {
                GalleryScreen(
                    onPhotoClick = { photoIndex ->
                        navController.navigate(Screen.PhotoDetail.createRoute(photoIndex.toLong()))
                    }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen()
            }

            composable(Screen.Albums.route) {
                AlbumsScreen()
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateToPrivacy = { navController.navigate(Screen.Privacy.route) }
                )
            }

            composable(Screen.Privacy.route) {
                PrivacyDashboardScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.PhotoDetail.route,
                arguments = listOf(
                    navArgument("photoIndex") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val photoIndex = backStackEntry.arguments?.getLong("photoIndex") ?: 0L
                PhotoDetailScreen(
                    initialPhotoIndex = photoIndex.toInt(),
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}