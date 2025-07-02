// FileHandler.kt
package com.example.examen2parcial

import android.content.Context
import java.io.*
import android.util.Log // Asegúrate de tener esta importación

class FileHandler(private val context: Context) {

    private val TAG = "FileHandler" // Etiqueta para tus logs

    fun writeToFile(fileName: String, content: String, append: Boolean = false) { // <-- AÑADIR PARÁMETRO 'append'
        try {
            val file = File(context.filesDir, fileName)
            FileOutputStream(file, append).use { // <-- Usar el parámetro 'append' aquí
                it.write(content.toByteArray())
                if (append) {
                    it.write("\n".toByteArray()) // <-- AÑADIR SALTO DE LÍNEA AL FINAL SI SE ESTÁ AÑADIENDO
                }
            }
            Log.d(TAG, "Successfully wrote to file: $fileName (append=$append)")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "Error writing to file $fileName: ${e.message}")
        }
    }

    fun readFromFile(fileName: String): String? {
        return try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                FileInputStream(file).use {
                    val content = it.bufferedReader().readText()
                    Log.d(TAG, "Successfully read from file: $fileName. Content length: ${content.length}")
                    content
                }
            } else {
                Log.d(TAG, "File not found: $fileName")
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "Error reading from file $fileName: ${e.message}")
            null
        }
    }
}