// Question.kt
package com.example.examen2parcial

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int // Index of the correct answer in the options list
)