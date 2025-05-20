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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelCategoria
import com.example.code_mobile.paginas.code_mobile.pComponente.menuComTituloPage
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.delay

@Composable
fun TelaCategorias(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModelCategoria: ViewModelCategoria = viewModel() // Use viewModel() diretamente aqui
) {
    val categorias by viewModelCategoria.listaCategorias.collectAsState()
    val cadastroSucesso by viewModelCategoria.cadastroSucesso.collectAsState()
    val erro by viewModelCategoria.mensagemErro.collectAsState()
    val isLoading by viewModelCategoria.showLoading.collectAsState()
    val exclusaoSucesso by viewModelCategoria.exclusaoSucesso.collectAsState()
    val mensagemErroExclusao by viewModelCategoria.mensagemErroExclusao.collectAsState()

    var pesquisa by remember { mutableStateOf("") }
    var showCadastroDialog by remember { mutableStateOf(false) }
    var showEdicaoDialog by remember { mutableStateOf(false) }
    var categoriaEditado by remember { mutableStateOf<ModelCategoria?>(null) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var categoriaParaExcluir by remember { mutableStateOf<ModelCategoria?>(null) }
    var showCadastroSucessoDialog by remember { mutableStateOf(false) } // Novo estado

    // Carregar categorias assim que abrir
    LaunchedEffect(Unit) {
        viewModelCategoria.carregarCategorias()
    }

    LaunchedEffect(exclusaoSucesso) {
        if (exclusaoSucesso) {
            println("Categoria excluída com sucesso!")
            viewModelCategoria.resetExclusaoSucesso() // Resetar o estado no ViewModel
            showConfirmDeleteDialog = false // Fechar o diálogo após o sucesso
        }
    }

    LaunchedEffect(mensagemErroExclusao) {
        if (!mensagemErroExclusao.isNullOrEmpty()) {
            println("Erro ao excluir categoria: $mensagemErroExclusao")
            // Opcional: Mostrar uma mensagem de erro ao usuário
        }
    }

    LaunchedEffect(cadastroSucesso) {
        if (cadastroSucesso) {
            showCadastroSucessoDialog = true
            showCadastroDialog = false // Fechar o diálogo de inscrição
            delay(3000) // Opcional: manter o diálogo de sucesso por alguns segundos
            showCadastroSucessoDialog = false
            viewModelCategoria.resetCadastroSucesso() // Importante resetar o estado no ViewModel
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        menuComTituloPage("Categoria", navController)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Input(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por categoria") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        println("Clicou para cadastrar uma categoria!")
                        showCadastroDialog = true
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            categorias
                .filter { it.nome.contains(pesquisa, ignoreCase = true) }
                .forEach { categoria ->
                    cardCategoria(
                        categoria.nome,
                        onEditClick = {
                            categoriaEditado = categoria
                            showEdicaoDialog = true
                        },
                        onDeleteClick = {
                            categoriaParaExcluir = categoria
                            showConfirmDeleteDialog = true
                        }
                    )
                    Spacer(modifier = modifier.height(10.dp))
                }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    if (showCadastroDialog) {
        NovaCategoriaDialog(
            viewModelCategoria = viewModelCategoria,
            onDismiss = { showCadastroDialog = false }
        )
    }

    if (showConfirmDeleteDialog && categoriaParaExcluir != null) {
        ExcluirCategoriaDialog( // Usando o diálogo de exclusão adaptado
            categoria = categoriaParaExcluir!!,
            onDismiss = { showConfirmDeleteDialog = false },
            onConfirmExcluir = { categoriaExcluir ->
                viewModelCategoria.deletarCategoria(
                    categoriaId = categoriaExcluir.id!!,
                    onExclusaoSucesso = { viewModelCategoria.resetExclusaoSucesso() },
                    onExclusaoErro = { viewModelCategoria.setMensagemErroExclusao(it) }
                )
            }
        )
    }

    if (showEdicaoDialog && categoriaEditado != null) {
        EditarCategoriaDialog(
            categoria = categoriaEditado!!,
            onDismiss = { showEdicaoDialog = false },
            onSalvar = { categoriaAtualizada ->
                viewModelCategoria.atualizarCategoria(categoriaAtualizada) {
                    showEdicaoDialog = false
                }
            }
        )
    }

    if (showCadastroSucessoDialog) {
        Dialog(onDismissRequest = { /* Não permitir fechar tocando fora */ }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2B2B2B))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Sucesso",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF4CAF50)),
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        "Cadastrado!",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "Redirecionando...",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ExcluirCategoriaDialog( // Diálogo de exclusão reutilizado e adaptado
    categoria: ModelCategoria,
    onDismiss: () -> Unit,
    onConfirmExcluir: (ModelCategoria) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Exclusão", color = Color.White) },
        text = { Text("Deseja mesmo excluir a categoria ${categoria.nome}?", color = Color.White) },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onConfirmExcluir(categoria)
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
fun NovaCategoriaDialog(
    viewModelCategoria: ViewModelCategoria,
    onDismiss: () -> Unit
) {
    val nome by viewModelCategoria.nome
    val nomeError by viewModelCategoria.nomeError

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(Color(0xFF121212))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Cadastrar", color = Color.White, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nome,
                    onValueChange = viewModelCategoria::atualizarNome,
                    label = { Text("Categoria") },
                    isError = nomeError != null,
                    textStyle = TextStyle(color = Color.White)
                )
                nomeError?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { viewModelCategoria.cadastrarCategoria() },
                        colors = ButtonDefaults.buttonColors(Color(0xFFE91E63))
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}


@Composable
fun EditarCategoriaDialog(
    categoria: ModelCategoria,
    onDismiss: () -> Unit,
    onSalvar: (ModelCategoria) -> Unit
) {
    var nomeEditado by remember { mutableStateOf(categoria.nome) }
    var erroNome by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(Color(0xFF121212))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Editar Categoria", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nomeEditado,
                    onValueChange = {
                        nomeEditado = it
                        erroNome = null
                    },
                    label = { Text("Categoria") },
                    isError = erroNome != null,
                    textStyle = TextStyle(color = Color.White)
                )
                erroNome?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            if (nomeEditado.isBlank()) {
                                erroNome = "O nome é obrigatório."
                            } else {
                                onSalvar(categoria.copy(nome = nomeEditado))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFE91E63))
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun TelaCategoriasPreviewSemDados() {
    CodemobileTheme {
        val navController = rememberNavController()
        val viewModel = viewModel<ViewModelCategoria>()
        TelaCategorias(navController = navController, viewModelCategoria = viewModel)
    }
}