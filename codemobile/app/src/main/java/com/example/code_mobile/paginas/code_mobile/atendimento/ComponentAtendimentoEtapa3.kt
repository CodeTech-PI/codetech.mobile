package com.example.code_mobile.paginas.code_mobile.atendimento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun ComponentAtendimentoEtapa3(
    valorTatuagem: String,
    onValorTatuagemChange: (String) -> Unit,
    dataAtendimento: String,
    onDataAtendimentoChange: (String) -> Unit,
    horarioAtendimento: String,
    onHorarioAtendimentoChange: (String) -> Unit,
    onAnteriorClick: () -> Unit,
    onProximoClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "Valor do Atendimento",
            style = textPadrao.copy(fontSize = 20.sp, color = Color.White),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = valorTatuagem,
            onValueChange = onValorTatuagemChange,
            label = { Text("Valor da Tatuagem", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = dataAtendimento,
            onValueChange = onDataAtendimentoChange,
            label = { Text("Data do Atendimento", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = horarioAtendimento,
            onValueChange = onHorarioAtendimentoChange,
            label = { Text("Horário do Atendimento", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
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
                onClick = onProximoClick,
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text("Próximo", color = Color.White)
            }
        }
    }
}