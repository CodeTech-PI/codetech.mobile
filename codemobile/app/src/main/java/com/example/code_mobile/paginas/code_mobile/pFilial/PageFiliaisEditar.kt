package com.example.code_mobile.paginas.code_mobile.pFilial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.CampoFilial
import com.example.code_mobile.paginas.code_mobile.cViewModel.filial.ViewModelFilial
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun FiliaisEditar(
    navController: NavController,
    viewModel: ViewModelFilial = viewModel(),
    modifier: Modifier = Modifier
) {
    // Recupera o objeto da filial passado via SavedStateHandle
    val filial = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Int>("filial")

    // Se filial for nulo, não renderiza nada (ou mostra erro)


    val cep by viewModel.cep
    val logradouro by viewModel.logradouro
    val bairro by viewModel.bairro
    val cidade by viewModel.cidade
    val estado by viewModel.estado
    val num by viewModel.num
    val complemento by viewModel.complemento

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF1B1B1B))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Editar", style = textPadrao.copy(fontSize = 30.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(20.dp))

        CampoFilial("CEP:", cep, onValorChange = viewModel::atualizarCep, textStyle = textPadrao, placeholderText = "")
        CampoFilial("Logradouro:", logradouro, onValorChange = viewModel::atualizarlogradouro, textStyle = textPadrao, placeholderText = "")
        CampoFilial("Bairro:", bairro, onValorChange = viewModel::atualizarBairro, textStyle = textPadrao, placeholderText = "")
        CampoFilial("Cidade:", cidade, onValorChange = viewModel::atualizarCidade, textStyle = textPadrao, placeholderText = "")
        CampoFilial("Estado:", estado, onValorChange = viewModel::atualizarEstado, textStyle = textPadrao, placeholderText = "")
        CampoFilial("Número:", num, onValorChange = viewModel::atualizarNum, textStyle = textPadrao, placeholderText = "")
        CampoFilial("Complemento:", complemento, onValorChange = viewModel::atualizarComplemento, textStyle = textPadrao, placeholderText = "")

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.editarFilial(filial?: 0) // Usa o objeto original com ID
                    navController.popBackStack()
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text("Salvar")
            }

            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF252525))
            ) {
                Text("Cancelar")
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
fun FiliaisEditarPreview() {
    CodemobileTheme {
        // Inicialize o navController aqui
        val navController = rememberNavController()
        val viewModel = ViewModelFilial()

        FiliaisEditar(navController, viewModel)

    }
}
