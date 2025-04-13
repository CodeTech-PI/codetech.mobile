package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
fun TelaCategorias(navController: NavController, modifier: Modifier = Modifier) {

    var pesquisa by remember { mutableStateOf("") }
    var showCadastroDialog by remember { mutableStateOf(false) }
    var showEdicaoDialog by remember { mutableStateOf(false) }
    var categoriaEditado by remember { mutableStateOf(Categoria("Tinta")) }

    val categorias = remember { mutableStateListOf(Categoria("Tinta")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        menuComTituloPage("Categoria", navController)

//        Row(
//            modifier = Modifier
//                .padding(horizontal = 20.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
//        ) {
            Input(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por categoria") },
                modifier = Modifier.weight(1f)
            )

//            Image(
//                painter = painterResource(id = R.drawable.icon_add),
//                contentDescription = "Adicionar",
//                modifier = Modifier
//                    .size(60.dp)
//                    .padding(top = 25.dp)
//                    .clickable { showCadastroDialog = true }
//            )
//        }

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            categorias
                .filter { it.categoria.contains(pesquisa, ignoreCase = true) }
                .forEachIndexed{
                index, categoria ->
                cardCategoria(
                    categoria.categoria,
                    onEditClick = {
                        categoriaEditado = categoria
                        showEdicaoDialog = true
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

    if (showEdicaoDialog) {
        ModalEdicaoCategoria(
            categoria = categoriaEditado,
            onDismiss = { showEdicaoDialog = false },
            onSave = { novaCategoria ->
                categoriaEditado = novaCategoria
            })
    }

    if (showCadastroDialog) {
        NovaCategoriaDialog(
            onDismiss = { showCadastroDialog = false },
            onSalvar = { novaCategoria ->
                categorias.add(novaCategoria)
                showCadastroDialog = false
            }
            )
    }
}

@Composable
fun CampoTextoCat(label: String, valor: String, onValorChange: (String) -> Unit) {
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
fun NovaCategoriaDialog(onDismiss: () -> Unit, onSalvar: (Categoria) -> Unit) {
    var categoriaTexto by remember { mutableStateOf("") }

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
                    value = categoriaTexto,
                    onValueChange = { categoriaTexto = it},
                    label = { Text("Categoria") },
                    textStyle = TextStyle(color = Color.White)
                    )

                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(Color.Gray)) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                          onSalvar(Categoria(categoriaTexto))
                            },
                        colors = ButtonDefaults.buttonColors(Color(0xFFE91E63))
                    ){
                        Text("Salvar")
                    }
                }
            }
        }
    }
}

@Composable
fun ModalEdicaoCategoria(categoria: Categoria, onDismiss: () -> Unit, onSave: (Categoria)-> Unit) {
    var categoriaTexto by remember { mutableStateOf(categoria.categoria) }

    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1B1B1B))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                // Título e botão fechar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Editar", color = Color.White, fontSize = 18.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CampoTexto("Categoria", categoriaTexto, { categoriaTexto = it })

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onSave(Categoria(categoriaTexto))
                            onDismiss()
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


data class Categoria(
    val categoria: String,
)

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun GreetingPreviewCategoria() {
    CodemobileTheme {

        val navController = rememberNavController()
        TelaCategorias(navController)
    }
}
