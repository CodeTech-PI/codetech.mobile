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
import androidx.compose.material3.OutlinedTextField
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
fun AgendamentoEtapa1(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModelAgendamento: ViewModelAgendamento
) {
    val viewModelCliente: ViewModelCliente = viewModel()

    val dataAgendamento by viewModelAgendamento.dataAgendamento
    val horarioAgendamento by viewModelAgendamento.horarioAgendamento
    val clienteSelecionado by viewModelAgendamento.clienteSelecionado
    val agendamentoIdCriado by viewModelAgendamento.agendamentoIdCriado.collectAsState()
    val agendamentoSucesso by viewModelAgendamento.agendamentoSucesso.collectAsState()
    val mensagemErroAgendamento by viewModelAgendamento.mensagemErro.collectAsState()
    val listaClientes by viewModelCliente.clientes.collectAsState()
    val showLoading by viewModelAgendamento.showLoading.collectAsState()

    var expandedCliente by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var exibirCalendario by remember { mutableStateOf(false) }
    var exibirCliente by remember { mutableStateOf(false) }


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dataAgendamento?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()?.toEpochMilli()
            ?: 0L
    )

    LaunchedEffect(Unit) {
        viewModelCliente.carregarClientes()
    }

    LaunchedEffect(agendamentoSucesso) {
        if (agendamentoSucesso && agendamentoIdCriado != null) {
            navController.navigate("AgendamentoCadastro2/${agendamentoIdCriado}")
            viewModelAgendamento.resetAgendamentoSucesso()
        }
    }

    LaunchedEffect(mensagemErroAgendamento) {
        mensagemErroAgendamento?.let {
            // TODO: Exibir erro com Snackbar ou Toast
            viewModelAgendamento.limparMensagemDeErro()
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
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigate("Agendamentos") }
                )
                Text(
                    text = "Cadastrar Agendamento",
                    style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(24.dp))
            }

            // Campo Data
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Data do Agendamento:",
                    style = textPadrao.copy(fontSize = 16.sp, color = Color.White)
                )
                Button(
                    onClick = { exibirCalendario = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050))
                ) {
                    Text(
                        text = dataAgendamento?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            ?: "Selecionar",
                        color = Color.White
                    )
                }
            }

            // Campo Horário
            OutlinedTextField(
                value = horarioAgendamento ?: "",
                onValueChange = {
                    viewModelAgendamento.atualizarHorarioAgendamento(it)
                },
                label = { Text("Horário (ex: 10:30)", color = Color.Gray) },
                placeholder = { Text("Ex: 10:30") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                textStyle = textPadrao.copy(fontSize = 16.sp, color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                isError = viewModelAgendamento.horarioAgendamentoError.value != null,
                supportingText = {
                    viewModelAgendamento.horarioAgendamentoError.value?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                }
            )

            // Dropdown Cliente
            Box {
                OutlinedTextField(
                    value = clienteSelecionado?.nome ?: "Selecione um cliente",
                    onValueChange = {},
                    label = { Text("Clientes", color = Color.Gray) },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = Color.White,
                            modifier = Modifier.clickable { expandedCliente = !expandedCliente }
                        )
                    },
                    textStyle = textPadrao.copy(fontSize = 16.sp, color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expandedCliente,
                    onDismissRequest = { expandedCliente = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listaClientes.forEach { cliente ->
                        DropdownMenuItem(
                            text = { Text(cliente.nome, color = Color.White) },
                            onClick = {
                                viewModelAgendamento.atualizarClienteSelecionado(cliente)
                                expandedCliente = false
                                exibirCliente = true
                            }
                        )
                    }
                }
            }

            // Card com informações do cliente selecionado
            if (exibirCliente && clienteSelecionado != null) {
                cardExibirCliente(
                    cliente = clienteSelecionado!!,
                    nome = "${clienteSelecionado?.nome}",
                    CPF = "CPF: ${clienteSelecionado?.cpf}",
                    telefone = "Telefone: ${clienteSelecionado?.telefone}"
                )
            }

        }

        // Botões inferiores
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
                Text("Cancelar", fontSize = 14.sp, color = Color.White)
            }

            Button(
                onClick = {
                    if (viewModelAgendamento.validarAgendamento()) {
                        clienteSelecionado?.let {
                            viewModelAgendamento.cadastrarAgendamento(
                                ModelAgendamento(
                                    id = null,
                                    dt = dataAgendamento?.toString().orEmpty(),
                                    horario = horarioAgendamento.orEmpty(),
                                    cancelado = false,
                                    usuario = it
                                )
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050)),
                enabled = !showLoading && clienteSelecionado != null
            ) {
                Text(
                    text = if (showLoading) "Carregando..." else "Próximo",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        // Loading Dialog
        if (showLoading) {
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

        // Alerta de cancelamento
        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = {
                    Text(
                        "Confirmar Cancelamento", color = Color.White,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        "Tem certeza que deseja cancelar o agendamento?",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showCancelDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("Não", color = Color.White, fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                showCancelDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text("Sim", color = Color.White, fontSize = 14.sp)
                        }
                    }
                },
                containerColor = Color(0xFF1B1B1B)
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
fun AgendamentoEtapa1Preview() {
    CodemobileTheme {
        val navController = rememberNavController()
        AgendamentoEtapa1(navController, viewModelAgendamento = ViewModelAgendamento())
    }
}