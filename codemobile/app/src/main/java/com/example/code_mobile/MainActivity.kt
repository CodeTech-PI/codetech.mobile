package com.example.code_mobile

import com.example.code_mobile.paginas.code_mobile.ui.theme.TelaLogin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.pCliente.ClienteCadastro
import com.example.code_mobile.paginas.code_mobile.pDashboard.DashboardScreen
import com.example.code_mobile.paginas.code_mobile.pFilial.Filiais
import com.example.code_mobile.paginas.code_mobile.pFilial.FiliaisCadastro
import com.example.code_mobile.paginas.code_mobile.pFilial.FiliaisEditar
import com.example.code_mobile.paginas.code_mobile.pComponente.Menu
import com.example.code_mobile.paginas.code_mobile.pCategoria.TelaCategorias
import com.example.code_mobile.paginas.code_mobile.pCliente.TelaClientes
import com.example.code_mobile.paginas.code_mobile.pEstoque.EstoqueCadastro
import com.example.code_mobile.paginas.code_mobile.pEstoque.TelaEstoque
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelCategoria
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelFilial

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
        composable("Categoria") { val viewModelCategoria: ViewModelCategoria = viewModel()
            TelaCategorias(navController, viewModelCategoria = viewModelCategoria)
        }
        composable("Menu"){ Menu(navController) }
        composable("Filiais") {
            val viewModelFilial: ViewModelFilial = viewModel()
            Filiais(navController, viewModel = viewModelFilial)
        }
        composable("FiliaisCadastro"){ FiliaisCadastro(navController) }
        composable("FiliaisEditar"){ FiliaisEditar(navController) }
        composable("Dashboard") { DashboardScreen(navController) }
        composable("ClienteCadastro") { ClienteCadastro(navController) }
        composable("EstoqueCadastro") { EstoqueCadastro(navController) }

    }

}
