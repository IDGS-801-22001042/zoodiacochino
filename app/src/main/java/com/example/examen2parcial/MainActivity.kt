// MainActivity.kt
package com.example.examen2parcial

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavOptionsBuilder // <-- AÑADIR ESTA IMPORTACIÓN para popUpTo
import com.example.examen2parcial.ui.theme.Examen2ParcialTheme
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen2ParcialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val fileHandler = remember { FileHandler(this) }
                    val zodiacCalculator = remember { ZodiacCalculator() }

                    NavHost(navController = navController, startDestination = "personalInfo") {
                        composable("personalInfo") {
                            PersonalInfoScreen(
                                fileHandler = fileHandler,
                                onNavigateToExam = { personalData ->
                                    navController.navigate("examScreen")
                                }
                            )
                        }
                        composable("examScreen") {
                            ExamScreen(
                                fileHandler = fileHandler,
                                onNavigateToResult = { score ->
                                    navController.navigate("resultsScreen")
                                }
                            )
                        }
                        composable("resultsScreen") {
                            ResultScreen(
                                fileHandler = fileHandler,
                                zodiacCalculator = zodiacCalculator,
                                onNavigateToMain = { // <-- DEFINIR EL CALLBACK DE NAVEGACIÓN
                                    navController.navigate("personalInfo") {
                                        // Esto limpia el "back stack" para que PersonalInfoScreen sea la única en la pila
                                        popUpTo("personalInfo") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}