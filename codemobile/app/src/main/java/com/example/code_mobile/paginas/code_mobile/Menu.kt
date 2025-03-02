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
fun Menu(navController: NavController, modifier: Modifier){

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
                    // Adicionar a ação para fechar o menu
                }
            )

        }

        //Lista de itens do menu
        MenuItem(iconResId = R.drawable.icone_perfil, text = "Clientes")
        MenuItem(iconResId = R.drawable.icone_perfil, text = "Estoque")
        MenuItem(iconResId = R.drawable.icone_perfil, text = "Atendimentos")
        MenuItem(iconResId = R.drawable.icone_perfil, text = "Dashboard", selected = true)
        MenuItem(iconResId = R.drawable.icone_perfil, text = "Filiais")
        MenuItem(iconResId = R.drawable.icone_perfil, text = "Sair")
    }
}

@Composable
fun MenuItem(iconResId: Int, text: String, selected: Boolean = false){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 15.dp),
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
        Menu(navController, modifier = Modifier)
    }
}