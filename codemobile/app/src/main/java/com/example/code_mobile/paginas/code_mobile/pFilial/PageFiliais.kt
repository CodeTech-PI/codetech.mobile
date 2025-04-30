package com.example.code_mobile.paginas.code_mobile.pFilial

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.ConfirmDeleteDialog
import com.example.code_mobile.paginas.code_mobile.Input
import com.example.code_mobile.paginas.code_mobile.cardFilial
import com.example.code_mobile.paginas.code_mobile.pComponente.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.cModel.ModelFiliais
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelFilial
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.delay


@Composable
fun Filiais(navController: NavController, viewModel: ViewModelFilial, modifier: Modifier = Modifier) {

    var showDialog by remember { mutableStateOf(false) }
    var filialToDelete by remember { mutableStateOf<ModelFiliais?>(null) }
    var filialToEdit by remember { mutableStateOf<ModelFiliais?>(null) }
    var pesquisa by remember { mutableStateOf("") }

    // Carregar filiais
    LaunchedEffect(Unit) {
        viewModel.carregarFiliais()
    }
    LaunchedEffect(navController.currentBackStackEntry) {
        // Esse efeito será chamado toda vez que essa tela for exibida novamente
        delay(2000) // espera 2 segundos
        viewModel.carregarFiliais()
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
        if (viewModel.showLoading.collectAsState().value) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            // Filtrar filiais por nome, caso haja pesquisa
            val filiaisFiltradas = if (pesquisa.isEmpty()) {
                viewModel.filiais.collectAsState().value
            } else {
                viewModel.filiais.collectAsState().value.filter {
                    it.logradouro.contains(pesquisa, ignoreCase = true)
                }
            }

            // Exibir filiais
            filiaisFiltradas.forEach { filial ->
                Box(modifier = Modifier.padding(8.dp)) {
                    cardFilial(
                        "Rua: ${filial.logradouro}",
                        "Estado: ${filial.estado}",
                        "Cidade: ${filial.cidade}",
                        "CEP: ${filial.cep}",
                        "Status: Operante",  // Ajuste conforme a lógica de status

                        onDeleteClick = {
                            filialToDelete = filial
                            showDialog = true
                        },
                        OnEditClick ={
                            filialToEdit = filial
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("filial", filial.id)
                            // ao direcionar para tela de editar preciso recuperar essa filialToEdit
                            navController.navigate("FiliaisEditar")
                        }
                    )
                }
            }

            // Se não houver filiais
            if (filiaisFiltradas.isEmpty()) {
                Text("Nenhuma filial encontrada", color = Color.White, modifier = Modifier.padding(16.dp))
            }
        }
    }

    // Diálogo de exclusão
    if (showDialog && filialToDelete != null) {
        ConfirmDeleteDialog(
            onConfirm = {
                filialToDelete?.let { filial ->
                    viewModel.deletarFilial(filial)
                    showDialog = false
                    Toast.makeText(navController.context, "Filial excluída!", Toast.LENGTH_SHORT).show()

                }
            },
            onCancel = {
                showDialog = false
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewFiliais() {
    CodemobileTheme {
        val navController = rememberNavController()
        val viewModel = ViewModelFilial()
        Filiais(navController = navController, viewModel = viewModel)
    }
}