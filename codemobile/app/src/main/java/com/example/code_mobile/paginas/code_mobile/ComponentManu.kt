package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.code_mobile.R

@Composable
fun manuComTituloPage(nomeTela: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.menu_hanburguer),
            contentDescription = "Menu lateral",
            modifier = Modifier
                .size(25.dp)
                .clickable {
                    println("Clicou para abrir menu!")
                }
        )
        Text(
            text = nomeTela,
            style = textPadrao,
            modifier = Modifier
                .weight(1f) // Ocupa o espaço que sobrar da Row
                .padding(10.dp),
            textAlign = TextAlign.Center
        )
    }
}