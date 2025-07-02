// ExamScreen.kt
package com.example.examen2parcial

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import kotlin.math.roundToInt
import java.time.LocalDateTime // <-- AÑADIR ESTA IMPORTACIÓN
import java.time.format.DateTimeFormatter // <-- AÑADIR ESTA IMPORTACIÓN

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamScreen(fileHandler: FileHandler, onNavigateToResult: (Int) -> Unit) {
    val questions = remember {
        listOf(
            Question("¿Cuál es la suma de 2+2?", listOf("8", "6", "4", "3"), 2),
            Question("¿Cuántos días tiene una semana?", listOf("5", "7", "10", "3"), 1),
            Question("¿Cuál es la capital de Francia?", listOf("Madrid", "Berlín", "París", "Roma"), 2),
            Question("¿Qué planeta es conocido como el Planeta Rojo?", listOf("Venus", "Júpiter", "Marte", "Saturno"), 2),
            Question("¿Quién escribió 'Don Quijote de la Mancha'?", listOf("Gabriel García Márquez", "Miguel de Cervantes", "Pablo Neruda", "Jorge Luis Borges"), 1),
            Question("¿Cuál es el río más largo del mundo?", listOf("Nilo", "Amazonas", "Mississippi", "Yangtsé"), 1)
        )
    }

    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Examen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            questions.forEachIndexed { qIndex, question ->
                Text(text = "${qIndex + 1}.- ${question.questionText}", style = MaterialTheme.typography.titleMedium)
                Column {
                    question.options.forEachIndexed { oIndex, option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedAnswers[qIndex] == oIndex,
                                onClick = {
                                    selectedAnswers[qIndex] = oIndex
                                }
                            )
                            Text(text = option)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                var correctAnswers = 0
                questions.forEachIndexed { qIndex, question ->
                    if (selectedAnswers[qIndex] == question.correctAnswerIndex) {
                        correctAnswers++
                    }
                }

                val totalQuestions = questions.size.toDouble()
                val maxScoreDesired = 10.0

                val scaledScore = if (totalQuestions > 0) {
                    (correctAnswers / totalQuestions) * maxScoreDesired
                } else {
                    0.0
                }

                val finalScore = scaledScore.roundToInt()

                // Generar marca de tiempo para el registro histórico
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val timestamp = currentDateTime.format(formatter)

                // Formatear la cadena de resultado del examen con marca de tiempo para añadir al archivo
                val examResultString = "$timestamp|$finalScore"

                // Guardar resultado del examen, añadiendo al archivo
                fileHandler.writeToFile("exam_results.txt", examResultString, true) // <-- GUARDAR EN MODO APPEND

                onNavigateToResult(finalScore)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Terminar Examen")
        }
    }
}