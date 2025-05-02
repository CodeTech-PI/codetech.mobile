package com.example.code_mobile.paginas.code_mobile.pComponente

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun menuComTituloPage(nomeTela: String, navController: NavController) {

    Spacer(modifier = Modifier.height(90.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Image(
            painter = painterResource(id = R.drawable.menu_hanburguer),
            contentDescription = "Menu lateral",
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    navController.navigate("Menu")
                }
        )
        Text(
            text = nomeTela,
            style = textPadrao.copy(fontSize = 18.sp), // Reduzindo um pouco o tamanho da fonte do título
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp), // Reduzindo um pouco o padding horizontal do texto
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(20.dp)) // Reduzindo a largura do Spacer para corresponder ao ícone
    }
}