package com.example.code_mobile.paginas.code_mobile

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code_mobile.R

fun Modifier.inputPadrao() = this
    .height(48.dp)
    .width(310.dp)
    .background(color = Color.White, RoundedCornerShape(10.dp))
    .clip(RoundedCornerShape(10.dp)) // Borda arredondada

@Composable // Input (Login)
fun CampoLogin(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: TextStyle
) {
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
fun Input(
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
fun card4Informacoes(
    caminhoImagem: Int,
    descricaoImagem: String,
    coluna1Info1: String,
    coluna1Info2: String,
    coluna2Info1: String,
    coluna2Info2: String,
    onEditClick: () -> Unit
) {

    val imagemInformada = painterResource(id = caminhoImagem)

    // tudo que tem dentro do card
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
                    }
            )
        }


        // Imagem e informações
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = imagemInformada,
                contentDescription = descricaoImagem,
                modifier = Modifier.size(75.dp)
            )

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

val textPadrao = TextStyle(
    fontSize = 20.sp,
    color = Color.White,
    fontStyle = FontStyle.Normal
)
