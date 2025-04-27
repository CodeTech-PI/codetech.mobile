package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.CampoFilial
import com.example.code_mobile.paginas.code_mobile.CampoFilialStatus
import com.example.code_mobile.paginas.code_mobile.viewModel.ViewModelFiliais
import com.example.code_mobile.ui.theme.CodemobileTheme


import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FiliaisCadastro(navController: NavController, viewModel: ViewModelFiliais = viewModel()) {
    // Usando os estados da ViewModel diretamente
    val novoFilial = viewModel.novaFilial
    val cepError = viewModel.cepError
    val lagradouroError = viewModel.lagradouroError
    val bairroError = viewModel.bairroError
    val cidadeError = viewModel.cidadeError
    val estadoError = viewModel.estadoError
    val numError = viewModel.numError
    val operacaoSucesso by viewModel.operacaoSucesso.collectAsState()
    val mensagemErro by viewModel.mensagemErro.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    // Efeito colateral para navegar após o cadastro bem-sucedido
    LaunchedEffect(operacaoSucesso) {
        if (operacaoSucesso) {
            viewModel.resetOperacaoSucesso()
            navController.navigate("Filiais") {
                popUpTo("FiliaisCadastro") { inclusive = true } // Evita voltar para a tela de cadastro
            }
        }
    }

    // Efeito colateral para exibir mensagens de erro
    LaunchedEffect(mensagemErro) {
        mensagemErro?.let {
            println("Erro ao cadastrar: $it") // Substitua por uma forma de feedback ao usuário (Snackbar, etc.)
            viewModel.limparMensagemDeErro()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFF1B1B1B))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Cadastrar",
            style = textPadrao.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        CampoFilial(
            titulo = "CEP:",
            valor = novoFilial.cep,
            onValorChange = viewModel::atualizarCep,
            textStyle = textPadrao,
            placeholderText = "Digite o CEP",
        )
        if (cepError != null) {
            if (estadoError != null) {
                Text(text = estadoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Logradouro:",
            valor = novoFilial.lagradouro,
            onValorChange = viewModel::atualizarLagradouro,
            textStyle = textPadrao,
            placeholderText = "Digite o Logradouro",
        )
        if (lagradouroError != null) {
            if (estadoError != null) {
                Text(text = estadoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Bairro:",
            valor = novoFilial.bairro,
            onValorChange = viewModel::atualizarBairro,
            textStyle = textPadrao,
            placeholderText = "Digite o Bairro",
        )
        if (bairroError != null) {
            if (estadoError != null) {
                Text(text = estadoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Cidade:",
            valor = novoFilial.cidade,
            onValorChange = viewModel::atualizarCidade,
            textStyle = textPadrao,
            placeholderText = "Digite a Cidade",
        )
        if (cidadeError != null) {
            if (estadoError != null) {
                Text(text = estadoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Estado:",
            valor = novoFilial.estado,
            onValorChange = viewModel::atualizarEstado,
            textStyle = textPadrao,
            placeholderText = "Ex: SP",
        )
        if (estadoError != null) {
            Text(text = estadoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Complemento:",
            valor = novoFilial.complemento,
            onValorChange = viewModel::atualizarComplemento,
            textStyle = textPadrao,
            placeholderText = "Digite o Complemento (opcional)",
        )
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Número:",
            valor = novoFilial.num.toString(),
            onValorChange = { viewModel.atualizarNum(it) },
            textStyle = textPadrao,
            placeholderText = "Digite o Número",
        )
        if (numError != null) {
            if (estadoError != null) {
                Text(text = estadoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    focusManager.clearFocus() // Remove o foco de qualquer campo de texto
                    viewModel.criarFilial()
                },
                modifier = Modifier
                    .width(130.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                enabled = !isLoading // Desabilita o botão enquanto carrega
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp)
                } else {
                    Text(text = "Salvar")
                }
            }

            Button(
                onClick = { navController.popBackStack() }, // Use popBackStack para voltar
                modifier = Modifier
                    .width(130.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF252525))
            ) {
                Text(text = "Cancelar")
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
fun GreetingPreview() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        FiliaisCadastro(navController)  // Passe o navController para TelaLogin
    }
}
