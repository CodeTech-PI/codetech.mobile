package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.componente.CampoFilial
import com.example.code_mobile.paginas.code_mobile.componente.CampoFilialStatus
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun FiliaisCadastro(navController: NavController, modifier: Modifier = Modifier) {
    var status by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var logradouro by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }

    val scrollState = rememberScrollState() // Cria um estado de scroll

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    Column(modifier = modifier
        .fillMaxSize() // faz a mudança pegar na tela toda
        .verticalScroll(scrollState)
        .background(Color(0xFF1B1B1B))
        .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally // Centraliza horizontalmente

    ) {
        Spacer(modifier = Modifier.height(20.dp)) // Remova o Spacer.weight() inicial


        Text(
            text = "Cadastrar",
            style = textPadrao.copy(
                fontSize = 30.sp, // unidade sp, somente para tamanho de texto
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(20.dp)) // espaço entre os campos

        CampoFilialStatus(
            titulo = "Status:",
            valor = status,
            onValorChange = { status = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp)) // espaço entre os campos

        CampoFilial (
            titulo = "CEP:",
            valor = cep,
            onValorChange = { cep = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp)) // espaço entre os campos

        CampoFilial (
            titulo = "Logradouro:",
            valor = logradouro,
            onValorChange = { logradouro = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp)) // espaço entre os campos

        CampoFilial (
            titulo = "Bairro:",
            valor = bairro,
            onValorChange = { bairro = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp)) // espaço entre os campos

        CampoFilial (
            titulo = "Cidade:",
            valor = cidade,
            onValorChange = { cidade = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp)) // espaço entre os campos

        CampoFilial (
            titulo = "Estado:",
            valor = estado,
            onValorChange = { estado = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp)) // espaço entre os campos

        CampoFilial (
            titulo = "Número:",
            valor = numero,
            onValorChange = { numero = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(40.dp)) // Aumente o espaço antes dos botões




        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), // Adiciona padding horizontal ao Row
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("Filiais") },
                modifier = Modifier
                    .width(130.dp)
                    .padding(horizontal = 8.dp), // Adiciona padding horizontal ao botão
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text(text = "Salvar")
            }

            Button(
                onClick = { navController.navigate("Filiais") },
                modifier = Modifier
                    .width(130.dp)
                    .padding(horizontal = 8.dp), // Adiciona padding horizontal ao botão
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF252525))
            ) {
                Text(text = "Cancelar")
            }
        }


    }

}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun GreetingPreview() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        FiliaisCadastro(navController)  // Passe o navController para TelaLogin
    }
}
