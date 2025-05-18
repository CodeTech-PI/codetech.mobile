package com.example.code_mobile.aplicacao_mobile.pAgendamento

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.textPadrao
import com.example.code_mobile.ui.theme.CodemobileTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.code_mobile.aplicacao_mobile.cViewModel.ViewModelAgendamento
import com.example.code_mobile.aplicacao_mobile.cViewModel.ViewModelFaturamento
import com.example.code_mobile.aplicacao_mobile.cViewModel.ViewModelOrdemServico
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun AgendamentoEtapa4(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModelAgendamento: ViewModelAgendamento,
    agendamentoId: Int
) {
    val viewModelOrdemServico: ViewModelOrdemServico = viewModel()
    val viewModelFaturamento: ViewModelFaturamento = viewModel();
    val dataAgendamento by viewModelAgendamento.dataAgendamento
    val horarioAgendamento by viewModelAgendamento.horarioAgendamento
    val valorTatuagem by viewModelAgendamento.valorTatuagem;
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val isLoadingFaturamento by viewModelFaturamento.isLoadingFaturamento.collectAsState();
    val showModalSucessoFaturamento by viewModelFaturamento.sucessoCarregarFaturamento.collectAsState();
    val erroFaturamento by viewModelFaturamento.erroCarregarFaturamento.collectAsState()

    val faturamento by viewModelFaturamento.faturamento.collectAsState()


    var mostrarDialogo by remember { mutableStateOf(false) }
    var mostrarModalSucesso by remember { mutableStateOf(false) }
    var sucessoCadastrarOrdem by remember { mutableStateOf(false) }
    val valor = valorTatuagem

    val confirmarFaturamento: () -> Unit = {
        if (valor != null) {
            viewModelOrdemServico.cadastrarOrdemServico(
                valorTatuagem = valor,
                agendamentoId = agendamentoId,
                onSucesso = { ordemCriada ->
                    if (ordemCriada != null) {
                        println("Ordem criada com sucesso! ID: ${ordemCriada.id}")
                        viewModelFaturamento.carregarFaturamento(ordemCriada.id);
                        sucessoCadastrarOrdem = true;

                    } else {
                        println("Erro: ordem criada veio nula.")
                    }
                }
            )
        } else {
            println("Valor da tatuagem inválido.")
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
                    text = "Atendimento",
                    style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(24.dp))
            }

            Text(
                text = "Confirmar informações",
                style = textPadrao.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = modifier
                .fillMaxWidth(0.75f)
                .fillMaxHeight(0.40f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Valor da Tatuagem:", style = textPadrao.copy(fontSize = 16.sp))
            Text(
                text = "${ NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorTatuagem)}",
                style = textPadrao.copy(fontSize = 16.sp),
                color = Color.White
            )

            Text("Data do atendimento:", style = textPadrao.copy(fontSize = 16.sp))
            Text(
                text = dataAgendamento?.format(formatter)?.toString() ?: "-",
                style = textPadrao.copy(fontSize = 16.sp),
                color = Color.White
            )

            Text("Horário do atendimento:", style = textPadrao.copy(fontSize = 16.sp))
            Text(
                text = horarioAgendamento?.toString() ?: "-",
                style = textPadrao.copy(fontSize = 16.sp),
                color = Color.White
            )
        }

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
                onClick = { navController.popBackStack() },
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
                onClick = { mostrarDialogo = true },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050))
            ) {
                Text(text = "Confirmar", fontSize = 14.sp, color = Color.White)
            }
        }

        if (mostrarDialogo) {
            ConfirmarFaturamentoDialog(
                onDismiss = { mostrarDialogo = false },
                onConfirmar = {
                    confirmarFaturamento()
                    mostrarDialogo = false
                }
            )
        }

        if (showModalSucessoFaturamento && !isLoadingFaturamento) {
            mostrarModalSucesso = true;
            var valor = faturamento?.lucro
            ModalSucessoOrdemServico(
                navController = navController,
                onClose = { mostrarModalSucesso = false },
                valorLucro = valor ?: BigDecimal.ZERO,
                erroLucro = false
            )
        }
        if ((erroFaturamento?.length ?: 0) > 0 && !isLoadingFaturamento && !showModalSucessoFaturamento) {
            mostrarModalSucesso = true;
            ModalSucessoOrdemServico(
                navController = navController,
                onClose = { mostrarModalSucesso = false },
                valorLucro = BigDecimal.ZERO,
                erroLucro = true
            )
        }
        if (isLoadingFaturamento) {
            ModalLoadingFaturamento()
        }
    }
}


@Composable
fun ModalSucessoOrdemServico(
    navController: NavController,
    onClose: () -> Unit,
    valorLucro: BigDecimal,
    erroLucro: Boolean
) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2B2B2B))
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // X de fechar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable { onClose()
                                navController.navigate("Agendamentos") {
                                    popUpTo("Agendamentos") { inclusive = true }
                                }
                            }
                            .size(24.dp)
                    )
                }

                Text(
                    text = "Ordem de Serviço",
                    style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                    color = Color(0xFFDF0050)
                )

                Text(
                    text = "Atendimento realizado com sucesso!",
                    style = textPadrao.copy(fontSize = 16.sp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))



                if (!erroLucro) {
                    Text(
                        text = "Lucro Estimado",
                        style = textPadrao.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                        color = Color.White
                    )
                    Text(
                        text = "${ NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorLucro)}",
                        style = textPadrao.copy(fontSize = 16.sp),
                        color = Color.White
                    )
                }

            }
        }
    }
}


@Composable
fun ModalLoadingFaturamento() {
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


@Composable
fun ConfirmarFaturamentoDialog(
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Faturamento", color = Color.White) },
        text = {
            Text(
                "Tem certeza que deseja gerar a ordem de serviço?",
                color = Color.White
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onConfirmar()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Sim", color = Color.White)
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(Color.Gray),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Não", color = Color.White)
                }
            }
        },
        containerColor = Color(0xFF2B2B2B)
    )
}


@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun AgendamentoEtapa4Preview() {
    CodemobileTheme {
        val navController = rememberNavController()
        AgendamentoEtapa4(navController = navController, viewModelAgendamento = ViewModelAgendamento(), agendamentoId = 1,  )
    }
}