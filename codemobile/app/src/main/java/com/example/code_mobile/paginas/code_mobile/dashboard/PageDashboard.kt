package com.example.code_mobile.paginas.code_mobile.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import com.example.code_mobile.paginas.code_mobile.menuComTituloPage
import com.example.code_mobile.paginas.code_mobile.model.ModelAgendamento
import com.example.code_mobile.paginas.code_mobile.model.ModelFaturamento
import com.example.code_mobile.paginas.code_mobile.model.ModelProduto
import com.example.code_mobile.paginas.code_mobile.service.ServiceDashboard
import com.example.code_mobile.token.network.RetrofithAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class MonthlyFinance(
    val month: String,
    val lucro: Float,
    val bruto: Float,
    val gastos: Float,
    val maxValue: Float
)

@Composable
fun DashboardScreen(navController: NavController, modifier: Modifier = Modifier) {
    var showFinancialChart by remember { mutableStateOf(false) }
    var showAlerts by remember { mutableStateOf(false) }

    var faturamentos by remember { mutableStateOf<List<ModelFaturamento>>(emptyList()) }
    var produtos by remember { mutableStateOf<List<ModelProduto>>(emptyList()) }
    var agendamentos by remember { mutableStateOf<List<ModelAgendamento>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            // Usa a instância do Retrofit configurada

            val faturamentosResponse = RetrofithAuth.dashboardService.getFaturamentos()
            val produtosResponse = RetrofithAuth.dashboardService.getProdutos()
            val agendamentosResponse = RetrofithAuth.dashboardService.getAgendamentos()

        } catch (e: Exception) {
            error = "Erro ao carregar dados: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    val financialData = remember(faturamentos) {
        faturamentos.groupBy {
            it.ordemServico.agendamento.dt.substring(0, 7)
        }.map { (month, items) ->
            val lucro = items.sumOf { it.lucro.toDouble() }.toFloat()
            val bruto = items.sumOf { it.ordemServico.valorTatuagem.toDouble() }.toFloat()
            MonthlyFinance(
                month = month,
                lucro = lucro,
                bruto = bruto,
                gastos = bruto - lucro,
                maxValue = maxOf(lucro, bruto))
        }.sortedBy { it.month }
    }

    val lowStockItems = remember(produtos) {
        produtos.filter { it.quantidade < 5 }
            .map { "${it.nome} - Estoque: ${it.quantidade} ${it.unidadeMedida}" }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        menuComTituloPage("Dashboard", navController)

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp))
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp))
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
                        value = "R$ ${"%.2f".format(financialData.sumOf { it.lucro.toDouble() })}",
                        color = Color(0xFF252525),
                        modifier = Modifier
                            .weight(1f)
                            .height(66.dp)
                    )
                }
            }

            if (showFinancialChart) {
                FinancialChart(
                    onBackClick = { showFinancialChart = false },
                    financialData = financialData
                )
            } else {
                ChartWithArrow(
                    title = "Atendimentos Mensais",
                    showIcon = true,
                    onArrowClick = { showFinancialChart = true },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        financialData.forEach { data ->
                            ChartBar(
                                height = (data.bruto * 2).dp,
                                label = data.month.takeLast(2),
                                color = Color(0xFFDF0050)
                            )
                        }
                    }
                }

                ChartWithArrow(
                    title = "Itens em Estoque",
                    showIcon = false,
                    onArrowClick = {}
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        produtos.take(4).forEach { produto ->
                            ChartBar(
                                height = (produto.quantidade * 10).dp,
                                label = produto.nome,
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
fun FinancialChart(
    onBackClick: () -> Unit,
    financialData: List<MonthlyFinance>
) {
    FinancialChartWithArrow(
        title = "Bruto x Gastos x Lucro por Mês",
        showIcon = true,
        onArrowClick = onBackClick
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            financialData.forEach { data ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = data.month,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    HorizontalBar("Lucro", data.lucro, data.maxValue, Color(0xFF2196F3))
                    HorizontalBar("Bruto", data.bruto, data.maxValue, Color(0xFF4CAF50))
                    HorizontalBar("Gastos", data.gastos, data.maxValue, Color(0xFFF44336))
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
        shape = RoundedCornerShape(12.dp) // Corrigido o fechamento do parâmetro shape
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // Adicionado o modificador .fillMaxSize()
        ) {
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
                    ) {
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
fun FinancialChartWithArrow(
    title: String,
    showIcon: Boolean,
    onArrowClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                if (showIcon) {
                    IconButton(onClick = onArrowClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Alternar gráfico",
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    }
}
@Composable
fun ChartWithArrow(
    title: String,
    showIcon: Boolean,
    onArrowClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                if (showIcon) {
                    IconButton(onClick = onArrowClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Alternar gráfico",
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}
@Composable
fun ChartBar(height: Dp, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            color = Color.Gray
        )
    }
}

@Composable
fun HorizontalBar(label: String, value: Float, maxValue: Float, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.height(20.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .width(70.dp)
                .padding(vertical = 0.dp),
            fontSize = 12.sp,
            color = Color.White
        )

        Box(
            modifier = Modifier
                .height(14.dp)
                .fillMaxWidth()
                .background(Color(0xFF424242), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value / maxValue)
                    .height(14.dp)
                    .background(color, RoundedCornerShape(4.dp)) // Fecha o background aqui
            ) // Fecha o Box interno aqui
        }

        Text(
            text = "R$ ${value.toInt()}K",
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 0.dp),
            color = Color.White
        )
    }
}

@Composable
fun AlertItem(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.Red, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    val navController = rememberNavController()
    DashboardScreen(navController)
}