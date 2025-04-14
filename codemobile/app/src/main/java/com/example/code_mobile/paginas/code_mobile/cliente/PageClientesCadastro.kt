package com.example.code_mobile.paginas.code_mobile.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.code_mobile.paginas.code_mobile.cliente.CampoCadastrarCliente
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun ClienteCadastro(navController: NavController, modifier: Modifier = Modifier) {
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var dataNasc by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val scrollState = rememberScrollState() // Cria um estado de scroll

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .fillMaxHeight(0.6f)
            .verticalScroll(scrollState)
            .background(Color(0xFF1B1B1B))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround // Alinha os itens com espaço igual antes, entre e depois deles
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp), // Aumentei o padding bottom para descer mais
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Cadastrar",
                style = textPadrao.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(30.dp))
        }

        CampoCadastrarCliente(
            titulo = "Nome:",
            valor = nome,
            onValorChange = { nome = it },
            textStyle = textPadrao
        )

        CampoCadastrarCliente(
            titulo = "CPF:",
            valor = cpf,
            onValorChange = { cpf = it },
            textStyle = textPadrao
        )

        CampoCadastrarCliente(
            titulo = "Data de nascimento:",
            valor = dataNasc,
            onValorChange = { dataNasc = it },
            textStyle = textPadrao
        )

        CampoCadastrarCliente(
            titulo = "Telefone:",
            valor = telefone,
            onValorChange = { telefone = it },
            textStyle = textPadrao
        )

        CampoCadastrarCliente(
            titulo = "E-mail:",
            valor = email,
            onValorChange = { email = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp)) // Reduzi o espaço antes dos botões

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("ClienteCadastro") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp), // Deixando os botões menos redondos
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text(text = "Salvar")
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp), // Deixando os botões menos redondos
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
fun ClienteCadastroPreview() {
    CodemobileTheme {
        val navController = rememberNavController()
        ClienteCadastro(navController)
    }
}