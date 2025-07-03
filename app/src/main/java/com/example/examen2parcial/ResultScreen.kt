// ResultScreen.kt
package com.example.examen2parcial

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResultScreen(fileHandler: FileHandler, zodiacCalculator: ZodiacCalculator, onNavigateToMain: () -> Unit) { // <-- AÑADIR NUEVO PARÁMETRO DE NAVEGACIÓN
    var personalData by remember { mutableStateOf<PersonalData?>(null) }
    var examScore by remember { mutableStateOf<Int?>(null) }
    var showFileContentDialog by remember { mutableStateOf(false) }
    var fileContentToShow by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val personalDataString = fileHandler.readFromFile("personal_data.txt")
        personalDataString?.let {
            // Leer solo la última entrada no vacía
            val lastEntry = it.split("\n").lastOrNull { line -> line.isNotBlank() }
            lastEntry?.let { entry ->
                // Remover la marca de tiempo para parsear los datos
                val contentWithoutTimestamp = entry.substringAfter("|")
                val parts = contentWithoutTimestamp.split(",")
                if (parts.size == 7) {
                    personalData = PersonalData(
                        nombre = parts[0],
                        apaterno = parts[1],
                        amaterno = parts[2],
                        diaNacimiento = parts[3].toIntOrNull() ?: 0,
                        mesNacimiento = parts[4].toIntOrNull() ?: 0,
                        anioNacimiento = parts[5].toIntOrNull() ?: 0,
                        sexo = parts[6]
                    )
                }
            }
        } ?: run {
            println("No se pudo leer personal_data.txt o no existe.")
        }

        val examScoreString = fileHandler.readFromFile("exam_results.txt")
        examScoreString?.let {
            // Leer solo la última entrada no vacía
            val lastEntry = it.split("\n").lastOrNull { line -> line.isNotBlank() }
            lastEntry?.let { entry ->
                // Remover la marca de tiempo para parsear el score
                val contentWithoutTimestamp = entry.substringAfter("|")
                examScore = contentWithoutTimestamp.toIntOrNull()
            }
        } ?: run {
            println("No se pudo leer exam_results.txt o no existe.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        personalData?.let { data ->
            val age = zodiacCalculator.calculateAge(data.diaNacimiento, data.mesNacimiento, data.anioNacimiento)
            val (zodiacName, zodiacImageResId) = zodiacCalculator.getChineseZodiac(data.anioNacimiento)

            Text(
                text = "Hola ${data.nombre} ${data.apaterno} ${data.amaterno}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tienes $age años y tu signo zodiacal",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (zodiacImageResId != 0) {
                Image(
                    painter = painterResource(id = zodiacImageResId),
                    contentDescription = zodiacName,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = "Es $zodiacName",
                style = MaterialTheme.typography.bodyLarge
            )
        } ?: run {
            Text("Cargando datos personales o datos no válidos...")
        }

        Spacer(modifier = Modifier.height(24.dp))

        examScore?.let { score ->
            Text(
                text = "Calificación: $score",
                style = MaterialTheme.typography.headlineLarge
            )
        } ?: run {
            Text("Cargando calificación o calificación no disponible...")
        }

        Spacer(modifier = Modifier.height(24.dp))


        Button(onClick = {
            val personalDataContent = fileHandler.readFromFile("personal_data.txt") ?: "No se encontró personal_data.txt"
            val examResultsContent = fileHandler.readFromFile("exam_results.txt") ?: "No se encontró exam_results.txt"
            fileContentToShow = "Contenido de personal_data.txt:\n$personalDataContent\n\n" +
                    "Contenido de exam_results.txt:\n$examResultsContent"
            showFileContentDialog = true
        }) {
            Text("Mostrar Registros del TXT")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = onNavigateToMain) {
            Text("Ir a la Página Principal")
        }
    }

    if (showFileContentDialog) {
        AlertDialog(
            onDismissRequest = { showFileContentDialog = false },
            title = { Text("Contenido de Archivos Registrados") },
            text = { Text(fileContentToShow) },
            confirmButton = {
                TextButton(onClick = { showFileContentDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}