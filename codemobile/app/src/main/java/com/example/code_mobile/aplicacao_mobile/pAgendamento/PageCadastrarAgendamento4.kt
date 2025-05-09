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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun AgendamentoEtapa4(
    navController: NavController,
    modifier: Modifier = Modifier,
    agendamentoId: Int,
    ) {

    // 1. Criar o Model, Service e ViewModel para Faturamento.
    // 2. No botão "Confirmar", deve chamar o método POST de faturamento.
    // 3. Para exibição dos dados:
    //    - Usar a ViewModel de Agendamento para obter data e hora.
    //    - Usar a ViewModel de Ordem de Serviço para obter o valor.


    var valorTatuagem by remember { mutableStateOf("") }

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
                    color = Color.Yellow
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
            Text(
                text = "Valor da Tatuagem:",
                style = textPadrao.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "R$ 400.0",
                style = textPadrao.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = "Data do atendimento:",
                style = textPadrao.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "18/11/2025",
                style = textPadrao.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = "Horário do atendimento:",
                style = textPadrao.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "10:00",
                style = textPadrao.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center
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
                onClick = { },
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
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDF0050))
            ) {
                Text(
                    text = "Confirmar",
                    fontSize = 14.sp,
                    color = Color.White
                )
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
fun AgendamentoEtapa4Preview() {
    CodemobileTheme {
        val navController = rememberNavController()
        AgendamentoEtapa4(navController = navController, agendamentoId = 1)
    }
}