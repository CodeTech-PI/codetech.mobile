package com.example.code_mobile.paginas.code_mobile.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.paginas.code_mobile.viewModel.cliente.ViewModelCliente
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteCadastro(navController: NavController, modifier: Modifier = Modifier) {

    val viewModel: ViewModelCliente = viewModel()

    val nome by viewModel.nome
    val cpf by viewModel.cpf
    val dataNasc by viewModel.dataNasc
    val telefone by viewModel.telefone
    val email by viewModel.email

    val nomeError by viewModel.nomeError
    val cpfError by viewModel.cpfError
    val dataNascError by viewModel.dataNascError
    val telefoneError by viewModel.telefoneError
    val emailError by viewModel.emailError

    val cadastroSucesso by viewModel.cadastroSucesso.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val mensagemErroBackend by viewModel.mensagemErro.collectAsState()


    val scrollState = rememberScrollState()
    var showCancelDialog by remember { mutableStateOf(false) }
    var exibirCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()


    LaunchedEffect(cadastroSucesso) {
        if (cadastroSucesso) {
            delay(3000)
            navController.navigate("Clientes")
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
                .fillMaxHeight(0.90f)
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
                        .clickable { navController.navigate("Clientes") }
                )
                Text(
                    text = "Cadastrar Cliente",
                    style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(24.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Data de Nascimento:",
                    style = textPadrao.copy(fontSize = 16.sp, color = Color.White)
                )
                Button(
                    onClick = { exibirCalendario = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050))
                ) {
                    Text(text = if (dataNasc.isEmpty()) "Selecionar" else dataNasc, color = Color.White)
                }
            }

            CampoCadastrarCliente(
                titulo = "Nome:",
                valor = nome,
                onValorChange = viewModel::atualizarNome,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: Letícia Lombardi",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                errorMessage = nomeError
            )

            CampoCadastrarCliente(
                titulo = "CPF:",
                valor = cpf,
                onValorChange = viewModel::atualizarCpf,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: 890.623.227-08",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                errorMessage = cpfError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = CpfVisualTransformation()
            )

            if (exibirCalendario) {
                DatePickerDialog(
                    onDismissRequest = { exibirCalendario = false },
                    confirmButton = {
                        TextButton(onClick = {
                            exibirCalendario = false
                            datePickerState.selectedDateMillis?.let {
                                viewModel.atualizarDataNasc(
                                    Instant.ofEpochMilli(it)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate().format(viewModel.dateFormatter)
                                )
                            }
                        }) {
                            Text("Confirmar", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { exibirCalendario = false }) {
                            Text("Cancelar", color = Color.White)
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            CampoCadastrarCliente(
                titulo = "Telefone:",
                valor = telefone,
                onValorChange = viewModel::atualizarTelefone,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: (11) 98765-4321",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                errorMessage = telefoneError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            CampoCadastrarCliente(
                titulo = "E-mail:",
                valor = email,
                onValorChange = viewModel::atualizarEmail,
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: leticia@lombardi.com",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                errorMessage = emailError
            )


            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = viewModel::cadastrarCliente,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp), // Altura menor
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                    enabled = !showLoading
                ) {
                    Text(
                        text = if (showLoading) "Salvando..." else "Salvar",
                        fontSize = 14.sp
                    ) // Fonte menor
                }

                Button(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(40.dp), // Altura menor
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF252525))
                ) {
                    Text(text = "Cancelar", fontSize = 14.sp) // Fonte menor
                }
            }
        }

        // Popup de confirmação de cancelamento
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
                            onClick = {
                                showCancelDialog = false
                                navController.navigate("Clientes")
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("Sim", color = Color.White, fontSize = 14.sp) // Fonte menor
                        }

                        Button(
                            onClick = { showCancelDialog = false },
                            colors = ButtonDefaults.buttonColors(Color.Gray),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("Não", color = Color.White, fontSize = 14.sp) // Fonte menor
                        }
                    }
                },
                containerColor = Color(0xFF2B2B2B)
            )
        }

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
                    }
                }
            }
        }

        mensagemErroBackend?.let { erro ->
            AlertDialog(
                onDismissRequest = { viewModel.limparMensagemDeErro() },
                title = { Text("Erro", color = Color.White, fontSize = 16.sp) }, // Fonte menor
                text = { Text(erro, color = Color.White, fontSize = 14.sp) }, // Fonte menor
                confirmButton = {
                    TextButton(onClick = { viewModel.limparMensagemDeErro() }) {
                        Text("OK", color = Color.White, fontSize = 14.sp) // Fonte menor
                    }
                },
                containerColor = Color(0xFF2B2B2B)
            )
        }

        if (showLoading) {
            Dialog(onDismissRequest = { }) {
                Box(
                    modifier = Modifier
                        .size(80.dp) // Tamanho menor
                        .clip(RoundedCornerShape(12.dp)) // Bordas menos arredondadas
                        .background(Color(0xFF2B2B2B)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp
                    ) // Barra menor
                }
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
fun ClienteCadastroPreview() {
    CodemobileTheme {
        val navController = rememberNavController()
        ClienteCadastro(navController)
    }
}