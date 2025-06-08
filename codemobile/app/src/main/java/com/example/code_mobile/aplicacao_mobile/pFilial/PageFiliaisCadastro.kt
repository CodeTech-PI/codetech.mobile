package com.example.code_mobile.paginas.code_mobile.pFilial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.CampoFilial
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelFilial
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.delay

@Composable
fun FiliaisCadastro(    navController: NavController,
    modifier: Modifier = Modifier
) {

    val viewModelFilial: ViewModelFilial = viewModel()

    val scrollState = rememberScrollState()

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    val cadastroSucesso by viewModelFilial.cadastroSucesso.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    val showLoading by viewModelFilial.showLoading.collectAsState()
    val mensagemErro by viewModelFilial.mensagemErro.collectAsState()

    LaunchedEffect(cadastroSucesso) {
        if (cadastroSucesso) {
            delay(3000)
            navController.navigate("Filiais")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = modifier
                .fillMaxHeight(0.75f)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFF1B1B1B))
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento padrão
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigate("Filiais") }
                )
                Text(
                    text = "Cadastrar Filial",
                    style = com.example.code_mobile.paginas.code_mobile.textPadrao.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(24.dp))
            }

            Spacer(modifier = Modifier.height(5.dp))

            CampoFilial(
                titulo = "CEP:",
                valor = viewModelFilial.cep.value,
                onValorChange = { viewModelFilial.atualizarCep(it) },
                erro = viewModelFilial.cepError.value ?: "",
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: 01234-567"
            )

            CampoFilial(
                titulo = "Logradouro:",
                valor = viewModelFilial.logradouro.value,
                onValorChange = { viewModelFilial.atualizarlogradouro(it) },
                erro = viewModelFilial.logradouroError.value ?: "",
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: Rua Exemplo"
            )

            CampoFilial(
                titulo = "Bairro:",
                valor = viewModelFilial.bairro.value,
                onValorChange = { viewModelFilial.atualizarBairro(it) },
                erro = viewModelFilial.bairroError.value ?: "",
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: Jardim Exemplo"
            )

            CampoFilial(
                titulo = "Cidade:",
                valor = viewModelFilial.cidade.value,
                onValorChange = { viewModelFilial.atualizarCidade(it) },
                erro = viewModelFilial.cidadeError.value ?: "",
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex São Paulo"
            )

            CampoFilial(
                titulo = "Estado:",
                valor = viewModelFilial.estado.value,
                onValorChange = { viewModelFilial.atualizarEstado(it) },
                textStyle = textPadrao.copy(fontSize = 16.sp),
                erro = viewModelFilial.estadoError.value ?: "",
                placeholderText = "Ex: SP"
            )

            CampoFilial(
                titulo = "Número:",
                valor = viewModelFilial.num.value,
                onValorChange = { viewModelFilial.atualizarNum(it) },
                textStyle = textPadrao.copy(fontSize = 16.sp),
                erro = viewModelFilial.numError.value ?: "",
                placeholderText = "Ex: 123"
            )

            CampoFilial(
                titulo = "Complemento:",
                valor = viewModelFilial.complemento.value,
                onValorChange = { viewModelFilial.atualizarComplemento(it) },
                erro = viewModelFilial.complementoError.value ?: "",
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: Apartamento"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = viewModelFilial::cadastrarFilial,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp), // Altura menor
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                    enabled = !showLoading
                ) {
                    Text(
                        text = if (showLoading) "Carregando..." else "Cadastrar",
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = viewModelFilial::cadastrarFilial,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp), // Altura menor
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                    enabled = !showLoading
                ) {
                    Text(
                        text = if (showLoading) "Carregando..." else "Cadastrar",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

            }

            // Exibe mensagem de sucesso
            if (cadastroSucesso) {
                Dialog(
                    onDismissRequest = { }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f) // Largura um pouco menor
                            .clip(RoundedCornerShape(8.dp)) // Bordas menos arredondadas
                            .background(Color(0xFF2B2B2B))
                            .padding(12.dp), // Padding menor
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento menor
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Sucesso",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(40.dp) // Ícone menor
                            )
                            Text(
                                "Cadastrado!", // Mensagem mais curta
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp // Fonte menor
                            )
                            Text(
                                "Redirecionando...",
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp // Fonte menor
                            )

                            Button(
                                onClick = {
                                    navController.navigate("Filiais")
                                },
                                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
                            ) {
                                Text("Ok", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }

            //Exibe confirmar cancelamento
            if (showCancelDialog) {
                AlertDialog(
                    onDismissRequest = { showCancelDialog = false },
                    title = {
                        Text(
                            "Confirmar Cancelamento",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }, // Fonte menor
                    text = {
                        Text(
                            "Deseja mesmo abandonar as alterações?",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }, // Fonte menor
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { showCancelDialog = false },
                                colors = ButtonDefaults.buttonColors(Color.Gray),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text("Não", color = Color.White, fontSize = 14.sp) // Fonte menor
                            }

                            Button(
                                onClick = {
                                    showCancelDialog = false
                                    navController.navigate("Filiais")
                                },
                                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text("Sim", color = Color.White, fontSize = 14.sp) // Fonte menor
                            }

                        }
                    },
                    containerColor = Color(0xFF2B2B2B)
                )
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
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)

@Composable
fun PreviewFiliaisCadastro() {
    CodemobileTheme {
        val navController = rememberNavController()
        val viewModel = ViewModelFilial()
        FiliaisCadastro(navController)
    }
}
