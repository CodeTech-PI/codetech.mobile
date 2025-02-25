package com.example.code_mobile

import com.example.code_mobile.paginas.code_mobile.ui.theme.Tela1
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        composable("PageLogin") { Tela1() }
    }

}
