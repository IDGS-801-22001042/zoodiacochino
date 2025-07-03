// Question.kt
package com.example.examen2parcial

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)