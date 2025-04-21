package com.example.code_mobile.paginas.code_mobile.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.inputPadrao
import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun cardCliente(
    cliente: ModelCliente,
    coluna1Info1: String,
    coluna1Info2: String,
    coluna2Info1: String,
    coluna2Info2: String,
    onEditClick: () -> Unit,
    onDeleteClick: (ModelCliente) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(12.dp))
            .border(
                2.dp,
                Color(0xFF252525),
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // icones de editar e deletar
        Row(
            modifier = Modifier
                .padding(end = 10.dp, top = 5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.icone_editar),
                contentDescription = "Editar",
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        println("Clicou para editar cliente!")
                        onEditClick()
                    }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painter = painterResource(id = R.drawable.icone_deletar),
                contentDescription = "Excluir",
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        println("Clicou para excluir cliente!")
                        onDeleteClick(cliente)
                    }
            )
        }

        // Imagem e informações
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(10.dp))

            // coluna 1: duas informações
            Column(
                modifier = Modifier.weight(0.5f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = coluna1Info1,
                    style = textPadrao.copy(fontSize = 16.sp)
                )

                Text(
                    text = coluna1Info2,
                    style = textPadrao.copy(fontSize = 16.sp)
                )
            }

            // coluna 2: duas informações
            Column(
                modifier = Modifier.weight(0.5f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = coluna2Info1,
                    style = textPadrao.copy(fontSize = 16.sp)
                )

                Text(
                    text = coluna2Info2,
                    style = textPadrao.copy(fontSize = 16.sp)
                )
            }
        }
    }
}

@Composable
fun Modifier.inputPadrao() = this
    .height(48.dp)
    .width(310.dp)
    .background(color = Color.White, RoundedCornerShape(10.dp))
    .clip(RoundedCornerShape(10.dp)) // Borda arredondada


@Composable // Input
fun InputPesquisarCliente(
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
fun CampoCadastrarCliente(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: TextStyle,
    placeholderText: String = ""
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Text(text = titulo, style = textStyle)

        Spacer(modifier = Modifier.height(10.dp))

        // Input
        TextField(
            value = valor,
            onValueChange = onValorChange,
            placeholder = { Text(placeholderText) },
            textStyle = textStyle.copy(fontSize = 16.sp, color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF252525), // Cor de fundo quando focado (cinza)
                unfocusedContainerColor = Color(0xFF252525), // Cor de fundo quando desfocado (cinza)
                cursorColor = Color.White,
                focusedTextColor = Color.White, // Cor do texto digitado quando focado (branco)
                unfocusedTextColor = Color.White, // Cor do texto digitado quando desfocado (branco)
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
    }
}

val textPadrao = TextStyle(
    fontSize = 20.sp,
    color = Color.White,
    fontStyle = FontStyle.Normal
)
