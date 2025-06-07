package com.example.code_mobile.paginas.code_mobile.pEstoque

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.code_mobile.ui.theme.CodemobileTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.inputPadrao
import com.example.code_mobile.paginas.code_mobile.pComponente.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.cModel.ModelEstoque
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelEstoque

@Composable
fun TelaEstoque(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: ViewModelEstoque = viewModel()

    var pesquisa by remember { mutableStateOf("") }
    val itensEstoque by viewModel.estoque.collectAsState()
    val isLoading by viewModel.isLoadingEstoque.collectAsState()
    val erroCarregar by viewModel.erroCarregarEstoque.collectAsState()

    var showDialogExcluir by remember { mutableStateOf(false) }
    var itemParaExcluir by remember { mutableStateOf<ModelEstoque?>(null) }
    var exclusaoSucesso by remember { mutableStateOf(false) }
    var mensagemErroExclusao by remember { mutableStateOf<String?>(null) }

    var showEdicaoDialog by remember { mutableStateOf(false) }
    var itemParaEditar by remember { mutableStateOf<ModelEstoque?>(null) }
    var edicaoSucesso by remember { mutableStateOf(false) }
    var mensagemErroEdicao by remember { mutableStateOf<String?>(null) }

    val listaCategorias by viewModel.categorias.collectAsState()

    println("Executando tela de estoque")

    LaunchedEffect(true) {
        println("TelaEstoque LaunchedEffect")
        viewModel.carregarEstoque()
    }

    LaunchedEffect(exclusaoSucesso) {
        if (exclusaoSucesso) {
            println("Item do estoque excluído com sucesso!")
            exclusaoSucesso = false // Resetar o estado
            showDialogExcluir = false // Fechar o diálogo após o sucesso
            viewModel.carregarEstoque() // Recarregar a lista
        }
    }

    LaunchedEffect(mensagemErroExclusao) {
        if (!mensagemErroExclusao.isNullOrEmpty()) {
            println("Erro ao excluir item do estoque: $mensagemErroExclusao")
            // Opcional: Mostrar uma mensagem de erro ao usuário
        }
    }

    LaunchedEffect(edicaoSucesso) {
        if (edicaoSucesso) {
            println("Item do estoque editado com sucesso!")
            edicaoSucesso = false
            showEdicaoDialog = false
            viewModel.carregarEstoque() // Recarregar a lista após a edição
        }
    }

    LaunchedEffect(mensagemErroEdicao) {
        if (!mensagemErroEdicao.isNullOrEmpty()) {
            println("Erro ao editar item do estoque: $mensagemErroEdicao")
            // Opcional: Mostrar uma mensagem de erro ao usuário
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        menuComTituloPage("Estoque", navController)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InputPesquisarEstoque(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por Nome") }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar um item no estoque!")
                        navController.navigate("EstoqueCadastro")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else if (!erroCarregar.isNullOrEmpty()) {
            Text(
                text = "Erro ao carregar estoque: $erroCarregar",
                color = Color.Red,
                style = textPadrao
            )
        } else {
            LazyColumn {
                item {
                    val estoqueFiltrado = if (pesquisa.isBlank()) {
                        itensEstoque
                    } else {
                        itensEstoque.filter { it.nome.contains(pesquisa, ignoreCase = true) }
                    }

                    for (item in estoqueFiltrado) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.nome,
                                style = textPadrao.copy(fontSize = 16.sp),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 10.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            cardEstoque(
                                itemEstoque = item,
                                coluna1Info1 = "Preço: R\$ ${String.format("%.2f", item.preco)}",
                                coluna1Info2 = "Quantidade: ${item.quantidade}",
                                coluna2Info1 = "ID: ${item.id}",
                                coluna2Info2 = "Unidade: ${item.unidadeMedida}",
                                onEditClick = { itemSelecionado ->
                                    itemParaEditar = itemSelecionado
                                    showEdicaoDialog = true
                                },
                                onDeleteClick = { itemSelecionado ->
                                    itemParaExcluir = itemSelecionado
                                    showDialogExcluir = true
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }


            if (showDialogExcluir && itemParaExcluir != null) {
                ExcluirEstoqueDialog(
                    item = itemParaExcluir!!,
                    onDismiss = { showDialogExcluir = false },
                    onConfirmExcluir = { itemExcluir ->
                        viewModel.excluirEstoque(
                            item = itemExcluir,
                            onExclusaoSucesso = { exclusaoSucesso = true },
                            onExclusaoErro = { mensagemErroExclusao = it }
                        )
                    }
                )
            }

            if (showEdicaoDialog && itemParaEditar != null) {
                EditarEstoqueDialog(
                    item = itemParaEditar!!,
                    listaCategorias = viewModel.categorias.collectAsState().value,
                    onDismiss = { itemParaEditar = null },
                    onSalvar = { itemAtualizado -> // 'itemAtualizado' aqui vem do diálogo
                        viewModel.atualizarEstoque(
                            itemAtualizadoRecebido = itemAtualizado, // Passa o item do diálogo
                            onSucesso = { itemParaEditar = null },
                            onError = { mensagem -> /* Lidar com o erro */ }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun InputPesquisarEstoque(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: TextStyle,
    labelInfo: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    Column()
    {
        Text(text = titulo, style = textStyle) // Tipo texto com o estilo passado no "Campo"

        // Input
        TextField(
            value = valor,
            onValueChange = onValorChange, // Atualiza o valor conforme o usuário digita
            textStyle = textStyle.copy(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier
                .inputPadrao(),
            singleLine = false, // impede quebra de linha na input
            label = labelInfo
        )
    }
}

@Composable
fun cardEstoque(
    itemEstoque: ModelEstoque,
    coluna1Info1: String,
    coluna1Info2: String,
    coluna2Info1: String,
    coluna2Info2: String,
    onEditClick: (ModelEstoque) -> Unit,
    onDeleteClick: (ModelEstoque) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { println("Clicou no card do item ${itemEstoque.nome}") },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color(0xFF2B2B2B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.icone_editar),
                contentDescription = "Editar",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onEditClick(itemEstoque) }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.icone_deletar),
                contentDescription = "Excluir",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDeleteClick(itemEstoque) }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = coluna1Info1,
                    style = textPadrao.copy(fontSize = 16.sp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = coluna1Info2,
                    style = textPadrao.copy(fontSize = 16.sp),
                    color = Color.White
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = coluna2Info1,
                    style = textPadrao.copy(fontSize = 16.sp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = coluna2Info2,
                    style = textPadrao.copy(fontSize = 16.sp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ExcluirEstoqueDialog(
    item: ModelEstoque,
    onDismiss: () -> Unit,
    onConfirmExcluir: (ModelEstoque) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Exclusão", color = Color.White) },
        text = { Text("Deseja mesmo excluir o item ${item.nome}?", color = Color.White) },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(Color.Gray),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Não", color = Color.White)
                }

                Button(
                    onClick = {
                        onConfirmExcluir(item)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Sim", color = Color.White)
                }
            }
        },
        containerColor = Color(0xFF2B2B2B)
    )
}

@Composable
fun EditarEstoqueDialog(
    item: ModelEstoque,
    listaCategorias: List<ModelCategoria>,
    onDismiss: () -> Unit,
    onSalvar: (ModelEstoque) -> Unit
) {
    var nomeEditado by remember { mutableStateOf(item.nome) }
    var descricaoEditada by remember { mutableStateOf(item.descricao) }
    var unidadeMedidaEditada by remember { mutableStateOf(item.unidadeMedida) }
    var precoEditado by remember { mutableStateOf(item.preco.toString()) }
    var quantidadeEditada by remember { mutableStateOf(item.quantidade.toString()) }

    // Novo estado para a categoria selecionada no DropdownMenu
    var categoriaSelecionada by remember { mutableStateOf<ModelCategoria?>(item.categoria) }
    var expandedCategoria by remember { mutableStateOf(false) }

    var erroNome by remember { mutableStateOf<String?>(null) }
    var erroDescricao by remember { mutableStateOf<String?>(null) }
    var erroPreco by remember { mutableStateOf<String?>(null) }
    var erroQuantidade by remember { mutableStateOf<String?>(null) }
    var erroCategoria by remember { mutableStateOf<String?>(null) }

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
                Text("Editar Item do Estoque", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))

                // Dropdown para selecionar a categoria
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box {
                        OutlinedTextField(
                            value = categoriaSelecionada?.nome ?: "Categoria", // Texto inicial
                            onValueChange = {  },
                            label = { Text("Selecione a Categoria", style = textPadrao.copy(fontSize = 16.sp, color = Color.Gray)) },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    tint = Color.White,
                                    modifier = Modifier.clickable { expandedCategoria = !expandedCategoria }
                                )
                            },
                            textStyle = textPadrao.copy(fontSize = 16.sp, color = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = expandedCategoria,
                            onDismissRequest = { expandedCategoria = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listaCategorias.forEach { categoria ->
                                DropdownMenuItem(
                                    text = { Text(categoria.nome, color = Color.White) },
                                    onClick = {
                                        categoriaSelecionada = categoria
                                        expandedCategoria = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
                    value = descricaoEditada,
                    onValueChange = { descricaoEditada = it },
                    label = { Text("Descrição", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = unidadeMedidaEditada,
                    onValueChange = { unidadeMedidaEditada = it },
                    label = { Text("Unidade de Medida", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = precoEditado,
                    onValueChange = {
                        precoEditado = it
                        erroPreco = null
                        if (it.isNotBlank() && it.toDoubleOrNull() == null) {
                            erroPreco = "Preço inválido"
                        }
                    },
                    label = { Text("Preço", color = Color.Gray) },
                    isError = erroPreco != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroPreco?.let {
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
                    value = quantidadeEditada,
                    onValueChange = {
                        quantidadeEditada = it
                        erroQuantidade = null
                        if (it.isNotBlank() && it.toIntOrNull() == null) {
                            erroQuantidade = "Quantidade inválida"
                        }
                    },
                    label = { Text("Quantidade", color = Color.Gray) },
                    isError = erroQuantidade != null,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                erroQuantidade?.let {
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
                            if (precoEditado.isBlank() || precoEditado.toDoubleOrNull() == null) {
                                erroPreco = "O preço é obrigatório e deve ser um número."
                                isValid = false
                            }
                            if (quantidadeEditada.isBlank() || quantidadeEditada.toIntOrNull() == null) {
                                erroQuantidade =
                                    "A quantidade é obrigatória e deve ser um número inteiro."
                                isValid = false
                            }
                            if (categoriaSelecionada == null) {
                                erroCategoria = "A categoria é obrigatória."
                                isValid = false
                            }

                            if (isValid) {
                                val itemAtualizado = item.copy(
                                    nome = nomeEditado,
                                    descricao = descricaoEditada,
                                    unidadeMedida = unidadeMedidaEditada,
                                    preco = precoEditado.toBigDecimal(),
                                    quantidade = quantidadeEditada.toInt(),
                                    categoria = categoriaSelecionada!!
                                )
                                onSalvar(itemAtualizado)
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

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)
@Composable
fun GreetingPreviewEstoque() {
    CodemobileTheme {
        val navController = rememberNavController()
        TelaEstoque(navController)
    }
}