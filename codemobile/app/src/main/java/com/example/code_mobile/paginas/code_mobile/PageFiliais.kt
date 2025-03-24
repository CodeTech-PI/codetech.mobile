package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.R
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun Filiais (navController: NavController, modifier: Modifier = Modifier){

    var showDialog by remember { mutableStateOf(false) }
    var pesquisa by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        menuComTituloPage("Filiais", navController)

        // Filtro e icone de adicionar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Input(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Pesquisar filial") }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar uma filial!")
                        navController.navigate("AdicionarFilial")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box (
        ) {


            cardFilial(

                "Rua: Haddock lobo",
                "Estado: SP",
                "Cidade: São Paulo",
                "CEP: 12345678",
                "Status: Inoperante",
                onDeleteClick = { showDialog = true }

            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box (
        ) {
            cardFilial(

                "Rua: São Marcos",
                "Estado: SP",
                "Cidade: São Paulo",
                "CEP: 87456321",
                "Status: Operante",
                onDeleteClick = { showDialog = true }

                )
        }

        Spacer(modifier = Modifier.height(20.dp))


        Box (
        ) {
            cardFilial(

                "Rua: Caraibas",
                "Estado: SP",
                "Cidade: São Paulo",
                "CEP: 19141010",
                "Status: Operante",
                onDeleteClick = { showDialog = true }

                )
        }






    }

//    if (showDialog) {
//        // Fundo cinza semi-transparente
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Gray.copy(alpha = 0.5f)), // Fundo cinza com transparência
//            contentAlignment = Alignment.Center
//        ) {
//            Card(
//                modifier = Modifier.padding(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525)), // Fundo do Card cinza
//                shape = RoundedCornerShape(10.dp) // Borda arredondada
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text("Confirmar Exclusão", color = Color.White)
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Tem certeza que deseja excluir esta filial?", color = Color.White)
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(horizontalArrangement = Arrangement.End) {
//                        Button(
//                            onClick = {
//                                println("Filial excluída!")
//                                showDialog = false
//                            },
//                            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)), // Botão salvar rosa
//                            shape = RoundedCornerShape(15.dp)
//                        ) {
//                            Text("Sim", color = Color.White)
//                        }
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Button(
//                            onClick = { showDialog = false },
//                            colors = ButtonDefaults.buttonColors(Color(0xFF1B1B1B)), // Botão cancelar preto
//                            shape = RoundedCornerShape(15.dp)
//                        ) {
//                            Text("Não", color = Color.White)
//                        }
//                    }
//                }
//            }
//        }
//    }

    if (showDialog) {
        // Chame o ConfirmDeleteDialog passando as funções para confirmar ou cancelar
        ConfirmDeleteDialog(
            onConfirm = {
                println("Filial excluída!")
                showDialog = false
            },
            onCancel = {
                showDialog = false
            }
        )
    }

    // Exemplo de como acionar o diálogo (por exemplo, ao clicar no ícone de lixeira)
    IconButton(onClick = { showDialog = true }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir Filial")
    }
}





@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_2
)

@Composable
fun GreetingPreviewFiliais() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        Filiais(navController)  // Passe o navController para TelaLogin
    }
}