package com.example.code_mobile

import android.content.res.Resources.Theme
import com.example.code_mobile.paginas.code_mobile.ui.theme.TelaLogin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.code_mobile.paginas.code_mobile.atendimento.PageAtendimento
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                AppNavigation()

        }
    }
}


@Composable
fun AppNavigation() {
    //
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "PageLogin") {
        composable("PageLogin") { TelaLogin(navController) }
        //composable("Clientes") { TelaClientes(navController) }
        composable("Atendimento") {
            PageAtendimento(navController = navController)
        }
    }

}


@Preview(showBackground = true, name = "PageAtendimento Preview")
@Composable
fun PageAtendimentoPreview() {
    val navController = rememberNavController()
    PageAtendimento(navController = navController)
}
