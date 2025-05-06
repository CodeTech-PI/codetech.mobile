package com.example.code_mobile.paginas.code_mobile.atendimento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code_mobile.paginas.code_mobile.textPadrao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentAtendimentoEtapa1(
    data: String,
    onDataChange: (String) -> Unit,
    horario: String,
    onHorarioChange: (String) -> Unit,
    cancelado: Boolean,
    onCanceladoChange: (Boolean) -> Unit,
    clienteSelecionado: String?,
    onClienteSelecionadoChange: (String?) -> Unit,
    nomeCliente: String,
    onNomeClienteChange: (String) -> Unit,
    telefoneCliente: String,
    onTelefoneClienteChange: (String) -> Unit,
    emailCliente: String,
    onEmailClienteChange: (String) -> Unit,
    dataNascimentoCliente: String,
    onDataNascimentoClienteChange: (String) -> Unit,
    cpfCliente: String,
    onCpfClienteChange: (String) -> Unit,
    onProximoClick: () -> Unit,
    clientesCadastrados: List<String>
) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            "Informações do Atendimento",
            style = textPadrao.copy(fontSize = 20.sp, color = Color.White),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = data,
            onValueChange = onDataChange,
            label = { Text("Data", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = horario,
            onValueChange = onHorarioChange,
            label = { Text("Horário", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = cancelado,
                onCheckedChange = onCanceladoChange
            )
            Text("Cancelado", style = textPadrao.copy(color = Color.White))
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown para Selecionar o Cliente
        var expandedCliente by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedCliente,
            onExpandedChange = { expandedCliente = !expandedCliente }
        ) {
            TextField(
                value = clienteSelecionado ?: "",
                onValueChange = onClienteSelecionadoChange,
                label = { Text("Selecione o cliente", style = TextStyle(color = Color.LightGray)) },
                readOnly = true,
                textStyle = TextStyle(color = Color.White),
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    disabledTextColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF2B2B2B),
                    unfocusedContainerColor = Color(0xFF2B2B2B)
                ),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedCliente,
                onDismissRequest = { expandedCliente = false }
            ) {
                clientesCadastrados.forEach { cliente ->
                    DropdownMenuItem(
                        text = { Text(cliente, color = Color.White) },
                        onClick = {
                            onClienteSelecionadoChange(cliente)
                            onNomeClienteChange(cliente)
                            // AQUI: Buscar os dados do cliente (telefone, email, etc.)
                            // e atualizar os estados correspondentes no PageAtendimento
                            expandedCliente = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = nomeCliente,
            onValueChange = onNomeClienteChange,
            label = { Text("Nome", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = telefoneCliente,
            onValueChange = onTelefoneClienteChange,
            label = { Text("Telefone", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emailCliente,
            onValueChange = onEmailClienteChange,
            label = { Text("Email", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dataNascimentoCliente,
            onValueChange = onDataNascimentoClienteChange,
            label = { Text("Data de Nascimento", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cpfCliente,
            onValueChange = onCpfClienteChange,
            label = { Text("CPF", style = TextStyle(color = Color.LightGray)) },
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color(0xFF2B2B2B),
                unfocusedContainerColor = Color(0xFF2B2B2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(
                onClick = onProximoClick,
                colors = ButtonDefaults.buttonColors(Color(0xFFDF0050))
            ) {
                Text("Próximo", color = Color.White)
            }
        }
    }
}