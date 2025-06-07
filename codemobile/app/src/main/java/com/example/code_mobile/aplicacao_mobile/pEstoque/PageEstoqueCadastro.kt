package com.example.code_mobile.paginas.code_mobile.pEstoque

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelEstoque
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueCadastro(navController: NavController, modifier: Modifier = Modifier) {

    val viewModel: ViewModelEstoque = viewModel()

    val nome by viewModel.nome
    val descricao by viewModel.descricao
    val unidadeMedida by viewModel.unidadeMedida
    val preco by viewModel.preco
    val quantidade by viewModel.quantidade

    val nomeError by viewModel.nomeError
    val precoError by viewModel.precoError
    val quantidadeError by viewModel.quantidadeError

    val cadastroSucesso by viewModel.cadastroSucesso.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val mensagemErroBackend by viewModel.mensagemErro.collectAsState()

    val listaCategorias by viewModel.categorias.collectAsState()
    var categoriaSelecionada by remember { mutableStateOf<ModelCategoria?>(null) }
    var expandedCategoria by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.carregarCategorias() // Carrega as categorias ao iniciar a tela
    }

    LaunchedEffect(cadastroSucesso) {
        if (cadastroSucesso) {
            delay(3000)
            navController.navigate("Estoque")
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
                .verticalScroll(scrollState)
                .background(Color(0xFF1B1B1B))
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento padrão
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigate("Estoque") }
                )
                Text(
                    text = "Cadastrar Produto",
                    style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(24.dp))
            }

            // Dropdown para selecionar a categoria
            Column(modifier = Modifier.fillMaxWidth()) {
                Box {
                    OutlinedTextField(
                        value = categoriaSelecionada?.nome ?: "Selecione uma categoria", // Texto inicial
                        onValueChange = { /* Não permitir edição direta */ },
                        label = { Text("Categoria", style = textPadrao.copy(fontSize = 16.sp, color = Color.Gray)) },
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

            CampoCadastrarEstoque(
                titulo = "Nome:",
                valor = nome,
                onValorChange = viewModel::atualizarNome,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: Luva",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                errorMessage = nomeError
            )

            CampoCadastrarEstoque(
                titulo = "Descrição:",
                valor = descricao,
                onValorChange = viewModel::atualizarDescricao,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: Marca latex, cor azul",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
            )

            CampoCadastrarEstoque(
                titulo = "Unidade de Medida:",
                valor = unidadeMedida,
                onValorChange = viewModel::atualizarUnidadeMedida,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: Unidade, Kg, Metro",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
            )

            CampoCadastrarEstoque(
                titulo = "Preço:",
                valor = preco,
                onValorChange = viewModel::atualizarPreco,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: 29.90",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                errorMessage = precoError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            CampoCadastrarEstoque(
                titulo = "Quantidade:",
                valor = quantidade,
                onValorChange = viewModel::atualizarQuantidade,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: 100",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                errorMessage = quantidadeError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF252525))
                ) {
                    Text(text = "Cancelar", fontSize = 14.sp)
                }

                Button(
                    onClick = {
                        viewModel.cadastrarEstoque(categoriaSelecionada) // Passa a categoria selecionada
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                    enabled = !showLoading
                ) {
                    Text(
                        text = if (showLoading) "Salvando..." else "Salvar",
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Popup de confirmação de cancelamento
        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = {
                    Text(
                        "Confirmar Cancelamento",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        "Deseja mesmo abandonar o cadastro?",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showCancelDialog = false },
                            colors = ButtonDefaults.buttonColors(Color.Gray),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("Não", color = Color.White, fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                showCancelDialog = false
                                navController.navigate("Estoque")
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("Sim", color = Color.White, fontSize = 14.sp)
                        }
                    }
                },
                containerColor = Color(0xFF2B2B2B)
            )
        }

        if (cadastroSucesso) {
            Dialog(
                onDismissRequest = {
                    navController.navigate("Estoque")
                }
            ) {
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Sucesso",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            "Cadastrado com Sucesso!",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Button(
                            onClick = {
                                navController.navigate("Estoque")
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
                        ) {
                            Text("Ok", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        mensagemErroBackend?.let { erro ->
            AlertDialog(
                onDismissRequest = { viewModel.limparMensagemDeErro() },
                title = { Text("Erro", color = Color.White, fontSize = 16.sp) },
                text = { Text(erro, color = Color.White, fontSize = 14.sp) },
                confirmButton = {
                    TextButton(onClick = { viewModel.limparMensagemDeErro() }) {
                        Text("OK", color = Color.White, fontSize = 14.sp)
                    }
                },
                containerColor = Color(0xFF2B2B2B)
            )
        }

        if (showLoading) {
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
    }
}

@Composable
fun CampoCadastrarEstoque(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle,
    placeholderText: String,
    tituloStyle: androidx.compose.ui.text.TextStyle,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = titulo, style = tituloStyle.copy(color = Color.White))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            textStyle = textStyle.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholderText, style = textStyle.copy(color = Color.Gray)) },
            isError = errorMessage != null,
            keyboardOptions = keyboardOptions
        )
        errorMessage?.let {
            Text(text = it, color = Color.Red, style = textStyle.copy(fontSize = 12.sp))
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)
@Composable
fun EstoqueCadastroPreview() {
    CodemobileTheme {
        val navController = rememberNavController()
        EstoqueCadastro(navController)
    }
}