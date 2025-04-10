package com.example.code_mobile.paginas.code_mobile.cliente

import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import com.example.code_mobile.paginas.code_mobile.service.ServiceCliente

import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.code_mobile.paginas.code_mobile.componente.Input
import com.example.code_mobile.paginas.code_mobile.componente.card4Informacoes
import com.example.code_mobile.paginas.code_mobile.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.componente.textPadrao
import com.example.code_mobile.token.network.RetrofithAuth
import com.example.code_mobile.token.network.TokenManager
import com.example.code_mobile.ui.theme.CodemobileTheme


@Composable
fun TelaClientes(navController: NavController, modifier: Modifier = Modifier) {
    var clientes by remember { mutableStateOf<List<ModelCliente>>(emptyList()) }
    println("Executando tela de clientes")

    LaunchedEffect(true) {
        println("TelaClientes: LaunchedEffect executado. Token atual: ${TokenManager.token}")
        try {
            val serviceCliente = RetrofithAuth.retrofit.create(ServiceCliente::class.java)
            val response = serviceCliente.getUsuarios()

            println("Estou dentro do try, token atual: ${TokenManager.token}")
            println(response.body())

            if (response.isSuccessful) {
                clientes = response.body() ?: emptyList()
            } else {
                println(
                    "Erro ao carregar clientes: ${response.code()} - ${
                        response.errorBody()?.string()
                    }"
                )
            }
        } catch (e: Exception) {
            println("Erro na requisição: ${e.message}")
            println("Estou no catch. Token: ${TokenManager.token}")
            e.printStackTrace()
        }
    }


    var pesquisa by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        menuComTituloPage("Clientes", navController)

        // Filtro e botão de adicionar
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
                    .size(60.dp)
                    .padding(top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar um cliente!")
                        navController.navigate("ClienteCadastro")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(clientes.filter {
                it.cpf.contains(pesquisa, ignoreCase = true)
            }) { cliente ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = cliente.nome,
                        style = textPadrao.copy(fontSize = 16.sp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )

                    card4Informacoes(
                        coluna1Info1 = "CPF: ${cliente.cpf}",
                        coluna1Info2 = "Nascimento: ${cliente.dataNascimento}",
                        coluna2Info1 = "Telefone: ${cliente.telefone}",
                        coluna2Info2 = "Email: ${cliente.email}",
                        onEditClick = {
                            println("Editar cliente: ${cliente.nome}")
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
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
fun GreetingPreviewClientes() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        TelaClientes(navController)  // Passe o navController para TelaLogin
    }
}