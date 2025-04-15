package com.example.code_mobile.paginas.code_mobile.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.componente.CampoLogin
import com.example.code_mobile.token.auth.AuthService
import com.example.code_mobile.token.auth.LoginRequest
import com.example.code_mobile.token.network.RetrofithAuth

import com.example.code_mobile.token.network.TokenManager
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun TelaLogin(navController: NavController, modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) } // Para mostrar "carregando" no botão
    var loginRequested by remember { mutableStateOf(false) } // Estado para indicar quando o login foi solicitado
    var errorMessage by remember { mutableStateOf<String?>(null) } // Mensagem de erro para o usuário

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    suspend fun login() {
        errorMessage = null // Limpa qualquer mensagem de erro anterior
        if (email.isNotEmpty() && senha.isNotEmpty()) {
            loading = true
            try {
                // Chama o AuthService para fazer o login
                val response = RetrofithAuth.retrofit.create(AuthService::class.java)
                    .login(LoginRequest(email, senha))

                if (response.isSuccessful) {
                    // Armazenando o token
                    TokenManager.token = response.body()?.token
                    println("Login bem-sucedido! Token: ${TokenManager.token}")

                    // Navega para a tela de estoque
                    navController.navigate("Estoque")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = when (response.code()) {
                        401 -> "Email ou senha incorretos."
                        else -> "Erro ao fazer login: ${response.code()} - $errorBody"
                    }
                    errorMessage = message
                    println(errorMessage)
                }
            } catch (e: Exception) {
                errorMessage = "Email ou senha incorretos"
                println(errorMessage)
            } finally {
                loading = false
            }
        } else {
            errorMessage = "Por favor, preencha todos os campos."
            println(errorMessage)
        }
    }


    // Chama a função de login quando loginRequested mudar para true
    LaunchedEffect(loginRequested) {
        if (loginRequested) {
            login()
            loginRequested = false // Reseta o estado de solicitação de login
        }
    }

    Column(
        modifier = modifier
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
            textStyle = textPadrao,
            modifier = Modifier.fillMaxWidth() // Ocupa a largura disponível
        )

        Spacer(modifier = Modifier.height(30.dp)) // Espaço entre os campos

        CampoLogin(
            titulo = "Senha:",
            valor = senha,
            onValorChange = { senha = it },
            textStyle = textPadrao,
            modifier = Modifier.fillMaxWidth() // Ocupa a largura disponível
        )

        Spacer(modifier = Modifier.height(30.dp)) // Espaço entre os campos

        Button(
            onClick = {
                // Altera o estado para indicar que o login foi solicitado
                loginRequested = true
            },
            modifier = Modifier
                .fillMaxWidth() // Ocupa a largura disponível
                .background(color = Color.White, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)) // Borda arredondada
                .background(Color(0xFFDF0050)),
            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
            contentPadding = PaddingValues(0.dp) // Centralizando o texto
        ) {
            Text(
                text = if (loading) "Carregando..." else "Entrar"
            )
        }

        // Exibe a mensagem de erro se houver
        if (!errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp)) // Espaço entre o botão e a mensagem de erro
            Text(
                text = errorMessage!!,
                color = Color(0xFFFF4D4D), // Vermelho para indicar erro
                style = textPadrao.copy(fontSize = 14.sp),
                textAlign = TextAlign.Center
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
        text = AnnotatedString(frase) +
                AnnotatedString(
                    codeTech,
                    spanStyle = funText.copy(Color(0XFF9B00CE), fontSize = 16.sp).toSpanStyle()
                ),
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