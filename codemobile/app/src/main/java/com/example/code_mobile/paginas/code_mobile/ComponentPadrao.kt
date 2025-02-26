package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code_mobile.paginas.code_mobile.ui.theme.inputPadrao

@Composable // Input (Login)
fun CampoLogin(titulo: String, valor: String, onValorChange: (String) -> Unit, textStyle: TextStyle) {
    Column(
        horizontalAlignment = Alignment.Start // Alinha para esquerda
    ) {
        Text(text = titulo, style = textStyle) // será do tipo texto com o estilo passado no "Campo"

        Spacer(modifier = Modifier.height(10.dp)) // Espaço entre título e campo

        // Input
        TextField(
            value = valor,
            onValueChange = onValorChange, // Atualiza o valor conforme o usuário digita
            textStyle = textStyle.copy(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier
                .inputPadrao()
                .height(180.dp), // Aumenta a altura da área de digitação
            singleLine = false // impede quebra de linha na input
        )
    }
}


@Composable // Input                                                                            //label não é uma String
fun Input(titulo: String, valor: String, onValorChange: (String) -> Unit, textStyle: TextStyle, labelInfo: @Composable (() -> Unit), modifier: Modifier = Modifier) {
    Column (
    )
    {
        Text(text = titulo, style = textStyle) // Tipo texto com o estilo passado no "Campo"

        // Input
        TextField(
            value = valor,
            onValueChange = onValorChange, // Atualiza o valor conforme o usuário digita
            textStyle = textStyle.copy(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier
                .inputPadrao(),
            singleLine = false, // impede quebra de linha na input
            label = labelInfo
        )
    }
}

val textPadrao = TextStyle(
    fontSize = 20.sp,
    color = Color.White,
    fontStyle = FontStyle.Normal
)
