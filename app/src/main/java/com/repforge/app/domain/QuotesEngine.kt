package com.repforge.app.domain

import kotlin.random.Random

object QuotesEngine {
    private val quotes = listOf(
        "The iron never lies.",
        "Discipline equals freedom.",
        "Strength is built one rep at a time.",
        "Consistency creates champions.",
        "Pain is temporary. Pride is forever.",
        "Don't count the days, make the days count.",
        "Success starts outside your comfort zone."
    )

    fun getRandomQuote(): String {
        return quotes[Random.nextInt(quotes.size)]
    }
}
