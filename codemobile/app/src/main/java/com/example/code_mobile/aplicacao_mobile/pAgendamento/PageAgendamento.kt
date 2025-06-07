package com.example.code_mobile.aplicacao_mobile.pAgendamento

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.code_mobile.R
import com.example.code_mobile.aplicacao_mobile.cViewModel.ViewModelAgendamento
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelCliente
import com.example.code_mobile.paginas.code_mobile.pCliente.cardCliente
import com.example.code_mobile.paginas.code_mobile.pComponente.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun TelaAgendamento(navController: NavController, modifier: Modifier = Modifier) {

    // Não tem front do card, precisa fazer. Tem o protótipo lá no figma
    // O get não ta vinculado no back para listar os agendamentos
    // O botão de adicionar ta vinculado no back

    val viewModel: ViewModelAgendamento = viewModel()
    var pesquisa by remember { mutableStateOf("") }
    val agendamentos by viewModel.agendamentos.collectAsState()
    val isLoading by viewModel.isLoadingAgendamentos.collectAsState()
    val erroCarregar by viewModel.erroCarregarAgendamentos.collectAsState()

    LaunchedEffect(true) {
        println("Tela Agendamentos LaunchedEffect")
        viewModel.carregarAgendamentos();
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        menuComTituloPage("Atendimentos", navController)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InputPesquisarAgendamento(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por data") }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar um agendamento!")
                        navController.navigate("AgendamentoCadastro")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else if (!erroCarregar.isNullOrEmpty()) {
            Text(
                text = "Erro ao carregar clientes: $erroCarregar",
                color = Color.Red,
                style = textPadrao
            )
        } else {
            LazyColumn {
                item {
                    val agendamentoFiltrados = if (pesquisa.isBlank()) {
                        agendamentos
                    } else {
                        agendamentos.filter { it.dt.contains(pesquisa) }
                    }

                    for (agendamento in agendamentoFiltrados) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            cardExibirAgendamento(
                                agendamento =  agendamento,
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_2
)

@Composable
fun GreetingPreviewAgendamentos() {
    androidx.compose.material3.MaterialTheme {
        val navController = androidx.navigation.compose.rememberNavController()
        TelaAgendamento(navController)
    }
}