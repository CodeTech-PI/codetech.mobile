package com.example.code_mobile.paginas.code_mobile.estoque

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.componente.Input
import com.example.code_mobile.paginas.code_mobile.componente.card4Informacoes
import com.example.code_mobile.paginas.code_mobile.componente.textPadrao
import com.example.code_mobile.paginas.code_mobile.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.model.ModelEstoque
import com.example.code_mobile.paginas.code_mobile.service.ServiceEstoque
import com.example.code_mobile.token.network.RetrofithAuth
import com.example.code_mobile.token.network.TokenManager
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun TelaEstoque(navController: NavController, modifier: Modifier = Modifier) {
    var produtos by remember { mutableStateOf<List<ModelEstoque>>(emptyList()) }
    var pesquisa by remember { mutableStateOf("") }
    var showCadastroDialog by remember { mutableStateOf(false) }
    var showEdicaoDialog by remember { mutableStateOf(false) }
    var produtoParaEditar by remember { mutableStateOf<ModelEstoque?>(null) } // Estado para o produto a ser editado

    // Função para adicionar um novo produto à lista
    val adicionarNovoProduto: (ModelEstoque) -> Unit = { novoProduto ->
        produtos = produtos + novoProduto
    }

    LaunchedEffect(true) {
        println("TelaEstoque LaunchedEffect, token atual: ${TokenManager.token}")
        try {
            val serviceProduto = RetrofithAuth.retrofit.create(ServiceEstoque::class.java)
            val response = serviceProduto.getProdutos()

            println("Entrou no try")
            println(response.body())

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    produtos = body
                } else {
                    println("Corpo da resposta nulo ao carregar produtos.")
                    produtos = emptyList()
                }
            } else {
                println(
                    "Erro ao carregar produtos: ${response.code()} - ${
                        response.errorBody()?.string()
                    }"
                )
            }
        } catch (e: Exception) {
            println("Erro na requisição: ${e.message}")
            println("Entrou no catch")
            e.printStackTrace()
        }
    }

    val produtosFiltrados = remember(produtos, pesquisa) {
        produtos.filter {
            it.categoria?.contains(pesquisa, ignoreCase = true) ?: false ||
                    it.nome?.contains(pesquisa, ignoreCase = true) ?: false ||
                    it.descricao?.contains(pesquisa, ignoreCase = true) ?: false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        menuComTituloPage("Estoque", navController)

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Input(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por categoria") },
                modifier = Modifier.weight(1f)
            )

            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 8.dp)
                    .clickable { showCadastroDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (produtosFiltrados.isEmpty()) {
            Text(
                "Nenhum produto encontrado.",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            for (produto in produtosFiltrados) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Text(
                        text = produto.nome ?: "",
                        style = textPadrao.copy(fontSize = 16.sp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )

                    card4Informacoes(
                        coluna1Info1 = "Categoria: ${produto.categoria ?: ""}",
                        coluna1Info2 = "Descrição: ${produto.descricao ?: ""}",
                        coluna2Info1 = "Unidade: ${produto.unidadeMedida ?: ""}",
                        coluna2Info2 = "Quantidade: ${produto.quantidade ?: ""}",
                        onEditClick = {
                            println("Editar produto: ${produto.nome}")
                            produtoParaEditar = produto // Atribui o produto clicado à variável de estado
                            showEdicaoDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    if (showEdicaoDialog) {
        // Agora passamos o produtoParaEditar para o ModalEdicaoProduto
        produtoParaEditar?.let {
            ModalEdicaoProduto(produto = it, onDismiss = { showEdicaoDialog = false })
        }
    }

    if (showCadastroDialog) {
        NovoProdutoDialog(onDismiss = { showCadastroDialog = false }, onProdutoCadastrado = adicionarNovoProduto)
    }
}

@Composable
fun CampoTexto(label: String, valor: String, onValorChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, color = Color.White, fontSize = 14.sp)
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(color = Color.White)
        )
    }
}

@Composable
fun NovoProdutoDialog(onDismiss: () -> Unit, onProdutoCadastrado: (ModelEstoque) -> Unit) {
    var categoriaSelecionada by remember { mutableStateOf("Selecione") }
    val categorias = listOf("Tinta", "Papel", "Agulha", "Outro")
    var expanded by remember { mutableStateOf(false) }

    // Estados para os campos de texto
    var id by remember { mutableStateOf("") }
    var nomeProduto by remember { mutableStateOf("") }
    var descricaoProduto by remember { mutableStateOf("") }
    var unidadeMedidaProduto by remember { mutableStateOf("") }
    var quantidadeProduto by remember { mutableStateOf("") }
    var precoProduto by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(Color(0xFF121212))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Cadastrar.", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Categoria:", color = Color.White)
                Box {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color.DarkGray)
                    ) {
                        Text(text = categoriaSelecionada, color = Color.White)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(text = { Text(categoria) }, onClick = {
                                categoriaSelecionada = categoria
                                expanded = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = nomeProduto,
                    onValueChange = { nomeProduto = it },
                    label = { Text("Produto") },
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = descricaoProduto,
                    onValueChange = { descricaoProduto = it },
                    label = { Text("Descrição") },
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = unidadeMedidaProduto,
                    onValueChange = { unidadeMedidaProduto = it },
                    label = { Text("Unidade Medida") },
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = quantidadeProduto,
                    onValueChange = { quantidadeProduto = it },
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = precoProduto,
                    onValueChange = { precoProduto = it },
                    label = { Text("Preço") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = TextStyle(color = Color.White)
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val novoProduto = ModelEstoque(
                                categoria = categoriaSelecionada,
                                nome = nomeProduto,
                                descricao = descricaoProduto,
                                unidadeMedida = unidadeMedidaProduto,
                                quantidade = quantidadeProduto.toIntOrNull() ?: 0,
                                preco = precoProduto.toDoubleOrNull() ?: 0.0,
                                id = id.toIntOrNull() ?: 0,
                                // Adicione outros campos do seu ModelEstoque aqui, se houver
                            )
                            onProdutoCadastrado(novoProduto)
                            onDismiss()
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

@Composable
fun ModalEdicaoProduto(produto: ModelEstoque, onDismiss: () -> Unit) {
    var categoria by remember { mutableStateOf(produto.categoria ?: "") }
    var nome by remember { mutableStateOf(produto.nome ?: "") }
    var descricao by remember { mutableStateOf(produto.descricao ?: "") }
    var unidade by remember { mutableStateOf(produto.unidadeMedida ?: "") }
    var quantidade by remember { mutableStateOf(produto.quantidade?.toString() ?: "") }
    var preco by remember { mutableStateOf(produto.preco?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
        containerColor = Color(0xFF1B1B1B),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Editar", color = Color.White, fontSize = 18.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.Red)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CampoTexto("Categoria", categoria, { categoria = it })
                CampoTexto("Produto", nome, { nome = it })
                CampoTexto("Descrição", descricao, { descricao = it })
                CampoTexto("Unidade Medida", unidade, { unidade = it })
                CampoTexto("Quantidade", quantidade, { quantidade = it })
                CampoTexto("Preço", preco, { preco = it })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Aqui você implementaria a lógica para salvar as edições
                    val produtoEditado = ModelEstoque(
                        id = produto.id, // Mantém o ID do produto original
                        categoria = categoria,
                        nome = nome,
                        descricao = descricao,
                        unidadeMedida = unidade,
                        quantidade = quantidade.toIntOrNull() ?: 0,
                        preco = preco.toDoubleOrNull() ?: 0.0,

                        // Adicione outros campos conforme necessário
                    )
                    println("Produto editado: $produtoEditado")
                    onDismiss()
                    // TODO: Implementar a chamada para atualizar o produto na sua lista 'produtos'
                    // e possivelmente na API.
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text("Salvar", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}

// Remova a declaração de Produto e produto2, pois ModalEdicaoProduto agora usa ModelEstoque diretamente.
// data class Produto(...)
// val produto2 = Produto(...)

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