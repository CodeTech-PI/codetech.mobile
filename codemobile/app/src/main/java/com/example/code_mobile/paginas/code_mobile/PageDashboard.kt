package com.example.code_mobile.paginas.code_mobile

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class MonthlyFinance(
    val month: String,
    val lucro: Float,
    val bruto: Float,
    val gastos: Float,
    val maxValue: Float
)

val financialData = listOf(
    MonthlyFinance("Jan", 60f, 80f, 45f, 80f),
    MonthlyFinance("Fev", 70f, 85f, 50f, 85f),
    MonthlyFinance("Mar", 65f, 90f, 55f, 90f),
    MonthlyFinance("Abr", 75f, 95f, 60f, 95f)
)

@Composable
fun DashboardScreen(navController: NavController, modifier: Modifier = Modifier) {
    var showFinancialChart by remember { mutableStateOf(false) }
    var showAlerts by remember { mutableStateOf(false) }
    val lowStockItems = listOf(
        "Tinta Preta - Estoque: 3 unidades",
        "Agulha - Estoque: 5 unidades"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        menuComTituloPage("Dashboard", navController)
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
                    value = "45/mês",
                    color = Color(0xFF252525),
                    modifier = Modifier
                        .weight(1f)
                        .height(66.dp)
                )

                KpiItem(
                    title = "Lucro Anual",
                    value = "R\$ 300K",
                    color = Color(0xFF252525),
                    modifier = Modifier
                        .weight(1f)
                        .height(66.dp)
                )
            }
        }

        if (showFinancialChart) {
            FinancialChart(onBackClick = { showFinancialChart = false })
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
                    ChartBar(70.dp, "Jan", Color(0xFFDF0050))
                    ChartBar(70.dp, "Fev", Color(0xFFDF0050))
                    ChartBar(70.dp, "Mar", Color(0xFFDF0050))
                    ChartBar(70.dp, "Abr", Color(0xFFDF0050))
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
                    ChartBar(70.dp, "Tinta Preta", Color(0xFF4888B7))
                    ChartBar(45.dp, "Agulha", Color(0xFF4888B7))
                    ChartBar(75.dp, "T. Vermelha", Color(0xFF4888B7))
                    ChartBar(60.dp, "T. Azul", Color(0xFF4888B7))
                }
            }
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
fun FinancialChart(onBackClick: () -> Unit) {
    FinancialChartWithArrow(
        title = "Bruto x Gastos x Lucro por Mês",
        showIcon = true,
        onArrowClick = onBackClick
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Espaço entre meses
        ) {
            financialData.forEach { data ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp) // 🔽 Espaço entre as barras
                ) {
                    Text(
                        text = data.month,
                        color = Color.White,
                        fontSize = 12.sp, // Fonte menor
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