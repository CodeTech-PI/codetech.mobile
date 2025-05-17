package com.example.code_mobile.aplicacao_mobile.pAgendamento

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.code_mobile.aplicacao_mobile.cModel.ModelAgendamento
import com.example.code_mobile.aplicacao_mobile.cViewModel.ViewModelAgendamento
import com.example.code_mobile.aplicacao_mobile.cViewModel.ViewModelOrdemServico
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.paginas.code_mobile.cViewModel.ViewModelCliente
import com.example.code_mobile.paginas.code_mobile.pCliente.CampoCadastrarCliente
import com.example.code_mobile.paginas.code_mobile.pCliente.ClienteCadastro
import com.example.code_mobile.paginas.code_mobile.pCliente.CpfVisualTransformation
import com.example.code_mobile.paginas.code_mobile.pCliente.cardCliente
import com.example.code_mobile.ui.theme.CodemobileTheme
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendamentoEtapa3(
    navController: NavController,
    modifier: Modifier = Modifier,
    agendamentoId: Int,

) {

    // Precisa testar p ver se o backend ta funcionando
    // Na teoria ta certo, mas acho que ta com problema no botão de "próximo"
    // Talvez a rota esteja errada, precisa validar. gemini ajuda demais

    val viewModelOrdemServico: ViewModelOrdemServico = viewModel()
    var valorTatuagem by remember { mutableStateOf("") }
    val ordemServicoCriada = viewModelOrdemServico.ordemServico.collectAsState().value
    val isLoadingOrdemServico by viewModelOrdemServico.isLoading.collectAsState()
    val mensagemOrdemServico by viewModelOrdemServico.mensagem.collectAsState()
    val erroOrdemServico by viewModelOrdemServico.erro.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = mensagemOrdemServico) {
        mensagemOrdemServico?.let {
            println("Ordem de Serviço: $it")
            viewModelOrdemServico.resetFeedback()
            navController.navigate("AgendamentoCadastro4/$agendamentoId")
        }
    }

    LaunchedEffect(key1 = erroOrdemServico) {
        erroOrdemServico?.let {
            println("Erro ao criar ordem de serviço: $it")
            viewModelOrdemServico.resetFeedback()
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
                .background(Color(0xFF1B1B1B))
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = "Valor",
                    style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(24.dp))
            }

            CampoCadastrarOrdem(
                titulo = "Valor da tatuagem:",
                valor = valorTatuagem,
                onValorChange = { valorTatuagem = it },
                textStyle = textPadrao.copy(fontSize = 16.sp),
                placeholderText = "Ex: 100.00",
                tituloStyle = textPadrao.copy(fontSize = 16.sp),
                isError = false,
                errorMessage = ""
            )
        }

        // Row dos botões fixada na parte inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = { showCancelDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF252525))
            ) {
                Text(text = "Voltar", fontSize = 14.sp, color = Color.White)
            }

            Button(
                onClick = {
                    val valor = valorTatuagem.toBigDecimalOrNull()
                    if (valor != null) {
                        viewModelOrdemServico.cadastrarOrdemServico(
                            valorTatuagem = valor,
                            agendamentoId = agendamentoId,
                            onSucesso = {
                                navController.navigate("AgendamentoCadastro4/$agendamentoId")
                            }
                        )
                    } else {
                        println("Valor da tatuagem inválido.")
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050)),
                enabled = !isLoadingOrdemServico && valorTatuagem.isNotBlank()
            ) {
                Text(
                    text = if (isLoadingOrdemServico) "Carregando..." else "Próximo",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        if (isLoadingOrdemServico) {
            Dialog(onDismissRequest = { }) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2B2B2B)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
            }
        }

        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = { Text("Cancelar valor da tatuagem?", color = Color.White) },
                text = {
                    Text(
                        "Tem certeza que deseja voltar para a seleção de produtos?",
                        color = Color.White
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
                    ) {
                        Text("Sim", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showCancelDialog = false },
                        colors = ButtonDefaults.buttonColors(Color(0xFF252525))
                    ) {
                        Text("Não", color = Color.White)
                    }
                },
                containerColor = Color(0xFF2B2B2B)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun AgendamentoEtapa3Preview() {
    CodemobileTheme {
        val navController = rememberNavController()
        AgendamentoEtapa3(navController = navController, agendamentoId = 1)
    }
}