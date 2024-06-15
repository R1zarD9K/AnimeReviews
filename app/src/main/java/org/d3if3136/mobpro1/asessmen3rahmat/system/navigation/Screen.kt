package org.d3if3136.mobpro1.asessmen3rahmat.system.navigation

sealed class Screen(val route: String) {
    data object Base : Screen("home")
}