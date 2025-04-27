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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.code_mobile.paginas.code_mobile.viewModel.ViewModelFiliais

import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun Filiais(navController: NavController, viewModel: ViewModelFiliais = viewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    var filialParaExcluir by remember { mutableStateOf<ModelFiliais?>(null) }
    var pesquisa by remember { mutableStateOf("") }

    // Observa o estado da lista de filiais da ViewModel
    val listaFiliais by viewModel.listaFiliais.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensagemErro by viewModel.mensagemErro.collectAsState()
    val operacaoSucesso by viewModel.operacaoSucesso.collectAsState()

    // Efeito colateral para recarregar a lista após uma operação de sucesso
    LaunchedEffect(operacaoSucesso) {
        if (operacaoSucesso) {
            viewModel.resetOperacaoSucesso()
            // A lista será automaticamente atualizada devido ao collectAsState
        }
    }

    // Efeito colateral para exibir mensagens de erro
    LaunchedEffect(mensagemErro) {
        mensagemErro?.let {
            println("Erro: $it") // Ou exibir um Snackbar/Toast
            viewModel.limparMensagemDeErro()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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

        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(listaFiliais.filter {
                    it.cidade.contains(pesquisa, ignoreCase = true) ||
                            it.estado.contains(pesquisa, ignoreCase = true) ||
                            it.lagradouro.contains(pesquisa, ignoreCase = true) ||
                            it.cep.contains(pesquisa, ignoreCase = true)
                }) { filial ->
                    cardFilial(
                        logradouro = filial.lagradouro,
                        estado = filial.estado,
                        cidade = filial.cidade,
                        cep = filial.cep,
                        status = "Operante", // Você precisará adicionar o status ao seu ModelFiliais se necessário
                        onDeleteClick = {
                            filialParaExcluir = filial
                            showDialog = true
                        },
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showDialog && filialParaExcluir != null) {
        ConfirmDeleteDialog(
            onConfirm = {
                filialParaExcluir?.let { viewModel.deletarFilial(it.num) } // Assumindo que 'num' é o ID
                filialParaExcluir = null
                showDialog = false
            },
            onCancel = {
                filialParaExcluir = null
                showDialog = false
            }
        )
    }
}




@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun GreetingPreviewFiliais() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        Filiais(navController)  // Passe o navController para TelaLogin
    }
}