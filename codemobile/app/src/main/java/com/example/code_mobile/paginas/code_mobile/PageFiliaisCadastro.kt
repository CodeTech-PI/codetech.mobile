package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.CampoFilial
import com.example.code_mobile.paginas.code_mobile.CampoFilialStatus
import com.example.code_mobile.paginas.code_mobile.viewModel.ViewModelFiliais
import com.example.code_mobile.ui.theme.CodemobileTheme


import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.code_mobile.paginas.code_mobile.model.ModelFiliais


@Composable
fun FilialCadastro(navController: NavController, viewModel: ViewModelFiliais = viewModel()) {
    var logradouro by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Operante") }

    // Função para salvar a filial
    val onSaveClick = {
        val filial = ModelFiliais(
            id = 0, // Defina o ID como 0 para que a API ou banco gere automaticamente
            cep = cep,
            lagradouro = logradouro,
            bairro = "", // Se for necessário, adicione o campo bairro também
            cidade = cidade,
            estado = estado,
            complemento = "", // Se for necessário, adicione o campo complemento
            num = 0, // Se for necessário, adicione o campo num
            status = status
        )
        // Chama o ViewModel para salvar a filial
        viewModel.cadastrarFilial(filial)
        // Exibe a mensagem de sucesso e navega de volta ou exibe algo mais
        navController.popBackStack() // Exemplo para voltar à tela anterior
        println("Filial salva!")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título da página
        Text(
            text = "Cadastro de Filial",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )

        // Campos de cadastro
        CampoFilial(
            titulo = "Logradouro",
            valor = logradouro,
            onValorChange = { logradouro = it },
            textStyle = textPadrao,
            placeholderText = "Digite o logradouro"
        )

        CampoFilial(
            titulo = "Estado",
            valor = estado,
            onValorChange = { estado = it },
            textStyle = textPadrao,
            placeholderText = "Digite o estado"
        )

        CampoFilial(
            titulo = "Cidade",
            valor = cidade,
            onValorChange = { cidade = it },
            textStyle = textPadrao,
            placeholderText = "Digite a cidade"
        )

        CampoFilial(
            titulo = "CEP",
            valor = cep,
            onValorChange = { cep = it },
            textStyle = textPadrao,
            placeholderText = "Digite o CEP"
        )

        CampoFilialStatus(
            titulo = "Status",
            valor = status,
            onValorChange = { status = it },
            textStyle = textPadrao
        )

        // Botão para salvar filial
        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(Color(0xFFDF0050)),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Salvar Filial", color = Color.White)
        }

        // Exibir as informações da filial cadastrada no formato de card
        cardFilial(
            logradouro = logradouro,
            estado = estado,
            cidade = cidade,
            cep = cep,
            status = status,
            onDeleteClick = {
                // Lógica de exclusão
                println("Filial excluída!")
            },
            navController = navController
        )
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
        FilialCadastro(navController)  // Passe o navController para TelaLogin
    }
}
