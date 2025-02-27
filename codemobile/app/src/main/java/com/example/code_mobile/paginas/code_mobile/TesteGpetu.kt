package com.example.code_mobile.paginas.code_mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFFF6699), shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "João Pereira", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "456.789.012-33", fontSize = 14.sp, color = Color.Gray)
                Text(text = "10/01/1992", fontSize = 14.sp, color = Color.Gray)
                Text(text = "(11) 977665544", fontSize = 14.sp, color = Color.White)
                Text(text = "joao.pereira@yahoo.com", fontSize = 14.sp, color = Color.White)
            }
        }
    }
}

