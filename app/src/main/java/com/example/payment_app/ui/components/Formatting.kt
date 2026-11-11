package com.example.payment_app.ui.components

import java.util.Locale
import kotlin.math.abs

/**
 * Formats an amount in cents, e.g. 4750 -> "$47.50".
 *
 * @param showPlusSign when true, positive amounts get a leading "+" (used for balances).
 *   Negative amounts always get a leading "-".
 */
fun formatMoney(cents: Long, currencySymbol: String = "$", showPlusSign: Boolean = false): String {
    val absolute = abs(cents)
    val body = String.format(Locale.US, "%s%,d.%02d", currencySymbol, absolute / 100, absolute % 100)
    return when {
        cents < 0 -> "-$body"
        cents > 0 && showPlusSign -> "+$body"
        else -> body
    }
}

/** "1 member", "4 members". */
fun countLabel(count: Int, singular: String, plural: String = "${singular}s"): String =
    "$count ${if (count == 1) singular else plural}"

/** Greeting for the Home header based on the hour of the day (0-23). */
fun greetingForHour(hour: Int): String = when (hour) {
    in 5..11 -> "Good morning"
    in 12..17 -> "Good afternoon"
    else -> "Good evening"
}
