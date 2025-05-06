package com.example.code_mobile.paginas.code_mobile.atendimento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun ComponentConfirmacaoAtendimento(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    Dialog(onDismissRequest = onCancelar) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2B2B2B), RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Atenção",
                style = textPadrao.copy(fontSize = 20.sp, color = Color(0xFFDF0050))
            )
            Text(
                "Tem certeza que deseja gerar a ordem de serviço?",
                style = textPadrao.copy(fontSize = 16.sp, color = Color.White),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onCancelar,
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text("Cancelar", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onConfirmar,
                    colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
                ) {
                    Text("Confirmar", color = Color.White)
                }
            }
        }
    }
}