package com.example.code_mobile.paginas.code_mobile.cliente

import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import com.example.code_mobile.paginas.code_mobile.service.ServiceCliente
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.token.network.RetrofithAuth
import com.example.code_mobile.token.network.TokenManager
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TelaClientes(navController: NavController, modifier: Modifier = Modifier) {
    val serviceCliente = RetrofithAuth.retrofit.create(ServiceCliente::class.java)


    var pesquisa by remember { mutableStateOf("") }
    var clientes by remember { mutableStateOf<List<ModelCliente>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var clienteParaExcluir by remember { mutableStateOf<ModelCliente?>(null) } // Para armazenar o cliente a ser excluído

    println("Executando tela de clientes")

    LaunchedEffect(true) {

        println("TelaClientes LaunchedEffect, token atual: ${TokenManager.token}")

        try {
            println("Entrou no try")

            val response = serviceCliente.getUsuarios()

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
            println("Entrou no catch")
            println("Erro na requisição: ${e.message}")
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        menuComTituloPage("Clientes", navController)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InputPesquisarCliente(
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
            item {
                val clientesFiltrados =
                    if (pesquisa.isBlank()) { // Vai trazer tudo do banco
                        clientes
                    } else {
                        clientes.filter { // Vai trazer so oq ela pesquisou
                            it.cpf.contains(pesquisa)
                        }
                    }

                for (cliente in clientesFiltrados) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = cliente.nome,
                            style = textPadrao.copy(fontSize = 16.sp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 10.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        cardCliente(
                            cliente = cliente,
                            coluna1Info1 = "CPF: ${cliente.cpf}",
                            coluna1Info2 = "Nascimento: ${cliente.dataNascimento}",
                            coluna2Info1 = "Telefone: ${cliente.telefone}",
                            coluna2Info2 = "Email: ${cliente.email}",
                            onEditClick = {},
                            onDeleteClick = { cliente -> // Atualiza o estado para mostrar o diálogo
                                clienteParaExcluir = cliente
                                showDialog = true // Vai exibir o pop up p excluir
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

            }

        }

        if (showDialog && clienteParaExcluir != null) {

            ExcluirClienteDialog(
                cliente = clienteParaExcluir!!,
                onDismiss = { showDialog = false },
                onConfirmExcluir = { clienteExcluir ->
                    println("Confirmou a exclusão do cliente: ${clienteExcluir.nome} - ID: ${clienteExcluir.id}")

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = serviceCliente.deletarUsuario(clienteExcluir.id)
                            if (response.isSuccessful) {
                                println("Cliente com ID ${clienteExcluir.id} excluído com sucesso.")

                                val responseAtualizado = serviceCliente.getUsuarios()
                                if (responseAtualizado.isSuccessful) {
                                    withContext(Dispatchers.Main) {
                                        clientes = responseAtualizado.body() ?: emptyList()
                                    }
                                } else {
                                    println(
                                        "Erro ao recarregar clientes após exclusão: ${responseAtualizado.code()} - ${
                                            responseAtualizado.errorBody()?.string()
                                        }"
                                    )
                                }
                            } else {
                                val errorBodyExclusao = response.errorBody()?.string()
                                val errorCodeExclusao = response.code()
                                println("Erro ao excluir cliente ${clienteExcluir.id} (Código: $errorCodeExclusao): $errorBodyExclusao")
                                val mensagemErro = when (errorCodeExclusao) {
                                    404 -> "Cliente não encontrado."
                                    else -> "Erro ao excluir o cliente. Tente novamente."
                                }
                                println("Mensagem de erro para o usuário: $mensagemErro")
                            }
                        } catch (e: Exception) {
                            println("Erro na requisição de exclusão: ${e.message}")
                            e.printStackTrace()
                            val mensagemErroConexao =
                                "Erro de conexão ou problema na requisição. Verifique sua internet e tente novamente."
                            println("Mensagem de erro para o usuário: $mensagemErroConexao")
                        } finally {
                            withContext(Dispatchers.Main) {
                                clienteParaExcluir = null
                                showDialog = false
                            }
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun ExcluirClienteDialog(
    cliente: ModelCliente,
    onDismiss: () -> Unit,
    onConfirmExcluir: (ModelCliente) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF252525),
        shape = RoundedCornerShape(1.dp),
        modifier = Modifier
            .border(0.5.dp, Color(0xFFDF0050), RoundedCornerShape(2.dp))
            .clip(RoundedCornerShape(2.dp)),
        title = {
            Text(
                "Excluir Cliente?",
                style = textPadrao,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                "Nome: \n${cliente.nome}\nCPF: ${cliente.cpf}",
                style = textPadrao.copy(fontSize = 18.sp, color = Color.White),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onConfirmExcluir(cliente); onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDF0050),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Excluir")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF555555),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
            }
        },
        dismissButton = { }
    )
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