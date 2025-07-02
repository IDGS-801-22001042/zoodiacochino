// PersonalInfoScreen.kt
package com.example.examen2parcial

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime // <-- AÑADIR ESTA IMPORTACIÓN
import java.time.format.DateTimeFormatter // <-- AÑADIR ESTA IMPORTACIÓN

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(fileHandler: FileHandler, onNavigateToExam: (PersonalData) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apaterno by remember { mutableStateOf("") }
    var amaterno by remember { mutableStateOf("") }
    var diaNacimiento by remember { mutableStateOf("") }
    var mesNacimiento by remember { mutableStateOf("") }
    var anioNacimiento by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var showErrorMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Datos Personales", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it; showErrorMessage = false },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrorMessage && nombre.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = apaterno,
            onValueChange = { apaterno = it; showErrorMessage = false },
            label = { Text("Apellido Paterno") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrorMessage && apaterno.isBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amaterno,
            onValueChange = { amaterno = it; showErrorMessage = false },
            label = { Text("Apellido Materno") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrorMessage && amaterno.isBlank()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Fecha de nacimiento", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = diaNacimiento,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) diaNacimiento = it
                    showErrorMessage = false
                },
                label = { Text("Día") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                isError = showErrorMessage && (diaNacimiento.toIntOrNull() == null || diaNacimiento.toIntOrNull() == 0)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = mesNacimiento,
                onValueChange = {
                    if (it.length <= 2 && it.all { char -> char.isDigit() }) mesNacimiento = it
                    showErrorMessage = false
                },
                label = { Text("Mes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                isError = showErrorMessage && (mesNacimiento.toIntOrNull() == null || mesNacimiento.toIntOrNull() == 0)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = anioNacimiento,
                onValueChange = {
                    if (it.length <= 4 && it.all { char -> char.isDigit() }) anioNacimiento = it
                    showErrorMessage = false
                },
                label = { Text("Año") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                isError = showErrorMessage && (anioNacimiento.toIntOrNull() == null || anioNacimiento.toIntOrNull() == 0)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Sexo", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = sexo == "Masculino",
                    onClick = { sexo = "Masculino"; showErrorMessage = false }
                )
                Text("Masculino")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = sexo == "Femenino",
                    onClick = { sexo = "Femenino"; showErrorMessage = false }
                )
                Text("Femenino")
            }
        }
        if (showErrorMessage) {
            Text(
                text = "Por favor, complete todos los campos y asegúrese de que la fecha y el sexo sean válidos.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                nombre = ""
                apaterno = ""
                amaterno = ""
                diaNacimiento = ""
                mesNacimiento = ""
                anioNacimiento = ""
                sexo = ""
                showErrorMessage = false
            }) {
                Text("Limpiar")
            }
            Button(onClick = {
                val isNombreValid = nombre.isNotBlank()
                val isApaternoValid = apaterno.isNotBlank()
                val isAmaternoValid = amaterno.isNotBlank()
                val isDiaValid = diaNacimiento.toIntOrNull() != null && diaNacimiento.toIntOrNull() != 0
                val isMesValid = mesNacimiento.toIntOrNull() != null && mesNacimiento.toIntOrNull() != 0
                val isAnioValid = anioNacimiento.toIntOrNull() != null && anioNacimiento.toIntOrNull() != 0
                val isSexoValid = sexo.isNotBlank()

                if (isNombreValid && isApaternoValid && isAmaternoValid &&
                    isDiaValid && isMesValid && isAnioValid && isSexoValid) {
                    val personalData = PersonalData(
                        nombre = nombre,
                        apaterno = apaterno,
                        amaterno = amaterno,
                        diaNacimiento = diaNacimiento.toInt(),
                        mesNacimiento = mesNacimiento.toInt(),
                        anioNacimiento = anioNacimiento.toInt(),
                        sexo = sexo
                    )

                    // Generar marca de tiempo para el registro histórico
                    val currentDateTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val timestamp = currentDateTime.format(formatter)

                    // Formatear la cadena de datos personales con marca de tiempo para añadir al archivo
                    val personalDataString = "$timestamp|${personalData.nombre},${personalData.apaterno}," +
                            "${personalData.amaterno},${personalData.diaNacimiento}," +
                            "${personalData.mesNacimiento},${personalData.anioNacimiento}," +
                            personalData.sexo

                    // Guardar datos personales, añadiendo al archivo
                    fileHandler.writeToFile("personal_data.txt", personalDataString, true) // <-- GUARDAR EN MODO APPEND

                    onNavigateToExam(personalData)
                } else {
                    showErrorMessage = true
                }
            }) {
                Text("Siguiente")
            }
        }
    }
}