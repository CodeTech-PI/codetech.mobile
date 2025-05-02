package com.example.code_mobile.paginas.code_mobile.pCliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code_mobile.R
import com.example.code_mobile.paginas.code_mobile.inputPadrao
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente
import com.example.code_mobile.paginas.code_mobile.textPadrao

@Composable
fun cardCliente(
    cliente: ModelCliente,
    coluna1Info1: String,
    coluna1Info2: String,
    coluna2Info1: String,
    coluna2Info2: String,
    onEditClick: (ModelCliente) -> Unit,
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
                        onEditClick(cliente)
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
    placeholderText: String,
    tituloStyle: TextStyle,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.None), // Correção aqui
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(titulo, style = tituloStyle)
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            textStyle = textStyle.copy(color = Color.White),
            placeholder = {
                Text(
                    placeholderText,
                    style = textStyle.copy(
                        fontSize = 14.sp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color(0xFFDF0050),
                fontSize = 12.sp
            )
        }
    }
}

class TelefoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(11)
        val formatted = buildString {
            if (digits.length > 0) append("(")
            append(digits.take(2))
            if (digits.length > 2) append(") ")
            append(digits.substring(2, minOf(digits.length, 7)))
            if (digits.length > 7) append("-")
            append(digits.substring(7, minOf(digits.length, 11)))
        }
        return TransformedText(AnnotatedString(formatted), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset <= 0 -> offset
                offset <= 2 -> offset + 1
                offset <= 7 -> offset + 3
                offset <= 11 -> offset + 4
                else -> formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 1 -> offset
                offset <= 4 -> offset - 1
                offset <= 9 -> offset - 3
                offset <= 14 -> offset - 4
                else -> 11
            }
        })
    }
}

class DataNascimentoVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(8)
        val formatted = buildString {
            append(digits.take(2))
            if (digits.length > 2) append("/")
            if (digits.length > 2) append(digits.substring(2, minOf(digits.length, 4)))
            if (digits.length > 4) append("/")
            if (digits.length > 4) append(digits.substring(4, minOf(digits.length, 8)))
        }
        return TransformedText(AnnotatedString(formatted), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 4 -> offset + 1
                offset <= 8 -> offset + 2
                else -> formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 5 -> offset - 1
                offset <= 10 -> offset - 2
                else -> 8
            }
        })
    }
}

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.replace(Regex("[^0-9]"), "")
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 2 || i == 5) out += "."
            if (i == 8) out += "-"
        }

        return TransformedText(
            text = AnnotatedString(out),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 2) return offset
                    if (offset <= 5) return offset + 1
                    if (offset <= 8) return offset + 2
                    return 11
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 2) return offset
                    if (offset <= 6) return offset - 1
                    if (offset <= 10) return offset - 2
                    return 9
                }
            }
        )
    }
}