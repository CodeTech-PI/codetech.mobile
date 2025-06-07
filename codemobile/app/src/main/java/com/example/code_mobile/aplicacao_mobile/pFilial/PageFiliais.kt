package com.example.code_mobile.paginas.code_mobile.pFilial

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.ConfirmDeleteDialog
import com.example.code_mobile.paginas.code_mobile.Input
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente
import com.example.code_mobile.paginas.code_mobile.cardFilial
import com.example.code_mobile.paginas.code_mobile.pComponente.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.cModel.ModelFiliais
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelCliente
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelFilial
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.delay


@Composable
fun TelaFiliais(navController: NavController, modifier: Modifier = Modifier) {

    val viewModel: ViewModelFilial = viewModel()

    var pesquisa by remember { mutableStateOf("") }
    val filiais by viewModel.filiais.collectAsState()
    val isLoading by viewModel.showLoading.collectAsState()
    val erroCarregar by viewModel.mensagemErro.collectAsState()

    var showDialogExcluir by remember { mutableStateOf(false) }
    var filialParaExcluir by remember { mutableStateOf<ModelFiliais?>(null) }
    var exclusaoSucesso by remember { mutableStateOf(false) }
    var mensagemErroExclusao by remember { mutableStateOf<String?>(null) }

    var showEdicaoDialog by remember { mutableStateOf(false) }
    var filialParaEditar by remember { mutableStateOf<ModelFiliais?>(null) }
    var edicaoSucesso by remember { mutableStateOf(false) }
    var mensagemErroEdicao by remember { mutableStateOf<String?>(null) }

    println("Executando tela de filiais")

    LaunchedEffect(Unit) {
        println("TelaFiliais LaunchedEffect")
        viewModel.carregarFiliais()
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        // Esse efeito será chamado toda vez que essa tela for exibida novamente
        delay(2000) // espera 2 segundos
        viewModel.carregarFiliais()
    }

    LaunchedEffect(exclusaoSucesso) {
        if (exclusaoSucesso) {
            println("Filial excluída com sucesso!")
            exclusaoSucesso = false // Resetar o estado
            showDialogExcluir = false // Fechar o diálogo após o sucesso
        }
    }

    LaunchedEffect(mensagemErroExclusao) {
        if (!mensagemErroExclusao.isNullOrEmpty()) {
            println("Erro ao excluir filial: $mensagemErroExclusao")
            // Opcional: Mostrar uma mensagem de erro ao usuário
        }
    }

    LaunchedEffect(edicaoSucesso) {
        if (edicaoSucesso) {
            println("Filial editada com sucesso!")
            edicaoSucesso = false
            showEdicaoDialog = false
            viewModel.carregarFiliais() // Recarregar a lista após a edição
        }
    }

    LaunchedEffect(mensagemErroEdicao) {
        if (!mensagemErroEdicao.isNullOrEmpty()) {
            println("Erro ao editar filial: $mensagemErroEdicao")
            // Opcional: Mostrar uma mensagem de erro ao usuário
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        menuComTituloPage("Filiais", navController)

        // Filtro e ícone de adicionar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Input(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Pesquisar filial") }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar uma filial!")
                        navController.navigate("FiliaisCadastro")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Exibir lista de filiais
        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else if (!erroCarregar.isNullOrEmpty()) {
            Text(
                text = "Erro ao carregar filiais: $erroCarregar",
                color = Color.Red,
                style = textPadrao
            )
        } else {
            // Filtrar filiais por nome, caso haja pesquisa
            val filiaisFiltradas = if (pesquisa.isEmpty()) {
                filiais
            } else {
                filiais.filter {
                    it.logradouro.contains(pesquisa, ignoreCase = true)
                }
            }

            // Exibir filiais
            LazyColumn {
                items(filiaisFiltradas) { filial ->
                    Box(modifier = Modifier.padding(8.dp)) {
                        cardFilial(
                            "Rua: ${filial.logradouro}",
                            "Estado: ${filial.estado}",
                            "Cidade: ${filial.cidade}",
                            "CEP: ${filial.cep}",
                            "Status: ${filial.status}", // Usando o status da filial

                            onDeleteClick = {
                                filialParaExcluir = filial
                                showDialogExcluir = true
                            },
                            OnEditClick = {
                                filialParaEditar = filial
                                showEdicaoDialog = true
                            }
                        )
                    }
                }

                // Se não houver filiais
                if (filiaisFiltradas.isEmpty()) {
                    item {
                        Text("Nenhuma filial encontrada", color = Color.White, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }

        // Diálogo de exclusão
        if (showDialogExcluir && filialParaExcluir != null) {
            ExcluirFilialDialog(

                filial = filialParaExcluir!!,
                onDismiss = { showDialogExcluir = false },
                onConfirmExcluir = { filialExcluir ->
                    viewModel.deletarFilial(
                        filial = filialExcluir,
                        onDeleteSuccess = { exclusaoSucesso = true },
                        onDeleteError = { mensagemErroExclusao = it }
                    )
                }
            )
        }

        // Diálogo de edição
        if (showEdicaoDialog && filialParaEditar != null) {
            EditarFilialDialog(
                onDismiss = { showEdicaoDialog = false },
                onSalvar = { filialAtualizada ->
                    viewModel.atualizarFilial(
                        filial = filialAtualizada,
                        onSucesso = { edicaoSucesso = true },
                        onError = { mensagemErroEdicao = it }
                    )
                },
                filial = filialParaEditar!! // !! pois já verificamos que não é nulo
            )
        }
    }
}

@Composable
fun ExcluirFilialDialog(
    filial: ModelFiliais,
    onDismiss: () -> Unit,
    onConfirmExcluir: (ModelFiliais) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Exclusão", color = Color.White) },
        text = { Text("Deseja mesmo excluir a filial localizada em ${filial.logradouro}?", color = Color.White) },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onConfirmExcluir(filial)
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
fun EditarFilialDialog(
    onDismiss: () -> Unit,
    onSalvar: (ModelFiliais) -> Unit,
    filial: ModelFiliais
) {
    var cepEditado by remember { mutableStateOf(filial.cep) }
    var logradouroEditado by remember { mutableStateOf(filial.logradouro) }
    var bairroEditado by remember { mutableStateOf(filial.bairro) }
    var cidadeEditada by remember { mutableStateOf(filial.cidade) }
    var estadoEditado by remember { mutableStateOf(filial.estado) }
    var numEditado by remember { mutableStateOf(filial.num.toString()) }
    var complementoEditado by remember { mutableStateOf(filial.complemento) }
    var statusEditado by remember { mutableStateOf(filial.status) }

    var erroCep by remember { mutableStateOf<String?>(null) }
    var erroLogradouro by remember { mutableStateOf<String?>(null) }
    var erroBairro by remember { mutableStateOf<String?>(null) }
    var erroCidade by remember { mutableStateOf<String?>(null) }
    var erroEstado by remember { mutableStateOf<String?>(null) }
    var erroNum by remember { mutableStateOf<String?>(null) }
    var erroComplemento by remember { mutableStateOf<String?>(null) }
    var erroStatus by remember { mutableStateOf<String?>(null) }

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
                Text("Editar Filial", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = cepEditado,
                    onValueChange = {
                        cepEditado = it
                        erroCep = null
                    },
                    label = { Text("CEP", color = Color.Gray) },
                    isError = erroCep != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroCep?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = logradouroEditado,
                    onValueChange = {
                        logradouroEditado = it
                        erroLogradouro = null
                    },
                    label = { Text("Logradouro", color = Color.Gray) },
                    isError = erroLogradouro != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroLogradouro?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = bairroEditado,
                    onValueChange = {
                        bairroEditado = it
                        erroBairro = null
                    },
                    label = { Text("Bairro", color = Color.Gray) },
                    isError = erroBairro != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroBairro?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cidadeEditada,
                    onValueChange = {
                        cidadeEditada = it
                        erroCidade = null
                    },
                    label = { Text("Cidade", color = Color.Gray) },
                    isError = erroCidade != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroCidade?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = estadoEditado,
                    onValueChange = {
                        estadoEditado = it
                        erroEstado = null
                    },
                    label = { Text("Estado", color = Color.Gray) },
                    isError = erroEstado != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroEstado?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = numEditado,
                    onValueChange = {
                        numEditado = it
                        erroNum = null
                    },
                    label = { Text("Número", color = Color.Gray) },
                    isError = erroNum != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroNum?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = complementoEditado,
                    onValueChange = {
                        complementoEditado = it
                        erroComplemento = null
                    },
                    label = { Text("Complemento", color = Color.Gray) },
                    isError = erroComplemento != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroComplemento?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = statusEditado,
                    onValueChange = {
                        statusEditado = it
                        erroStatus = null
                    },
                    label = { Text("Status", color = Color.Gray) },
                    isError = erroStatus != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroStatus?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
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
                            if (cepEditado.isBlank()) {
                                erroCep = "O CEP é obrigatório."
                                isValid = false
                            }
                            if (logradouroEditado.isBlank()) {
                                erroLogradouro = "O Logradouro é obrigatório."
                                isValid = false
                            }
                            if (bairroEditado.isBlank()) {
                                erroBairro = "O Bairro é obrigatório."
                                isValid = false
                            }
                            if (cidadeEditada.isBlank()) {
                                erroCidade = "A Cidade é obrigatória."
                                isValid = false
                            }
                            if (estadoEditado.isBlank()) {
                                erroEstado = "O Estado é obrigatório."
                                isValid = false
                            }
                            if (numEditado.isBlank()) {
                                erroNum = "O Número é obrigatório."
                                isValid = false
                            }

                            if (isValid) {
                                val filialAtualizada = filial.copy(
                                    cep = cepEditado,
                                    logradouro = logradouroEditado,
                                    bairro = bairroEditado,
                                    cidade = cidadeEditada,
                                    estado = estadoEditado,
                                    num = numEditado.toIntOrNull() ?: filial.num,
                                    complemento = complementoEditado,
                                    status = statusEditado
                                )
                                onSalvar(filialAtualizada)
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

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)

@Composable
fun PreviewFiliais() {
    CodemobileTheme {
        val navController = rememberNavController()
        val viewModel = ViewModelFilial()
        TelaFiliais(navController)
    }
}
