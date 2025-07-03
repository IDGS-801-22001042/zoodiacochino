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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExamScreen(fileHandler: FileHandler, onNavigateToResult: (Int) -> Unit) {
    val questions = remember {
        listOf(
            Question(
                "¿Cuál es el elemento químico más abundante en la corteza terrestre?",
                listOf("Oxígeno", "Silicio", "Aluminio", "Hierro"),
                0 // Oxígeno
            ),
            Question(
                "¿En qué año llegó el hombre a la Luna?",
                listOf("1965", "1969", "1972", "1961"),
                1 // 1969
            ),
            Question(
                "¿Cuál es el océano más grande del mundo?",
                listOf("Atlántico", "Índico", "Ártico", "Pacífico"),
                3 // Pacífico
            ),
            Question(
                "¿Qué instrumento se utiliza para medir la presión atmosférica?",
                listOf("Termómetro", "Anemómetro", "Barómetro", "Higrómetro"),
                2 // Barómetro
            ),
            Question(
                "¿Quién pintó la Mona Lisa?",
                listOf("Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Claude Monet"),
                2 // Leonardo da Vinci
            ),
            Question(
                "¿Cuál es la capital de Japón?",
                listOf("Seúl", "Pekín", "Tokio", "Bangkok"),
                2 // Tokio
            )
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