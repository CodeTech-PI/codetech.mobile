package com.example.code_mobile.aplicacao_mobile.pAgendamento

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.pComponente.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun TelaAgendamento(navController: NavController, modifier: Modifier = Modifier) {

    // Não tem front do card, precisa fazer. Tem o protótipo lá no figma
    // O get não ta vinculado no back para listar os agendamentos
    // O botão de adicionar ta vinculado no back

    var pesquisa by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        menuComTituloPage("Agendamentos", navController)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InputPesquisarAgendamento(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por CPF") }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar um agendamento!")
                        navController.navigate("AgendamentoCadastro")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_2
)

@Composable
fun GreetingPreviewAgendamentos() {
    androidx.compose.material3.MaterialTheme {
        val navController = androidx.navigation.compose.rememberNavController()
        TelaAgendamento(navController)
    }
}