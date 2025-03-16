package com.example.code_mobile.paginas.code_mobile
import androidx.compose.ui.unit.Dp
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Linha de KPIs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KpiItem("Lucro Mensal", "R$ 25.000", Color(0xFF4CAF50))
            KpiItem("Agendamentos", "45", Color(0xFF2196F3))
            KpiItem("Estoque", "85%", Color(0xFFFF9800))
        }

        // Gráfico Simples
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(200.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Desempenho Mensal",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Barras simples
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    ChartBar(70.dp, "Jan", Color(0xFF4CAF50))
                    ChartBar(100.dp, "Fev", Color(0xFF2196F3))
                    ChartBar(85.dp, "Mar", Color(0xFFFF9800))
                    ChartBar(120.dp, "Abr", Color(0xFF9C27B0))
                }
            }
        }

        // Lista de Alertas
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Alertas",
                style = MaterialTheme.typography.titleMedium,
                color = Color.DarkGray
            )

            AlertItem("Tinta Preta acabando", "Estoque: 3 unidades")
            AlertItem("Agulha acabando", "Estoque: 5 unidades")
        }
    }
}

@Composable
fun KpiItem(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier.size(100.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {  // <-- Fechamento correto dos parâmetros do Card
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
                .height(height)  // Agora usando o tipo Dp corretamente
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
    // Inicialize o navController aqui
    val navController = rememberNavController()
    DashboardScreen(navController)  // Passe o navController para TelaLogin
    }
