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
import com.example.code_mobile.paginas.code_mobile.cliente.ClienteCadastro
import com.example.code_mobile.paginas.code_mobile.ClienteEditar
import com.example.code_mobile.paginas.code_mobile.DashboardScreen
import com.example.code_mobile.paginas.code_mobile.Filiais
import com.example.code_mobile.paginas.code_mobile.FiliaisCadastro
import com.example.code_mobile.paginas.code_mobile.FiliaisEditar
import com.example.code_mobile.paginas.code_mobile.Menu
import com.example.code_mobile.paginas.code_mobile.TelaCategorias
import com.example.code_mobile.paginas.code_mobile.cliente.TelaClientes
import com.example.code_mobile.paginas.code_mobile.estoque.TelaEstoque

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
        composable("Estoque") { TelaEstoque(navController) }
        composable("Categoria") { TelaCategorias(navController) }
        composable("Menu"){ Menu(navController) }
        composable("Filiais"){ Filiais(navController) }
        composable("FiliaisCadastro"){ FiliaisCadastro(navController) }
        composable("FiliaisEditar"){ FiliaisEditar(navController) }
        composable("Dashboard") { DashboardScreen(navController) }
        composable("ClienteCadastro") { ClienteCadastro(navController) }
        composable("ClienteEditar") { ClienteEditar(navController) }

    }

}
