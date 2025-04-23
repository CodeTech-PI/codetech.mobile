package com.example.code_mobile.paginas.code_mobile.cliente

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import com.example.code_mobile.paginas.code_mobile.viewModel.cliente.ViewModelCliente
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun TelaClientes(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: ViewModelCliente = viewModel()

    var pesquisa by remember { mutableStateOf("") }
    val clientes by viewModel.clientes.collectAsState()
    val isLoading by viewModel.isLoadingClientes.collectAsState()
    val erroCarregar by viewModel.erroCarregarClientes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var clienteParaExcluir by remember { mutableStateOf<ModelCliente?>(null) }
    var exclusaoSucesso by remember { mutableStateOf(false) }
    var mensagemErroExclusao by remember { mutableStateOf<String?>(null) }

    println("Executando tela de clientes")

    LaunchedEffect(true) {
        println("TelaClientes LaunchedEffect")
        viewModel.carregarClientes()
    }

    LaunchedEffect(exclusaoSucesso) {
        if (exclusaoSucesso) {
            println("Cliente excluído com sucesso!")
            exclusaoSucesso = false // Resetar o estado
            showDialog = false // Fechar o diálogo após o sucesso
        }
    }

    LaunchedEffect(mensagemErroExclusao) {
        if (!mensagemErroExclusao.isNullOrEmpty()) {
            println("Erro ao excluir cliente: $mensagemErroExclusao")
            // Opcional: Mostrar uma mensagem de erro ao usuário
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

        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else if (!erroCarregar.isNullOrEmpty()) {
            Text(
                text = "Erro ao carregar clientes: $erroCarregar",
                color = Color.Red,
                style = textPadrao
            )
        } else {
            LazyColumn {
                item {
                    val clientesFiltrados = if (pesquisa.isBlank()) {
                        clientes
                    } else {
                        clientes.filter { it.cpf.contains(pesquisa) }
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
                                onDeleteClick = { cliente ->
                                    clienteParaExcluir = cliente
                                    showDialog = true
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

        if (showDialog && clienteParaExcluir != null) {
            ExcluirClienteDialog(
                cliente = clienteParaExcluir!!,
                onDismiss = { showDialog = false },
                onConfirmExcluir = { clienteExcluir ->
                    viewModel.excluirCliente(
                        cliente = clienteExcluir,
                        onExclusaoSucesso = { exclusaoSucesso = true },
                        onExclusaoErro = { mensagemErroExclusao = it }
                    )
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
        dismissButton = {}
    )
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    showSystemUi = true,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_2
)
@Composable
fun GreetingPreviewClientes() {
    androidx.compose.material3.MaterialTheme {
        val navController = androidx.navigation.compose.rememberNavController()
        TelaClientes(navController)
    }
}