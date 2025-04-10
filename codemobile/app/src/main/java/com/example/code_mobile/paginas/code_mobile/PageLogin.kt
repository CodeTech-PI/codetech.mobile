package com.example.code_mobile.paginas.code_mobile.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.CampoLogin
import com.example.code_mobile.token.auth.AuthService
import com.example.code_mobile.token.auth.LoginRequest
import com.example.code_mobile.token.network.RetrofithAuth
import com.example.code_mobile.token.network.TokenManager
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun TelaLogin(navController: NavController, modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) } // Para mostrar ou esconder o carregamento
    var loginRequested by remember { mutableStateOf(false) } // Estado para indicar quando o login foi solicitado

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    suspend fun login() {
        if (email.isNotEmpty() && senha.isNotEmpty()) {
            loading = true
            try {
                // Chama o AuthService para fazer o login
                val response = RetrofithAuth.retrofit.create(AuthService::class.java)
                    .login(LoginRequest(email, senha))

                if (response.isSuccessful) {
                    // Armazenando o token sem o "Bearer"
                    TokenManager.token = response.body()?.token
                    println("Login bem-sucedido! Token: ${TokenManager.token}")

                    // Navega para a tela de estoque
                    navController.navigate("Estoque")
                } else {
                    println("Erro ao fazer login: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Erro ao tentar fazer login: ${e.message}")
            } finally {
                loading = false
            }
        } else {
            println("Por favor, preencha todos os campos.")
        }
    }




    // LaunchedEffect para chamar a função de login quando loginRequested mudar para true
    LaunchedEffect(loginRequested) {
        if (loginRequested) {
            login() // Chama a função login
            loginRequested = false // Reseta o estado de solicitação de login
        }
    }

    Column(modifier = modifier
        .fillMaxSize() // Faz a mudança pegar na tela toda
        .background(Color(0xFF1B1B1B))
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centraliza horizontalmente
    ) {
        Spacer(modifier = Modifier.weight(0.5f)) // Centraliza verticalmente

        Text(
            text = "Lombardi",
            style = textPadrao.copy(
                fontSize = 40.sp, // Tamanho de texto
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(50.dp)) // Espaço entre os campos

        CampoLogin(
            titulo = "Email:",
            valor = email,
            onValorChange = { email = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(30.dp)) // Espaço entre os campos

        CampoLogin(
            titulo = "Senha:",
            valor = senha,
            onValorChange = { senha = it },
            textStyle = textPadrao
        )

        Spacer(modifier = Modifier.height(30.dp)) // Espaço entre os campos

        Button(
            onClick = {
                // Altera o estado para indicar que o login foi solicitado
                loginRequested = true
            },
            modifier = Modifier
                .width(310.dp)
                .background(color = Color.White, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)) // Borda arredondada
                .background(Color(0xFFDF0050)),
            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
            contentPadding = PaddingValues(0.dp) // Centralizando
        ) {
            Text(
                text = if (loading) "Carregando..." else "Entrar"
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Empurra o próximo elemento para o final

        FraseInferior(
            frase = "Desenvolvido por ",
            codeTech = "Codetech",
            funText = textPadrao
        )

        Spacer(modifier = Modifier.weight(0.2f)) // Empurra o próximo elemento para o final
    }
}



@Composable
fun FraseInferior(frase: String, codeTech: String, funText: TextStyle) {
    Text(
        //AnnotatedString = manipular style de diferentes partes do texto com estilos diferentes
        // metade da frase (rosa) + final da frase (verde)

        text = AnnotatedString(frase) +
                AnnotatedString(codeTech, spanStyle = funText.copy(Color(0XFF9B00CE), fontSize = 16.sp).toSpanStyle()),
        style = funText.copy(
            fontSize = 16.sp
        )
    )

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
        TelaLogin(navController)  // Passe o navController para TelaLogin
    }
}