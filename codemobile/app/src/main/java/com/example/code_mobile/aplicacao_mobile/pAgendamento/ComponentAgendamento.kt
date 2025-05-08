package com.example.code_mobile.aplicacao_mobile.pAgendamento

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente
import com.example.code_mobile.paginas.code_mobile.inputPadrao
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable // Input
fun InputPesquisarAgendamento(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: TextStyle,
    labelInfo: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    Column(
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


@Composable
fun CampoCadastrarAgendamneto(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: TextStyle,
    placeholderText: String,
    tituloStyle: TextStyle,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = titulo, style = tituloStyle.copy(color = Color.White))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            textStyle = textStyle.copy(color = Color.White),
            placeholder = { Text(placeholderText, style = textStyle.copy(color = Color.Gray)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFDF0050),
                unfocusedBorderColor = Color(0xFF505050),
                cursorColor = Color.White,
                errorBorderColor = Color.Red
            ),
            isError = isError
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red
            )
        }
    }
}

@Composable
fun cardExibirCliente(
    cliente: ModelCliente,
    nome: String,
    CPF: String,
    telefone: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                2.dp,
                Color(0xFF252525),
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(0.5f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = nome,
                    style = textPadrao.copy(fontSize = 16.sp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = CPF,
                    style = textPadrao.copy(fontSize = 14.sp)
                )

                Text(
                    text = telefone,
                    style = textPadrao.copy(fontSize = 14.sp)
                )
            }
        }
    }
}