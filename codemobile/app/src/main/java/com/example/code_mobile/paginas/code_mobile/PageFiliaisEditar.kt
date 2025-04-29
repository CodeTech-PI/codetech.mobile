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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.code_mobile.paginas.code_mobile.CampoFilial
import com.example.code_mobile.paginas.code_mobile.CampoFilialStatus
import com.example.code_mobile.paginas.code_mobile.model.ModelFiliais
import com.example.code_mobile.paginas.code_mobile.viewModel.filial.ViewModelFilial
import com.example.code_mobile.ui.theme.CodemobileTheme

@Composable
fun FiliaisEditar(
    navController: NavController,
    viewModel: ViewModelFilial = viewModel(),
    filial: ModelFiliais, // Filial selecionada para edição
    modifier: Modifier = Modifier
) {
    // Preenche os campos com os dados da filial selecionada
    LaunchedEffect(Unit) {
        viewModel.preencherCamposParaEdicao(filial)
    }


    val cep by viewModel.cep
    val logradouro by viewModel.logradouro
    val bairro by viewModel.bairro
    val cidade by viewModel.cidade
    val estado by viewModel.estado
    val num by viewModel.num

    val scrollState = rememberScrollState()

    val textPadrao = TextStyle(
        fontSize = 20.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFF1B1B1B))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Editar",
            style = textPadrao.copy(fontSize = 30.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(20.dp))
        CampoFilial("CEP:", cep, onValorChange = viewModel::atualizarCep, textStyle = textPadrao, placeholderText = "")
        Spacer(modifier = Modifier.height(20.dp))
        CampoFilial("Logradouro:", logradouro, onValorChange = viewModel::atualizarlogradouro, textStyle = textPadrao, placeholderText = "")
        Spacer(modifier = Modifier.height(20.dp))
        CampoFilial("Bairro:", bairro, onValorChange = viewModel::atualizarBairro, textStyle = textPadrao, placeholderText = "")
        Spacer(modifier = Modifier.height(20.dp))
        CampoFilial("Cidade:", cidade, onValorChange = viewModel::atualizarCidade, textStyle = textPadrao, placeholderText = "")
        Spacer(modifier = Modifier.height(20.dp))
        CampoFilial("Estado:", estado, onValorChange = viewModel::atualizarEstado, textStyle = textPadrao, placeholderText = "")
        Spacer(modifier = Modifier.height(20.dp))
        CampoFilial("Número:", num, onValorChange = viewModel::atualizarNum, textStyle = textPadrao, placeholderText = "")

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.editarFilial(filial)
                    navController.navigate("Filiais")
                },
                modifier = Modifier
                    .width(130.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text(text = "Salvar")
            }

            Button(
                onClick = { navController.navigate("Filiais") },
                modifier = Modifier
                    .width(130.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF252525))
            ) {
                Text(text = "Cancelar")
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
        val filial = ModelFiliais(
            id = 1,
            status = "Operante",
            cep = "19141010",
            logradouro = "Rua Caraibas",
            bairro = "Emilio Marengo",
            cidade = "São Paulo",
            estado = "SP",
            num = 10,
            complemento = "casa 2"
        );
        FiliaisEditar(navController, viewModel, filial)
    }
}
