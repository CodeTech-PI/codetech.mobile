package com.example.code_mobile.paginas.code_mobile

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
    coluna2Info2: String
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

@Composable
fun cardFilial(
    logradouro: String,
    estado: String,
    cidade: String,
    cep: String,
    status: String,
    onDeleteClick: () -> Unit
) {



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
        verticalArrangement = Arrangement.Center,
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
                    }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painter = painterResource(id = R.drawable.icone_deletar),
                contentDescription = "Excluir",
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        onDeleteClick()
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = logradouro,
                    style = textPadrao.copy(fontSize = 16.sp)
                )

                Text(
                    text = cidade,
                    style = textPadrao.copy(fontSize = 16.sp)
                )

                Text(
                    text = estado,
                    style = textPadrao.copy(fontSize = 16.sp)
                )

                Text(
                    text = cep,
                    style = textPadrao.copy(fontSize = 16.sp)
                )
            }

            // coluna 2: duas informações
            Text(
                text = status,
                style = textPadrao.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
    }
}

@Composable // Input
fun CampoFilial(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: TextStyle,
    placeholderText: String = ""
) {
    Column(
        horizontalAlignment = Alignment.Start // Alinha para esquerda
    ) {
        Text(text = titulo, style = textStyle) // será do tipo texto com o estilo passado no "Campo"

        Spacer(modifier = Modifier.height(10.dp)) // Espaço entre título e campo

        // Input
        TextField(
            value = valor,
            onValueChange = onValorChange,
            placeholder = {Text(placeholderText) }, // Adiciona o placeholder aqui
            textStyle = textStyle.copy(fontSize = 16.sp, color = Color.White), // Cor do título do campo (branco)
            modifier = Modifier
                .width(300.dp)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoFilialStatus( // Novo componente para o campo "Status"
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    textStyle: TextStyle,
    opcoes: List<String> = listOf("Operante", "Inoperante")
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,

    ) {
        Text(text = titulo, style = textStyle)

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = valor,
                onValueChange = {},
                placeholder = { Text("Selecione") },
                textStyle = textStyle.copy(fontSize = 16.sp, color = Color.White),
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp)
                    .menuAnchor(),
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF252525),
                    unfocusedContainerColor = Color(0xFF252525),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White) // Fundo branco aqui
            ) {
                opcoes.forEach { opcao ->
                    DropdownMenuItem(
                        text = { Text(text = opcao, color = Color.Gray) }, // Letras cinzas aqui
                        onClick = {
                            onValorChange(opcao)
                            expanded = false
                        },
                        modifier = Modifier.background(Color.White) // Cor de fundo branca
                    )

                }
            }
        }
    }
}

// Em ComponentesPadrao.kt
@Composable
fun ConfirmDeleteDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Confirmar Exclusão", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tem certeza que deseja excluir esta filial?", color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = {
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("Sim", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onCancel()
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF1B1B1B)),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("Não", color = Color.White)
                    }
                }
            }
        }
    }
}




val textPadrao = TextStyle(
    fontSize = 20.sp,
    color = Color.White,
    fontStyle = FontStyle.Normal
)
