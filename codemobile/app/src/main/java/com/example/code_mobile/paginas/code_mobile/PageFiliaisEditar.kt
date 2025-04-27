package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.viewModel.ViewModelFiliais
import com.example.code_mobile.ui.theme.CodemobileTheme


@Composable
fun FiliaisEditar(navController: NavController, viewModel: ViewModelFiliais = viewModel()) {
    // Acessa a filial em edição da ViewModel
    val filialEmEdicao = viewModel.filialEmEdicao
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

    // Efeito colateral para navegar após a edição bem-sucedida
    LaunchedEffect(operacaoSucesso) {
        if (operacaoSucesso) {
            viewModel.resetOperacaoSucesso()
            navController.navigate("Filiais") {
                popUpTo("FiliaisEditar") { inclusive = true } // Evita voltar para a tela de edição
            }
        }
    }

    // Efeito colateral para exibir mensagens de erro
    LaunchedEffect(mensagemErro) {
        mensagemErro?.let {
            println("Erro ao editar: $it") // Substitua por uma forma de feedback ao usuário
            viewModel.limparMensagemDeErro()
        }
    }

    // Se filialEmEdicao for nulo, talvez mostrar uma tela de carregamento ou erro
    if (filialEmEdicao == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1B1B1B)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else if (mensagemErro != null) {
                Text(text = "Erro ao carregar detalhes da filial.", color = Color.Red)
            } else {
                Text(text = "Nenhuma filial selecionada para editar.", color = Color.Gray)
            }
        }
        return
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
            text = "Editar",
            style = textPadrao.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        CampoFilialStatus(
            titulo = "Status:",
            valor = if (filialEmEdicao.estado.isNotEmpty()) "Operante" else "Inoperante", // Adapte conforme a lógica do seu status
            onValorChange = { /* TODO: Implementar atualização do status na ViewModel */ },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(20.dp))

        CampoFilial(
            titulo = "CEP:",
            valor = filialEmEdicao.cep,
            onValorChange = viewModel::atualizarCepEdicao,
            textStyle = textPadrao,
            placeholderText = "Digite o CEP"
        )
        if (cepError != null) {
            Text(text = cepError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Logradouro:",
            valor = filialEmEdicao.lagradouro,
            onValorChange = viewModel::atualizarLagradouroEdicao,
            textStyle = textPadrao,
            placeholderText = "Digite o Logradouro"
        )
        if (lagradouroError != null) {
            Text(text = lagradouroError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Bairro:",
            valor = filialEmEdicao.bairro,
            onValorChange = viewModel::atualizarBairroEdicao,
            textStyle = textPadrao,
            placeholderText = "Digite o Bairro"
        )
        if (bairroError != null) {
            Text(text = bairroError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Cidade:",
            valor = filialEmEdicao.cidade,
            onValorChange = viewModel::atualizarCidadeEdicao,
            textStyle = textPadrao,
            placeholderText = "Digite a Cidade"
        )
        if (cidadeError != null) {
            Text(text = cidadeError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Estado:",
            valor = filialEmEdicao.estado,
            onValorChange = viewModel::atualizarEstadoEdicao,
            textStyle = textPadrao,
            placeholderText = "Ex: SP"
        )
        if (estadoError != null) {
            Text(text = estadoError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Complemento:",
            valor = filialEmEdicao.complemento,
            onValorChange = viewModel::atualizarComplementoEdicao,
            textStyle = textPadrao,
            placeholderText = "Digite o Complemento (opcional)"
        )
        Spacer(modifier = Modifier.height(10.dp))

        CampoFilial(
            titulo = "Número:",
            valor = filialEmEdicao.num.toString(),
            onValorChange = { viewModel.atualizarNumEdicao(it) },
            textStyle = textPadrao,
            placeholderText = "Digite o Número"
        )
        if (numError != null) {
            Text(text = numError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
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
                    focusManager.clearFocus()
                    filialEmEdicao?.let { viewModel.atualizarFilial(it.num) }
                },
                modifier = Modifier
                    .width(130.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp)
                } else {
                    Text(text = "Salvar")
                }
            }

            Button(
                onClick = { navController.popBackStack() },
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
fun FiliaisEditarPreview() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        FiliaisEditar(navController)
    }
}
