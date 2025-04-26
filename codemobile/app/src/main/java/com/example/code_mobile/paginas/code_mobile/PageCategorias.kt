package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.estoque.CampoTexto
import com.example.code_mobile.paginas.code_mobile.model.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.viewmodel.categoria.ViewModelCategoria
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun TelaCategorias(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModelCategoria: ViewModelCategoria
) {
    val categorias by viewModelCategoria.listaCategorias.collectAsState()
    val cadastroSucesso by viewModelCategoria.cadastroSucesso.collectAsState()
    val erro by viewModelCategoria.mensagemErro.collectAsState()
    val isLoading by viewModelCategoria.showLoading.collectAsState()

    var pesquisa by remember { mutableStateOf("") }
    var showCadastroDialog by remember { mutableStateOf(false) }
    var showEdicaoDialog by remember { mutableStateOf(false) }
    var categoriaEditado by remember { mutableStateOf<ModelCategoria?>(null) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var categoriaParaExcluir by remember { mutableStateOf<ModelCategoria?>(null) }

    // Carregar categorias assim que abrir
    LaunchedEffect(Unit) {
        viewModelCategoria.carregarCategorias()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        menuComTituloPage("Categoria", navController)

        Input(
            titulo = "",
            valor = pesquisa,
            onValorChange = { pesquisa = it },
            textStyle = textPadrao,
            labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por categoria") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

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

        Button(
            onClick = { showCadastroDialog = true },
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
        ) {
            Text("Nova Categoria", color = Color.White)
        }
    }

    if (showCadastroDialog) {
        NovaCategoriaDialog(
            viewModelCategoria = viewModelCategoria,
            onDismiss = { showCadastroDialog = false }
        )
    }

    if (showConfirmDeleteDialog && categoriaParaExcluir != null) {
        ConfirmDeleteDialogCategoria(
            onConfirm = {
                categoriaParaExcluir?.id?.let { viewModelCategoria.deletarCategoria(it) }
                showConfirmDeleteDialog = false
            },
            onCancel = {
                showConfirmDeleteDialog = false
            }
        )
    }

    if (cadastroSucesso) {
        viewModelCategoria.resetCadastroSucesso()
        showCadastroDialog = false
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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
