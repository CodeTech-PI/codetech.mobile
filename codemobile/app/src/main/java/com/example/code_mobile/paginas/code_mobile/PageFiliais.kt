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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.model.ModelFiliais
import com.example.code_mobile.paginas.code_mobile.viewModel.filial.ViewModelFilial
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun Filiais(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: ViewModelFilial = viewModel()

    var pesquisa by remember { mutableStateOf("") }
    val filiais by viewModel.filiais.collectAsState()
    val isLoading by viewModel.isLoadingFiliais.collectAsState()
    val erroCarregar by viewModel.erroCarregarFiliais.collectAsState()
    val exclusaoSucesso by viewModel.exclusaoSucesso.collectAsState()
    val mensagemErroExclusao by viewModel.mensagemErroExclusao.collectAsState()

    var showDialogExcluir by remember { mutableStateOf(false) }
    var filialParaExcluir by remember { mutableStateOf<ModelFiliais?>(null) }

    LaunchedEffect(true) {
        viewModel.carregarFiliais()
    }

    LaunchedEffect(exclusaoSucesso) {
        if (exclusaoSucesso) {
            println("Filial excluída com sucesso!")
            viewModel.resetExclusaoSucesso()
            showDialogExcluir = false
        }
    }

    LaunchedEffect(mensagemErroExclusao) {
        if (!mensagemErroExclusao.isNullOrEmpty()) {
            println("Erro ao excluir filial: $mensagemErroExclusao")
            // Opcional: Mostrar mensagem de erro ao usuário
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
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Input(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Pesquisar filial") },
                modifier = Modifier.weight(1f)
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

        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else if (!erroCarregar.isNullOrEmpty()) {
            Text(
                text = "Erro ao carregar filiais: $erroCarregar",
                color = Color.Red,
                style = textPadrao
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val filiaisFiltradas = if (pesquisa.isBlank()) {
                    filiais
                } else {
                    filiais.filter {
                        it.lagradouro.contains(pesquisa, ignoreCase = true) ||
                                it.cidade.contains(pesquisa, ignoreCase = true) ||
                                it.estado.contains(pesquisa, ignoreCase = true) ||
                                it.cep.contains(pesquisa, ignoreCase = true)
                    }
                }

                filiaisFiltradas.forEach { filial ->
                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        cardFilial(
                            logradouro = "Rua: ${filial.lagradouro}",
                            estado = "Estado: ${filial.estado}",
                            cidade = "Cidade: ${filial.cidade}",
                            cep = "CEP: ${filial.cep}",
                            status = "Status: ${filial.estado}", // Mantendo 'estado' aqui, ajuste se necessário
                            onDeleteClick = {
                                filialParaExcluir = filial
                                showDialogExcluir = true
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    if (showDialogExcluir && filialParaExcluir != null) {
        ExcluirFilialDialog( // Usando o diálogo correto
            filial = filialParaExcluir!!,
            onDismiss = { showDialogExcluir = false },
            onConfirmExcluir = { filialExcluir ->
                viewModel.deletarFilial( // Chamando a função correta do ViewModel
                    filial = filialExcluir, // Passando o objeto ModelFiliais completo
                    onExclusaoSucesso = { viewModel.resetExclusaoSucesso() },
                    onExclusaoErro = { viewModel.setMensagemErroExclusao(it) }
                )
            }
        )
    }
}

@Composable
fun ExcluirFilialDialog(
    filial: com.example.code_mobile.paginas.code_mobile.model.ModelFiliais,
    onDismiss: () -> Unit,
    onConfirmExcluir: (com.example.code_mobile.paginas.code_mobile.model.ModelFiliais) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Exclusão", color = Color.White) },
        text = { Text("Deseja mesmo excluir a filial ${filial.lagradouro}?", color = Color.White) },
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

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)
@Composable
fun GreetingPreviewFiliais() {
    CodemobileTheme {
        val navController = rememberNavController()
        Filiais(navController)
    }
}