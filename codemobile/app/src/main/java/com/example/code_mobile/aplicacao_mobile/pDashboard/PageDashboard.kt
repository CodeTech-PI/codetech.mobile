package com.example.code_mobile.paginas.code_mobile.pDashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.aplicacao_mobile.cModel.ModelAgendamento
import com.example.code_mobile.aplicacao_mobile.cModel.ModelFaturamento
import com.example.code_mobile.paginas.code_mobile.cModel.ModelAgendamentoDash
import com.example.code_mobile.paginas.code_mobile.cModel.ModelFaturamentoDash
//import com.example.code_mobile.paginas.code_mobile.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.cModel.ModelProduto
import com.example.code_mobile.paginas.code_mobile.cService.ServiceDashboard
import com.example.code_mobile.paginas.code_mobile.cService.ServiceEstoque
import com.example.code_mobile.token.auth.AuthService
import com.example.code_mobile.token.auth.LoginRequest
import java.time.format.DateTimeFormatter
import com.example.code_mobile.token.network.RetrofithAuth
import java.time.LocalDate
import kotlin.math.log


@Composable
fun DashboardScreen(navController: NavController, modifier: Modifier = Modifier) {
//    val dashboardService = RetrofithAuth.retrofit.create(ServiceDashboard::class.java)
    val dashboardService = RetrofithAuth.dashboardService

    var showAlerts by remember { mutableStateOf(false) }

    var faturamentos by remember { mutableStateOf<List<ModelFaturamentoDash>>(emptyList()) }
    var produtos by remember { mutableStateOf<List<ModelProduto>>(emptyList()) }
    var agendamentos by remember { mutableStateOf<List<ModelAgendamentoDash>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            // Faturamentos
            val fatResponse = dashboardService.getFaturamentos()
            if(fatResponse.isSuccessful) {
                faturamentos = fatResponse.body() ?: emptyList()
            } else {
                error = "Erro ao buscar faturamentos: ${fatResponse.code()}"
            }

            // Produtos
            val prodResponse = dashboardService.getProdutos()
            if(prodResponse.isSuccessful) {
                produtos = prodResponse.body() ?: emptyList()
            } else {
                error = "Erro ao buscar produtos: ${prodResponse.code()}"
            }

            // Agendamentos
            val agendResponse = dashboardService.getAgendamentos()
            if(agendResponse.isSuccessful) {
                agendamentos = agendResponse.body() ?: emptyList()
            } else {
                error = "Erro ao buscar agendamentos: ${agendResponse.code()}"
            }

        } catch (e: Exception) {
            error = "Erro de conexão: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }


    val financialData = remember(faturamentos) {
        faturamentos.groupBy {
            // Converte a string "yyyy-MM-dd" para LocalDate e depois formata
            LocalDate.parse(it.ordemServico.agendamento.dt)
                .format(DateTimeFormatter.ofPattern("yyyy-MM"))
        }.map { (month, items) ->
            val lucro = items.sumOf { it.lucro.toDouble() }.toFloat()
            val bruto = items.sumOf { it.ordemServico.valorTatuagem.toDouble() }.toFloat()
            lucro to bruto
        }
    }

    val lowStockItems = remember(produtos) {
        produtos.filter { it.quantidade < 5 }
            .map { "${it.nome} - Estoque: ${it.quantidade}" }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
//        menuComTituloPage("Dashboard", navController)

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (!isLoading && error == null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiItem(
                    title = "Itens Baixo Estoque",
                    value = "${lowStockItems.size} itens",
                    color = Color(0xFF252525),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    showAlertIcon = true,
                    onAlertClick = { showAlerts = !showAlerts },
                    alerts = if (showAlerts) lowStockItems else emptyList()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiItem(
                        title = "Média Atend. Mensais",
                        value = "${agendamentos.size / 12}/mês",
                        color = Color(0xFF252525),
                        modifier = Modifier
                            .weight(1f)
                            .height(66.dp)
                    )

                    KpiItem(
                        title = "Lucro Anual",
                        value = "R$ ${"%.2f".format(financialData.sumOf { it.first.toDouble() })}",
                        color = Color(0xFF252525),
                        modifier = Modifier
                            .weight(1f)
                            .height(66.dp)
                    )
                }

                // Gráfico de Atendimentos Mensais
                ChartWithArrow(title = "Atendimentos Mensais") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        financialData.take(12).forEachIndexed { index, data ->
                            ChartBar(
                                height = (data.second * 0.002).dp,
                                label = "${index + 1}",
                                color = Color(0xFFDF0050)
                            )
                        }
                    }
                }

                // Gráfico de Itens em Estoque
                ChartWithArrow(title = "Itens em Estoque") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        produtos.take(6).forEach { produto ->
                            ChartBar(
                                height = (produto.quantidade * 10).dp,
                                label = produto.nome.take(3),
                                color = Color(0xFF4888B7)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KpiItem(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
    showAlertIcon: Boolean = false,
    onAlertClick: () -> Unit = {},
    alerts: List<String> = emptyList()
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (showAlertIcon) {
                IconButton(
                    onClick = onAlertClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Alertas",
                        tint = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (alerts.isEmpty()) {
                    Text(
                        text = value,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    )
                        {
                            alerts.forEach { alert ->
                                Text(
                                    text = alert,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun ChartWithArrow(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)))
        {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        }

}

@Composable
fun ChartBar(height: Dp, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(height)
                .background(
                    color = color,
                    shape = RoundedCornerShape(4.dp)
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    val navController = rememberNavController()
    DashboardScreen(navController)
}