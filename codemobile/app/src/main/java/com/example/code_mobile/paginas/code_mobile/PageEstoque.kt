package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun TelaEstoque(navController: NavController, modifier: Modifier = Modifier) {
    var pesquisa by remember { mutableStateOf("") }
    var showCadastroDialog by remember { mutableStateOf(false) }
    var showEdicaoDialog by remember { mutableStateOf(false) }
    var produtoEditado by remember { mutableStateOf(Produto("Tinta", "Tinta Preta", "Tinta Preta para desenho", "ml", "35", "10,00")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        manuComTituloPage("Estoque")

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

        Box {
            card4Informacoes(
                R.drawable.icon_instrumentos,
                "Equipamentos de tatuagem",
                "Tinta Rosa",
                "R$20.00",
                "Tinta",
                "Quantidade:30",
                onEditClick = {
                    println("Ícone de edição clicado!")
                    produtoEditado = Produto("Tinta", "Tinta Preta", "Tinta Preta para desenho", "ml", "35", "10,00")
                    showEdicaoDialog = true
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showCadastroDialog = true },
            modifier = Modifier
                .width(300.dp)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
        ) {
            Text("Novo Produto", color = Color.White)
        }
    }

    if (showEdicaoDialog) {
        ModalEdicaoProduto(produto = produtoEditado, onDismiss = { showEdicaoDialog = false })
    }

    if (showCadastroDialog) {
        NovoProdutoDialog(onDismiss = { showCadastroDialog = false })
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
fun NovoProdutoDialog(onDismiss: () -> Unit) {
    var categoriaSelecionada by remember { mutableStateOf("Selecione") }
    val categorias = listOf("Tinta", "Papel", "Agulha", "Outro")
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(Color(0xFF121212))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Cadastrar", color = Color.White, fontSize = 20.sp)
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
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Produto") })
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Descrição") })
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Unidade Medida") })
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Quantidade") })
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Preço") })

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                        Text("Cancelar")
                    }
                    Button(onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(Color(0xFFE91E63))) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}


@Composable
fun ModalEdicaoProduto(produto: Produto, onDismiss: () -> Unit) {
    var categoria by remember { mutableStateOf(produto.categoria) }
    var nome by remember { mutableStateOf(produto.nome) }
    var descricao by remember { mutableStateOf(produto.descricao) }
    var unidade by remember { mutableStateOf(produto.unidade) }
    var quantidade by remember { mutableStateOf(produto.quantidade) }
    var preco by remember { mutableStateOf(produto.preco) }

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
                onClick = onDismiss,
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

data class Produto(
    val categoria: String,
    val nome: String,
    val descricao: String,
    val unidade: String,
    val quantidade: String,
    val preco: String
)




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
