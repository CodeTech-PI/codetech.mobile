package com.example.code_mobile.paginas.code_mobile

import android.view.Menu
import android.view.MenuItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun Menu(navController: NavController){

    var selectedItem by remember { mutableStateOf("Dashboard") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalArrangement = Arrangement.End
        ){
            Text(
                text = "X",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                   navController.popBackStack()
                }
            )

        }

        //Lista de itens do menu
        MenuItem(iconResId = R.drawable.clientes_branco, text = "Clientes", selected = selectedItem == "Clientes", onClick = {
            selectedItem = "Clientes"
            navController.navigate("Clientes")
        })
        MenuItem(iconResId = R.drawable.estoque_branco, text = "Estoque", selected = selectedItem == "Estoque", onClick = {
            selectedItem = "Estoque"
            navController.navigate("Estoque")
        })
        MenuItem(iconResId = R.drawable.estoque_branco, text = "Categoria", selected = selectedItem == "Categoria", onClick = {
            selectedItem = "Categoria"
            navController.navigate("Categoria")
        })
        MenuItem(iconResId = R.drawable.atendimentos_branco, text = "Atendimentos", selected = selectedItem == "Atendimentos", onClick = {
            selectedItem = "Atendimentos"
            navController.navigate("Atendimentos")
        })
        MenuItem(iconResId = R.drawable.dash_branco, text = "Dashboard", selected = selectedItem == "Dashboard", onClick = {
            selectedItem = "Dashboard"
            navController.navigate("Dashboard")
        })
        MenuItem(iconResId = R.drawable.filiais_branco, text = "Filiais", selected = selectedItem == "Filiais", onClick = {
            selectedItem = "Filiais"
            navController.navigate("Filiais")
        })
        MenuItem(iconResId = R.drawable.logout_branco, text = "Sair", selected = selectedItem == "Sair", onClick = {
            selectedItem = "Sair"
            navController.navigate("PageLogin"){
                popUpTo(navController.graph.startDestinationId) { inclusive = true } // Remove todas as telas do histórico
            }
        })
    }
}

@Composable
fun MenuItem(iconResId: Int, text: String, selected: Boolean = false, onClick: () -> Unit){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 15.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = text,
            modifier = Modifier.size(30.dp),
            alpha = if (selected) 1f else 0.7f
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = text,
            color = if(selected) Color(0xFFE91E63) else Color.White,
            fontSize = 18.sp,
            fontWeight = if(selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun GreetingPreviewMenu(){
    CodemobileTheme {
        val navController = rememberNavController()
        Menu(navController)
    }
}