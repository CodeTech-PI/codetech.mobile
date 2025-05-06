package com.example.code_mobile.paginas.code_mobile.atendimento

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import android.view.MenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.code_mobile.paginas.code_mobile.viewModel.atendimento.ViewModelAtendimento

@Composable
fun PageAtendimento(navController: NavController) {
    val viewModel: ViewModelAtendimento = viewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       // menu("Atendimentos", navController)
        EtapasAtendimento(viewModel)
    }
}

@Composable
fun EtapasAtendimento(viewModel: ViewModelAtendimento) {
    var etapaAtual by remember { mutableStateOf(1) }
    var showConfirmacao by remember { mutableStateOf(false) }

    // Estados para os dados do atendimento
    var dataAtendimento by remember { mutableStateOf("") }
    var horarioAtendimento by remember { mutableStateOf("") }
    var cancelado by remember { mutableStateOf(false) }
    var clienteSelecionado by remember { mutableStateOf<String?>(null) }
    var nomeCliente by remember { mutableStateOf("") }
    var telefoneCliente by remember { mutableStateOf("") }
    var emailCliente by remember { mutableStateOf("") }
    var dataNascimentoCliente by remember { mutableStateOf("") }
    var cpfCliente by remember { mutableStateOf("") }
    var produtosUtilizados by remember { mutableStateOf(listOf<String>()) }
    var valorTatuagem by remember { mutableStateOf("") }

    val clientesCadastradosMock = remember { mutableStateOf(listOf("Cliente A", "Cliente B", "Cliente C")) }

    when (etapaAtual) {
        1 -> ComponentAtendimentoEtapa1(
            data = dataAtendimento,
            onDataChange = { dataAtendimento = it },
            horario = horarioAtendimento,
            onHorarioChange = { horarioAtendimento = it },
            cancelado = cancelado,
            onCanceladoChange = { cancelado = it },
            clienteSelecionado = clienteSelecionado,
            onClienteSelecionadoChange = {
                clienteSelecionado = it
                if (!it.isNullOrEmpty()) {
                    nomeCliente = it
                    telefoneCliente = "123456789"
                    emailCliente = "cliente@email.com"
                    dataNascimentoCliente = "01/01/2000"
                    cpfCliente = "111.222.333-44"
                } else {
                    nomeCliente = ""
                    telefoneCliente = ""
                    emailCliente = ""
                    dataNascimentoCliente = ""
                    cpfCliente = ""
                }
            },
            nomeCliente = nomeCliente,
            onNomeClienteChange = { nomeCliente = it },
            telefoneCliente = telefoneCliente,
            onTelefoneClienteChange = { telefoneCliente = it },
            emailCliente = emailCliente,
            onEmailClienteChange = { emailCliente = it },
            dataNascimentoCliente = dataNascimentoCliente,
            onDataNascimentoClienteChange = { dataNascimentoCliente = it },
            cpfCliente = cpfCliente,
            onCpfClienteChange = { cpfCliente = it },
            onProximoClick = { etapaAtual = 2 },
            clientesCadastrados = clientesCadastradosMock.value
        )
        2 -> ComponentAtendimentoEtapa2(
            produtosUtilizados = produtosUtilizados,
            onProdutoUtilizadoChange = { produto, selecionado ->
                produtosUtilizados = if (selecionado) {
                    produtosUtilizados + produto
                } else {
                    produtosUtilizados - produto
                }
            },
            onAnteriorClick = { etapaAtual = 1 },
            onProximoClick = { etapaAtual = 3 }
        )
        3 -> ComponentAtendimentoEtapa3(
            valorTatuagem = valorTatuagem,
            onValorTatuagemChange = { valorTatuagem = it },
            dataAtendimento = dataAtendimento,
            onDataAtendimentoChange = { dataAtendimento = it },
            horarioAtendimento = horarioAtendimento,
            onHorarioAtendimentoChange = { horarioAtendimento = it },
            onAnteriorClick = { etapaAtual = 2 },
            onProximoClick = { etapaAtual = 4 }
        )
        4 -> ComponentAtendimentoEtapa4(
            valorTatuagem = valorTatuagem,
            dataAtendimento = dataAtendimento,
            horarioAtendimento = horarioAtendimento,
            onAnteriorClick = { etapaAtual = 3 },
            onConfirmarClick = { showConfirmacao = true }
        )
    }

    if (showConfirmacao) {
        ComponentConfirmacaoAtendimento(
            onConfirmar = {
                viewModel.criarAtendimento(
                    dataAtendimento = dataAtendimento,
                    horarioAtendimento = horarioAtendimento,
                    cancelado = cancelado,
                    clienteSelecionado = clienteSelecionado,
                    nomeCliente = nomeCliente,
                    telefoneCliente = telefoneCliente,
                    emailCliente = emailCliente,
                    dataNascimentoCliente = dataNascimentoCliente,
                    cpfCliente = cpfCliente,
                    produtosUtilizados = produtosUtilizados,
                    valorTatuagem = valorTatuagem,
                    onSucesso = {
                        showConfirmacao = false
                        etapaAtual = 1
                        dataAtendimento = ""
                        horarioAtendimento = ""
                        cancelado = false
                        clienteSelecionado = null
                        nomeCliente = ""
                        telefoneCliente = ""
                        emailCliente = ""
                        dataNascimentoCliente = ""
                        cpfCliente = ""
                        produtosUtilizados = emptyList()
                        valorTatuagem = ""
                    },
                    onError = { erro ->
                        println("Erro ao criar atendimento: $erro")
                    }
                )
            },
            onCancelar = { showConfirmacao = false }
        )
    }
}
