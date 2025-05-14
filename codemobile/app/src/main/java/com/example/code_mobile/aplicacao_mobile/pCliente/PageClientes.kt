package com.example.code_mobile.paginas.code_mobile.pCliente

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.pComponente.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelCliente
import kotlinx.coroutines.launch

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

    var showEdicaoDialog by remember { mutableStateOf(false) }
    var clienteParaEditar by remember { mutableStateOf<ModelCliente?>(null) }
    var edicaoSucesso by remember { mutableStateOf(false) }
    var mensagemErroEdicao by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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

    LaunchedEffect(edicaoSucesso) {
        if (edicaoSucesso) {
            println("Cliente editado com sucesso!")
            edicaoSucesso = false
            showEdicaoDialog = false
            viewModel.carregarClientes()
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Cliente alterado com sucesso!",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(mensagemErroEdicao) {
        if (!mensagemErroEdicao.isNullOrEmpty()) {
            println("Erro ao editar cliente: $mensagemErroEdicao")
            // Opcional: Mostrar uma mensagem de erro ao usuário
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFF1B1B1B)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                                        onEditClick = { clienteSelecionado ->
                                            clienteParaEditar = clienteSelecionado
                                            showEdicaoDialog = true
                                        },
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

                if (showEdicaoDialog && clienteParaEditar != null) {
                    EditarClienteDialog(
                        cliente = clienteParaEditar!!,
                        onDismiss = { showEdicaoDialog = false },
                        onSalvar = { clienteAtualizado ->
                            viewModel.atualizarCliente(
                                cliente = clienteAtualizado,
                                onSucesso = { edicaoSucesso = true },
                                onError = { mensagemErroEdicao = it }
                            )
                        }
                    )
                }
            }
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
        title = { Text("Confirmar Exclusão", color = Color.White) },
        text = { Text("Deseja mesmo excluir o cliente ${cliente.nome}?", color = Color.White) },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onConfirmExcluir(cliente)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Sim", color = Color.White)
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(Color.Gray),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Não", color = Color.White)
                }
            }
        },
        containerColor = Color(0xFF2B2B2B)
    )
}

@Composable
fun EditarClienteDialog(
    cliente: ModelCliente,
    onDismiss: () -> Unit,
    onSalvar: (ModelCliente) -> Unit
) {
    var nomeEditado by remember { mutableStateOf(cliente.nome) }
    var cpfEditado by remember { mutableStateOf(cliente.cpf) }
    var dataNascimentoEditado by remember { mutableStateOf(cliente.dataNascimento) }
    var telefoneEditado by remember { mutableStateOf(cliente.telefone) }
    var emailEditado by remember { mutableStateOf(cliente.email) }

    var erroNome by remember { mutableStateOf<String?>(null) }
    var erroCpf by remember { mutableStateOf<String?>(null) }
    var erroDataNascimento by remember { mutableStateOf<String?>(null) }
    var erroTelefone by remember { mutableStateOf<String?>(null) }
    var erroEmail by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(Color(0xFF2B2B2B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Editar Cliente", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nomeEditado,
                    onValueChange = {
                        nomeEditado = it
                        erroNome = null
                    },
                    label = { Text("Nome", color = Color.Gray) },
                    isError = erroNome != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroNome?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cpfEditado,
                    onValueChange = {
                        cpfEditado = it
                        erroCpf = null
                    },
                    label = { Text("CPF", color = Color.Gray) },
                    isError = erroCpf != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroCpf?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dataNascimentoEditado,
                    onValueChange = {
                        dataNascimentoEditado = it
                        erroDataNascimento = null
                    },
                    label = { Text("Data de Nascimento", color = Color.Gray) },
                    isError = erroDataNascimento != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroDataNascimento?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = telefoneEditado,
                    onValueChange = {
                        telefoneEditado = it
                        erroTelefone = null
                    },
                    label = { Text("Telefone", color = Color.Gray) },
                    isError = erroTelefone != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroTelefone?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = emailEditado,
                    onValueChange = {
                        emailEditado = it
                        erroEmail = null
                    },
                    label = { Text("Email", color = Color.Gray) },
                    isError = erroEmail != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroEmail?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                    Button(
                        onClick = {
                            var isValid = true
                            if (nomeEditado.isBlank()) {
                                erroNome = "O nome é obrigatório."
                                isValid = false
                            }
                            if (cpfEditado.isBlank()) {
                                erroCpf = "O CPF é obrigatório."
                                isValid = false
                            }
                            if (dataNascimentoEditado.isBlank()) {
                                erroDataNascimento = "A data de nascimento é obrigatória."
                                isValid = false
                            }
                            if (telefoneEditado.isBlank()) {
                                erroTelefone = "O telefone é obrigatório."
                                isValid = false
                            }
                            if (emailEditado.isBlank()) {
                                erroEmail = "O email é obrigatório."
                                isValid = false
                            }

                            if (isValid) {
                                val clienteAtualizado = cliente.copy(
                                    nome = nomeEditado,
                                    cpf = cpfEditado,
                                    dataNascimento = dataNascimentoEditado,
                                    telefone = telefoneEditado,
                                    email = emailEditado
                                )
                                onSalvar(clienteAtualizado)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
                    ) {
                        Text("Salvar", color = Color.White)
                    }
                }
            }
        }
    }
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