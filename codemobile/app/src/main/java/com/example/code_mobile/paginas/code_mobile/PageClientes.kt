package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun TelaClientes(navController: NavController, modifier: Modifier = Modifier) {


    var pesquisa by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        menuComTituloPage("Clientes")

        // Filtro e icone de adicionar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Input(
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
                    .size(80.dp)
                    .padding(top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar um cliente!")
                        navController.navigate("Categorias")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box (
        ) {
            Text(
                text = "Samarah Costa",
                style = textPadrao.copy(fontSize = 16.sp),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.TopCenter)  // Alinha no topo, mas centraliza horizontalmente
                    .padding(top = 10.dp)  // Adicionando um pouco de espaço do topo
            )

            card4Informacoes(
                R.drawable.icone_perfil,
                "perfil",
                "123.456.789-00",
                "23/01/2004",
                "(11) 95858-5792",
                "samarah@codetech"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box (
        ) {
            Text(
                text = "Caio Araruna",
                style = textPadrao.copy(fontSize = 16.sp),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.TopCenter)  // Alinha no topo, mas centraliza horizontalmente
                    .padding(top = 10.dp)  // Adicionando um pouco de espaço do topo
            )

            card4Informacoes(
                R.drawable.icone_perfil,
                "perfil",
                "123.456.789-00",
                "23/01/2004",
                "(11) 95858-5792",
                "caio.araruna@codetech"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box (
        ) {
            Text(
                text = "Caio Araruna",
                style = textPadrao.copy(fontSize = 16.sp),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.TopCenter)  // Alinha no topo, mas centraliza horizontalmente
                    .padding(top = 10.dp)  // Adicionando um pouco de espaço do topo
            )

            card4Informacoes(
                R.drawable.icone_perfil,
                "perfil",
                "123.456.789-00",
                "23/01/2004",
                "(11) 95858-5792",
                "caio.araruna@codetech"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box (
        ) {
            Text(
                text = "Hosana Flores",
                style = textPadrao.copy(fontSize = 16.sp),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.TopCenter)  // Alinha no topo, mas centraliza horizontalmente
                    .padding(top = 10.dp)  // Adicionando um pouco de espaço do topo
            )

            card4Informacoes(
                R.drawable.icone_perfil,
                "perfil",
                "123.456.789-00",
                "23/01/2004",
                "(11) 95858-5792",
                "hosana@codetech"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun GreetingPreviewClientes() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        TelaClientes(navController)  // Passe o navController para TelaLogin
    }
}