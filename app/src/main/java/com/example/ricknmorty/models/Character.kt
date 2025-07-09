package com.example.ricknmorty.models

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Serializable
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Location,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
) {
    fun whenCreated(): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val createdDate = LocalDate.parse(created, formatter)
        val currentDate = LocalDate.now()
        val period = Period.between(createdDate, currentDate)

        return when {
            period.years > 0 -> "${period.years} years ago"
            period.months > 0 -> "${period.months} months ago"
            period.days > 0 -> "${period.days} days ago"
            else -> "Today"
        }
    }
}
