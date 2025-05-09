package com.example.code_mobile.aplicacao_mobile.pAgendamento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.code_mobile.aplicacao_mobile.cModel.ModelAgendamento
import com.example.code_mobile.aplicacao_mobile.cModel.ModelListaProduto
import com.example.code_mobile.aplicacao_mobile.cViewModel.ViewModelListaProduto
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelEstoque
import com.example.code_mobile.paginas.code_mobile.pEstoque.InputPesquisarEstoque
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.ui.theme.CodemobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendamentoEtapa2(
    navController: NavController,
    agendamentoId: Int,
    modifier: Modifier = Modifier
) {

    var pesquisa by remember { mutableStateOf("") }
    val viewModelEstoque: ViewModelEstoque = viewModel()
    val viewModelListaProduto: ViewModelListaProduto = viewModel()
    val listaEstoque by viewModelEstoque.estoque.collectAsState()

    LaunchedEffect(true) {
        println("TelaAgendamento2 LaunchedEffect")
        viewModelEstoque.carregarEstoque()
    }
    println("Conteúdo de listaEstoque: $listaEstoque")
    val estoqueFiltrado = remember(pesquisa, listaEstoque) {
        if (pesquisa.isBlank()) {
            listaEstoque
        } else {
            listaEstoque.filter { it.nome.contains(pesquisa, ignoreCase = true) }
        }
    }
    println("Conteúdo de estoqueFiltrado: $estoqueFiltrado")

    val showLoadingListaProduto by viewModelListaProduto.isLoadingListaProduto.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    val produtosSelecionados = remember { mutableStateMapOf<Int, Int>() } // <idProduto, quantidade>

    // Estado para feedback de operações (criar lista de produtos)
    val operacaoSucessoListaProduto by viewModelListaProduto.operacaoSucesso.collectAsState()
    val mensagemOperacaoListaProduto by viewModelListaProduto.mensagemOperacao.collectAsState()
    val erroOperacaoListaProduto by viewModelListaProduto.erroListaProduto.collectAsState()

    LaunchedEffect(operacaoSucessoListaProduto) {
        if (operacaoSucessoListaProduto) {
            println("Lista de produtos criada com sucesso!")
            navController.navigate("AgendamentoCadastro3/$agendamentoId")
            viewModelListaProduto.resetOperacaoFeedback()
        }
    }

    LaunchedEffect(erroOperacaoListaProduto) {
        erroOperacaoListaProduto?.let { erro ->
            println("Erro ao criar lista de produtos: $erro")
            // Exibir mensagem de erro para o usuário, se necessário
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight(0.75f)
                .fillMaxSize()
                .background(Color(0xFF1B1B1B))
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            run {
                                showCancelDialog = true
                                navController.popBackStack() // Volta para AgendamentoEtapa1
                            }
                        }
                )
                Text(
                    text = "Produtos Utilizados",
                    style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(24.dp))
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputPesquisarEstoque(
                    titulo = "",
                    valor = pesquisa,
                    onValorChange = { pesquisa = it },
                    textStyle = textPadrao,
                    labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por Nome") }
                )
            }

            LazyColumn {
                items(estoqueFiltrado) { produto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var isProdutoSelecionado by remember {
                            mutableStateOf(
                                produtosSelecionados.containsKey(
                                    produto.id
                                )
                            )
                        }
                        var quantidadeProduto by remember(produtosSelecionados[produto.id]) {
                            mutableStateOf(produtosSelecionados[produto.id]?.toString() ?: "")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = isProdutoSelecionado,
                                onCheckedChange = { novoValor ->
                                    produto.id?.let { productId ->
                                        isProdutoSelecionado = novoValor
                                        if (novoValor) {
                                            produtosSelecionados[productId] = 1
                                            quantidadeProduto = "1"
                                        } else {
                                            produtosSelecionados.remove(productId)
                                            quantidadeProduto = ""
                                        }
                                    } ?: run {
                                        println("Erro: ID do produto é nulo ao selecionar/deselecionar.")
                                    }
                                }
                            )
                            Text(produto.nome, style = textPadrao.copy(color = Color.White))
                        }

                        if (isProdutoSelecionado) {
                            OutlinedTextField(
                                value = quantidadeProduto,
                                onValueChange = { newValue ->
                                    println("Tipo de produto.id: ${produto.id?.javaClass?.name}, Valor: ${produto.id}")
                                    val filteredValue = newValue.filter { it.isDigit() }

                                    produto.id?.let { produtoId ->
                                        if (filteredValue.isNotBlank()) {
                                            produtosSelecionados[produtoId] = filteredValue.toInt()
                                        } else {
                                            produtosSelecionados.remove(produtoId) // Use produtoId aqui também
                                        }
                                        quantidadeProduto = filteredValue
                                    } // Fechamento do let
                                },
                                label = { Text("Qtd.", color = Color.Gray) },
                                modifier = Modifier
                                    .width(80.dp),
                                textStyle = textPadrao.copy(color = Color.White),
                                colors = OutlinedTextFieldDefaults.colors(
                                    Color(0xFFDF0050)
                                )
                            )
                        }
                    }
                    Divider(color = Color(0xFF505050), thickness = 1.dp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Row dos botões fixada na parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom // Alinhar os botões na parte inferior da Row
            ) {
                Button(
                    onClick = {
                        navController.popBackStack() // Volta para AgendamentoEtapa1
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF252525))
                ) {
                    Text(text = "Voltar", fontSize = 14.sp, color = Color.White)
                }

                Button(
                    onClick = {
                        println("Produtos Selecionados: $produtosSelecionados para o agendamento $agendamentoId")
                        viewModelListaProduto.cadastrarListaProduto(
                            produtosSelecionadosMap = produtosSelecionados,
                            agendamentoId = agendamentoId,
                            onSucesso = {
                                println("Produtos adicionados à lista do agendamento $agendamentoId com sucesso.")
                                println("Dados enviados (IDs): Produtos=$produtosSelecionados, Agendamento=$agendamentoId")
                                navController.navigate("AgendamentoCadastro3/$agendamentoId")
                            },
                            onError = { erro ->
                                println("Erro ao adicionar produtos à lista do agendamento $agendamentoId: $erro")
                                println("Dados da tentativa (IDs): Produtos=$produtosSelecionados, Agendamento=$agendamentoId")
                                // Exibir mensagem de erro para o usuário
                            }
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050)),
                    enabled = !showLoadingListaProduto && agendamentoId != -1 // Certifique-se que temos o ID do agendamento
                ) {
                    Text(
                        text = if (showLoadingListaProduto) "Carregando..." else "Próximo",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
    }

    if (showLoadingListaProduto) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2B2B2B)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancelar Seleção de Produtos?", color = Color.White) },
            text = {
                Text(
                    "Tem certeza que deseja voltar e cancelar a seleção de produtos?",
                    color = Color.White
                )
            },
            confirmButton = {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
                ) {
                    Text("Sim", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCancelDialog = false },
                    colors = ButtonDefaults.buttonColors(Color(0xFF252525))
                ) {
                    Text("Não", color = Color.White)
                }
            },
            containerColor = Color(0xFF2B2B2B)
        )
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
fun AgendamentoEtapa2Preview() {
    CodemobileTheme {
        val navController = rememberNavController()
        AgendamentoEtapa2(navController = navController, agendamentoId = 1)
    }
}