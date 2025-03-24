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
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun Teste(
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(true) }

    // tudo que tem dentro do card
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(0.9f)
//            .clip(RoundedCornerShape(12.dp))
//            .border(
//                2.dp,
//                Color(0xFF252525),
//                shape = RoundedCornerShape(12.dp)
//            ),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        // icones de editar e deletar
//        Row(
//            modifier = Modifier
//                .padding(end = 10.dp, top = 5.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.Bottom,
//            horizontalArrangement = Arrangement.End
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.icone_editar),
//                contentDescription = "Editar",
//                modifier = Modifier
//                    .size(25.dp)
//                    .clickable {
//                        println("Clicou para editar cliente!")
//                    }
//            )
//
//            Spacer(modifier = Modifier.width(10.dp))
//
//            Image(
//                painter = painterResource(id = R.drawable.icone_deletar),
//                contentDescription = "Excluir",
//                modifier = Modifier
//                    .size(25.dp)
//                    .clickable {
//                        showDialog = true
//                    }
//            )
//        }
//
//        // Imagem e informações
//        Row(
//            modifier = Modifier.padding(10.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
//        ) {
//            Spacer(modifier = Modifier.width(10.dp))
//
//            // coluna 1: duas informações
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(4.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//                Text(
//                    text = logradouro,
//                    style = textPadrao.copy(fontSize = 16.sp)
//                )
//
//                Text(
//                    text = cidade,
//                    style = textPadrao.copy(fontSize = 16.sp)
//                )
//
//                Text(
//                    text = estado,
//                    style = textPadrao.copy(fontSize = 16.sp)
//                )
//
//                Text(
//                    text = cep,
//                    style = textPadrao.copy(fontSize = 16.sp)
//                )
//            }
//
//            // coluna 2: duas informações
//            Text(
//                text = status,
//                style = textPadrao.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
//                modifier = Modifier.align(Alignment.Bottom)
//            )
//        }

    if (showDialog) {
        // Fundo cinza semi-transparente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)), // Fundo cinza com transparência
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)), // Fundo do Card cinza
                shape = RoundedCornerShape(10.dp) // Borda arredondada
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
                                println("Filial excluída!")
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)), // Botão salvar rosa
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text("Sim", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(Color(0xFF1B1B1B)), // Botão cancelar preto
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text("Não", color = Color.White)
                        }
                        }
                    }
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
fun TestePreview() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        Teste(navController)
    }
}