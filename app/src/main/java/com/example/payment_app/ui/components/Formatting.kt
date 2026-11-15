package com.example.payment_app.ui.components

import com.example.payment_app.ui.screens.groups.GroupSummary
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToLong

/** Groups store amounts as Double (62.0); our pages use whole cents (6200). */
fun Double.toCents(): Long = (this * 100).roundToLong()

/** Sum of your balance across all groups, in cents. Positive = owed to you. */
fun netBalanceCents(groups: List<GroupSummary>): Long = groups.sumOf { it.amount.toCents() }

fun formatMoney(cents: Long, currencySymbol: String = "$", showPlusSign: Boolean = false): String {
    val absolute = abs(cents)
    val body = String.format(Locale.US, "%s%,d.%02d", currencySymbol, absolute / 100, absolute % 100)
    return when {
        cents < 0 -> "-$body"
        cents > 0 && showPlusSign -> "+$body"
        else -> body
    }
}

fun countLabel(count: Int, singular: String, plural: String = "${singular}s"): String =
    "$count ${if (count == 1) singular else plural}"

fun greetingForHour(hour: Int): String = when (hour) {
    in 5..11 -> "Good morning"
    in 12..17 -> "Good afternoon"
    else -> "Good evening"
}
