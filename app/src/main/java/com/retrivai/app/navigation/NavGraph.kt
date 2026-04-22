package com.retrivai.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.retrivai.app.ui.gallery.GalleryScreen
import com.retrivai.app.ui.photo.PhotoDetailScreen

sealed class Screen(val route: String) {
    data object Gallery : Screen("gallery")
    data object PhotoDetail : Screen("photo_detail/{photoIndex}") {
        fun createRoute(photoIndex: Long) = "photo_detail/$photoIndex"
    }
}

@Composable
fun RetrivNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Gallery.route
    ) {
        composable(Screen.Gallery.route) {
            GalleryScreen(
                onPhotoClick = { photoIndex ->
                    navController.navigate(Screen.PhotoDetail.createRoute(photoIndex.toLong()))
                }
            )
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