package org.d3if3136.mobpro1.asessmen3rahmat.system.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.mainViewModel
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.model.User
import org.d3if3136.mobpro1.asessmen3rahmat.ui.screen.PublicGrid

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier, viewModel: mainViewModel, user: User) {
    NavHost(
        navController = navController,
        startDestination = Screen.Base.route
    ) {
        /*----------------[Main Route]----------------*/
        composable(route = Screen.Base.route) {
            PublicGrid("Home", viewModel = viewModel, modifier = modifier, user)
        }
    }
}