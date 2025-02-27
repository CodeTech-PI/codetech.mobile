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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun TelaClientes(navController: NavController, modifier: Modifier = Modifier) {


    var pesquisa by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        manuComTituloPage("Clientes")

        // Filtro e icone de adicionar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Input(
                titulo = "",
                valor = pesquisa,
                onValorChange = { pesquisa = it },
                textStyle = textPadrao,
                labelInfo = { if (pesquisa.isEmpty()) Text("Filtre por CPF") },
                modifier = Modifier
                    .weight(1f)
            )

            Image(
                painter = painterResource(id = R.drawable.icon_add),
                contentDescription = "Adicionar",
                modifier = Modifier
                    .size(80.dp)
                    .padding(start = 8.dp, top = 25.dp)
                    .clickable {
                        println("Clicou para cadastrar um cliente!")
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f) // vai ocupar 90% da largura
                .height(110.dp)
                .clip(RoundedCornerShape(12.dp)) // Cor de fundo + cantos arredondados
                .border(
                    2.dp,
                    Color(0xFF252525),
                    shape = RoundedCornerShape(12.dp)
                )
        ){

            // Nome e Icone
            Row (
                    modifier = Modifier
                     .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){

                Spacer(modifier = Modifier.width(145.dp))

                Text(
                    text = "João Pereira",
                    style = textPadrao.copy(fontSize = 16.sp),
                    modifier = Modifier.weight(1f) // Ocupa o máximo possível do espaço disponível
                )

                Spacer(modifier = Modifier.width(10.dp))

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

            // Ícone de perfil e dados
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icone_perfil),
                    contentDescription = "Foto Perfil",
                    modifier = Modifier
                        .size(85.dp)
                        .padding(start = 20.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(start = 25.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "123.456.789-00",
                        style = textPadrao.copy(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "23/01/2004",
                        style = textPadrao.copy(fontSize = 16.sp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa o espaço restante
                        .padding(start = 10.dp), // Aproxima os textos da esquerda
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "123.456.789-00",
                        style = textPadrao.copy(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "23/01/2004",
                        style = textPadrao.copy(fontSize = 16.sp)
                    )
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
fun GreetingPreview() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        TelaClientes(navController)  // Passe o navController para TelaLogin
    }
}