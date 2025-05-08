package com.example.code_mobile

import com.example.code_mobile.paginas.code_mobile.ui.theme.TelaLogin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.code_mobile.aplicacao_mobile.pAgendamento.TelaAgendamento
import com.example.code_mobile.aplicacao_mobile.pAgendamento.AgendamentoEtapa1
import com.example.code_mobile.aplicacao_mobile.pAgendamento.AgendamentoEtapa2
import com.example.code_mobile.paginas.code_mobile.pCliente.ClienteCadastro
import com.example.code_mobile.paginas.code_mobile.pDashboard.DashboardScreen
import com.example.code_mobile.paginas.code_mobile.pFilial.FiliaisCadastro
import com.example.code_mobile.paginas.code_mobile.pComponente.Menu
import com.example.code_mobile.paginas.code_mobile.pCategoria.TelaCategorias
import com.example.code_mobile.paginas.code_mobile.pCliente.TelaClientes
import com.example.code_mobile.paginas.code_mobile.pEstoque.EstoqueCadastro
import com.example.code_mobile.paginas.code_mobile.pEstoque.TelaEstoque
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelCategoria
import com.example.code_mobile.paginas.code_mobile.pFilial.TelaFiliais

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
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "PageLogin") {

        composable("PageLogin") { TelaLogin(navController) }
        composable("Estoque") { TelaEstoque(navController) }
        composable("Clientes") { TelaClientes(navController) }
        composable("Agendamentos") { TelaAgendamento(navController) }
        composable("Filiais") { TelaFiliais(navController) }
        composable("Menu") { Menu(navController) }
        composable("Dashboard") { DashboardScreen(navController) }
        composable("Categoria") {
            val viewModelCategoria: ViewModelCategoria = viewModel()
            TelaCategorias(navController, viewModelCategoria = viewModelCategoria)
        }
        composable("ClienteCadastro") { ClienteCadastro(navController) }
        composable("AgendamentoCadastro") { AgendamentoEtapa1(navController) }
        composable(
            "AgendamentoCadastro2/{agendamentoId}",
            arguments = listOf(navArgument("agendamentoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val agendamentoId = backStackEntry.arguments?.getInt("agendamentoId") ?: -1
            AgendamentoEtapa2(navController = navController, agendamentoId = agendamentoId)
        }
        composable("EstoqueCadastro") { EstoqueCadastro(navController) }
        composable("FiliaisCadastro") { FiliaisCadastro(navController) }

    }

}
