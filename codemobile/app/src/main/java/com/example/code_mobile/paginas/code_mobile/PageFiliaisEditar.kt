package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.code_mobile.paginas.code_mobile.viewModel.ViewModelFiliais
import com.example.code_mobile.ui.theme.CodemobileTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.unit.dp
import com.example.code_mobile.paginas.code_mobile.model.ModelFiliais





import androidx.compose.ui.window.Dialog


@Composable
fun  FiliaisEditar(
    filial: ModelFiliais,
    onDismiss: () -> Unit,
    onSalvar: (ModelFiliais) -> Unit
) {
    var cepEditado by remember { mutableStateOf(filial.cep) }
    var lagradouroEditado by remember { mutableStateOf(filial.lagradouro) }
    var bairroEditado by remember { mutableStateOf(filial.bairro) }
    var cidadeEditado by remember { mutableStateOf(filial.cidade) }
    var estadoEditado by remember { mutableStateOf(filial.estado) }
    var complementoEditado by remember { mutableStateOf(filial.complemento) }
    var numEditado by remember { mutableStateOf(filial.num.toString()) }
    var statusEditado by remember { mutableStateOf(filial.status) }

    var erroCep by remember { mutableStateOf<String?>(null) }
    var erroLagradouro by remember { mutableStateOf<String?>(null) }
    var erroBairro by remember { mutableStateOf<String?>(null) }
    var erroCidade by remember { mutableStateOf<String?>(null) }
    var erroEstado by remember { mutableStateOf<String?>(null) }
    var erroNum by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFF121212)), // Defina a cor de fundo usando o modificador
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Editar Filial", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = cepEditado,
                    onValueChange = {
                        cepEditado = it
                        erroCep = null
                    },
                    label = { Text("CEP") },
                    isError = erroCep != null,
                    textStyle = TextStyle(color = Color.White)
                )
                erroCep?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lagradouroEditado,
                    onValueChange = {
                        lagradouroEditado = it
                        erroLagradouro = null
                    },
                    label = { Text("Logradouro") },
                    isError = erroLagradouro != null,
                    textStyle = TextStyle(color = Color.White)
                )
                erroLagradouro?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = bairroEditado,
                    onValueChange = {
                        bairroEditado = it
                        erroBairro = null
                    },
                    label = { Text("Bairro") },
                    isError = erroBairro != null,
                    textStyle = TextStyle(color = Color.White)
                )
                erroBairro?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cidadeEditado,
                    onValueChange = {
                        cidadeEditado = it
                        erroCidade = null
                    },
                    label = { Text("Cidade") },
                    isError = erroCidade != null,
                    textStyle = TextStyle(color = Color.White)
                )
                erroCidade?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = estadoEditado,
                    onValueChange = {
                        estadoEditado = it
                        erroEstado = null
                    },
                    label = { Text("Estado") },
                    isError = erroEstado != null,
                    textStyle = TextStyle(color = Color.White)
                )
                erroEstado?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = complementoEditado,
                    onValueChange = { complementoEditado = it },
                    label = { Text("Complemento") },
                    textStyle = TextStyle(color = Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = numEditado,
                    onValueChange = {
                        numEditado = it
                        erroNum = null
                    },
                    label = { Text("Número") },
                    isError = erroNum != null,
                    textStyle = TextStyle(color = Color.White)
                )
                erroNum?.let {
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Dropdown para selecionar o status da filial
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = statusEditado,
                        onValueChange = { /* Não deve ser editável diretamente */ },
                        label = { Text("Status") },
                        readOnly = true,
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Operante", "Inoperante").forEach { statusOption ->
                            DropdownMenuItem(onClick = {
                                statusEditado = statusOption
                                expanded = false
                            }) {
                                Text(statusOption, color = Color.White)
                            }
                        }
                    }
                    // TODO: Adicionar um ícone para expandir o dropdown
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                    Button(
                        onClick = {
                            var isValid = true
                            if (cepEditado.isBlank()) {
                                erroCep = "O CEP é obrigatório."
                                isValid = false
                            }
                            if (lagradouroEditado.isBlank()) {
                                erroLagradouro = "O Logradouro é obrigatório."
                                isValid = false
                            }
                            if (bairroEditado.isBlank()) {
                                erroBairro = "O Bairro é obrigatório."
                                isValid = false
                            }
                            if (cidadeEditado.isBlank()) {
                                erroCidade = "A Cidade é obrigatória."
                                isValid = false
                            }
                            if (estadoEditado.isBlank()) {
                                erroEstado = "O Estado é obrigatório."
                                isValid = false
                            }
                            if (numEditado.isBlank() || numEditado.toIntOrNull() == null) {
                                erroNum = "O Número é obrigatório e deve ser um número."
                                isValid = false
                            }

                            if (isValid) {
                                onSalvar(
                                    filial.copy(
                                        cep = cepEditado,
                                        lagradouro = lagradouroEditado,
                                        bairro = bairroEditado,
                                        cidade = cidadeEditado,
                                        estado = estadoEditado,
                                        complemento = complementoEditado,
                                        num = numEditado.toInt(),
                                        status = statusEditado
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFE91E63))
                    ) {
                        Text("Salvar", color = Color.White)
                    }
                }
            }
        }
    }
}





//@Composable
//fun FiliaisEditarPreview() {
//    CodemobileTheme {
//        // Inicialize o navController aqui
//        val navController = rememberNavController()
//        FiliaisEditar(navController)
//    }
//}
