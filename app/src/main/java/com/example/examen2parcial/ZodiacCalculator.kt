// ZodiacCalculator.kt
package com.example.examen2parcial

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period

class ZodiacCalculator {

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(day: Int, month: Int, year: Int): Int {
        val birthDate = LocalDate.of(year, month, day)
        val currentDate = LocalDate.now()
        return Period.between(birthDate, currentDate).years
    }

    fun getChineseZodiac(year: Int): Pair<String, Int> {
        val zodiacAnimals = listOf(
            "Rata", "Buey", "Tigre", "Conejo", "Dragón", "Serpiente",
            "Caballo", "Cabra", "Mono", "Gallo", "Perro", "Cerdo"
        )
        val zodiacImages = mapOf(
            "Rata" to R.drawable.rata,
            "Buey" to R.drawable.buey,
            "Tigre" to R.drawable.tigre,
            "Conejo" to R.drawable.conejo,
            "Dragón" to R.drawable.dragon,
            "Serpiente" to R.drawable.serpiente,
            "Caballo" to R.drawable.caballo,
            "Cabra" to R.drawable.cabra,
            "Mono" to R.drawable.mono,
            "Gallo" to R.drawable.gallo,
            "Perro" to R.drawable.perro,
            "Cerdo" to R.drawable.cerdo
        )

        val startYear = 1900
        val offset = (year - startYear) % 12
        val zodiac = zodiacAnimals[offset]
        return Pair(zodiac, zodiacImages[zodiac] ?: 0)
    }
}