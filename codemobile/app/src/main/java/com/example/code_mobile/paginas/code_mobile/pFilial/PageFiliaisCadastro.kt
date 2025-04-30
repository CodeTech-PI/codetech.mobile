package com.example.code_mobile.paginas.code_mobile.pFilial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.CampoFilial
import com.example.code_mobile.paginas.code_mobile.cViewModel.filial.ViewModelFilial
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun FiliaisCadastro(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModelFilial: ViewModelFilial = viewModel() // <- Usa a ViewModel
) {
    val scrollState = rememberScrollState()

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    val cadastroSucesso by viewModelFilial.cadastroSucesso.collectAsState()
    val showLoading by viewModelFilial.showLoading.collectAsState()
    val mensagemErro by viewModelFilial.mensagemErro.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFF1B1B1B))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Cadastrar Filial",
            style = textPadrao.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        CampoFilial(
            titulo  = "CEP",
            valor  = viewModelFilial.cep.value,
            onValorChange  = { viewModelFilial.atualizarCep(it) },
            erro = viewModelFilial.cepError.value ?: "",
            textStyle = textPadrao
        )

        CampoFilial(
            titulo  = "Logradouro",
            valor  = viewModelFilial.logradouro.value,
            onValorChange  = { viewModelFilial.atualizarlogradouro(it) },
            erro = viewModelFilial.logradouroError.value ?: "",
            textStyle = textPadrao
        )

        CampoFilial(
            titulo  = "Bairro",
            valor  = viewModelFilial.bairro.value,
            onValorChange  = { viewModelFilial.atualizarBairro(it) },
            erro = viewModelFilial.bairroError.value ?: "",
            textStyle = textPadrao
        )

        CampoFilial(
            titulo  = "Cidade",
            valor  = viewModelFilial.cidade.value,
            onValorChange  = { viewModelFilial.atualizarCidade(it) },
            erro = viewModelFilial.cidadeError.value ?: "",
            textStyle = textPadrao
        )

        CampoFilial(
            titulo  = "Estado",
            valor  = viewModelFilial.estado.value,
            onValorChange  = { viewModelFilial.atualizarEstado(it) },
            textStyle = textPadrao,
            erro = viewModelFilial.estadoError.value ?: "",
        )

        CampoFilial(
            titulo  = "Número",
            valor  = viewModelFilial.num.value,
            onValorChange  = { viewModelFilial.atualizarNum(it) },
            textStyle = textPadrao,
            erro = viewModelFilial.numError.value ?: "",
        )

        CampoFilial(
            titulo  = "Complemento",
            valor  = viewModelFilial.complemento.value,
            onValorChange  = { viewModelFilial.atualizarComplemento(it) },
            erro = viewModelFilial.complementoError.value ?: "",
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { viewModelFilial.cadastrarFilial() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (showLoading) "Carregando..." else "Cadastrar",
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe mensagem de sucesso
        if (cadastroSucesso) {
            Text(
                text = "Filial cadastrada com sucesso!",
                color = Color.Green,
                fontSize = 18.sp
            )
            // Reseta sucesso depois de um tempo ou ação
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                viewModelFilial.resetarCadastroSucesso()
                navController.popBackStack() // Volta para tela anterior
            }
        }

        // Exibe mensagem de erro
        mensagemErro?.let { erro ->
            Text(
                text = erro,
                color = Color.Red,
                fontSize = 18.sp
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewFiliaisCadastro() {
    CodemobileTheme {
        val navController = rememberNavController()
        val viewModel = ViewModelFilial()
        FiliaisCadastro(navController = navController, viewModelFilial = viewModel)
    }
}
