package com.example.code_mobile.paginas.code_mobile.atendimento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun ComponentAtendimentoEtapa4(
    valorTatuagem: String,
    dataAtendimento: String,
    horarioAtendimento: String,
    onAnteriorClick: () -> Unit,
    onConfirmarClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "Gerar Ordem de Serviço",
            style = textPadrao.copy(fontSize = 20.sp, color = Color.White),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Valor da Tatuagem:",
            style = textPadrao.copy(fontSize = 16.sp, color = Color.LightGray),
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            valorTatuagem,
            style = textPadrao.copy(fontSize = 18.sp, color = Color.White),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Data do Atendimento:",
            style = textPadrao.copy(fontSize = 16.sp, color = Color.LightGray),
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            dataAtendimento,
            style = textPadrao.copy(fontSize = 18.sp, color = Color.White),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Horário do Atendimento:",
            style = textPadrao.copy(fontSize = 16.sp, color = Color.LightGray),
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            horarioAtendimento,
            style = textPadrao.copy(fontSize = 18.sp, color = Color.White),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(
                onClick = onAnteriorClick,
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text("Voltar", color = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onConfirmarClick,
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text("Gerar Ordem", color = Color.White)
            }
        }
    }
}