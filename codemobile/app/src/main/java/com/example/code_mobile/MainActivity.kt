package com.example.code_mobile

import com.example.code_mobile.paginas.code_mobile.ui.theme.TelaLogin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.DashboardScreen
import com.example.code_mobile.paginas.code_mobile.Menu
import com.example.code_mobile.paginas.code_mobile.TelaCategorias
import com.example.code_mobile.paginas.code_mobile.TelaClientes
import com.example.code_mobile.ui.theme.CodemobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodemobileTheme {

                AppNavigation()
                }
            }
        }
    }


@Composable
fun AppNavigation() {
                        //
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "PageLogin") {
        composable("PageLogin") { TelaLogin(navController) }
        composable("Clientes") { TelaClientes(navController) }
        composable("Categorias") { TelaCategorias(navController) }
        composable("Dashboard") { DashboardScreen(navController) }
        composable("Menu"){ Menu(navController) }

    }

}
