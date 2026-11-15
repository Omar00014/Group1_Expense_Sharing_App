package com.example.payment_app.ui.screens.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.abs

/**
 * Thin aliases onto your theme (Color.kt / Theme.kt), so screens stay readable
 * and all colors live in one place. Change the theme -> every screen follows.
 */
object GroupColors {
    val Bg: Color @Composable get() = MaterialTheme.colorScheme.background            // DarkBackground
    val Card: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainer    // DarkSurface
    val Field: Color @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh // DarkSurfaceHigh
    val Text: Color @Composable get() = MaterialTheme.colorScheme.onBackground        // OnDark
    val Muted: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant   // OnDarkMuted
    val Pill: Color @Composable get() = MaterialTheme.colorScheme.primary             // PillLight
    val OnPill: Color @Composable get() = MaterialTheme.colorScheme.onPrimary         // DarkBackground
    val Avatar: Color @Composable get() = MaterialTheme.colorScheme.primary           // PillLight placeholder
}

fun formatMoney(amount: Double, signed: Boolean = true): String {
    val base = String.format(Locale.US, "\$%.2f", abs(amount))
    return when {
        !signed -> base
        amount < 0 -> "-$base"
        else -> "+$base"
    }
}

@Composable
fun Avatar(modifier: Modifier = Modifier, size: Int = 44) {
    Box(modifier.size(size.dp).clip(CircleShape).background(GroupColors.Avatar))
}

@Composable
fun PillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    filled: Boolean = true,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (filled) GroupColors.Pill else GroupColors.Card,
            contentColor = if (filled) GroupColors.OnPill else GroupColors.Text,
            disabledContainerColor = GroupColors.Pill.copy(alpha = 0.3f),
            disabledContentColor = GroupColors.OnPill.copy(alpha = 0.6f)
        )
    ) { Text(text, style = MaterialTheme.typography.labelLarge, maxLines = 1) }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    leading: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = GroupColors.Muted) },
        leadingIcon = leading,
        singleLine = singleLine,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = GroupColors.Field,
            unfocusedContainerColor = GroupColors.Field,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = GroupColors.Text,
            unfocusedTextColor = GroupColors.Text,
            cursorColor = GroupColors.Text
        )
    )
}

@Composable
fun SectionLabel(text: String, trailing: String? = null, onTrailingClick: (() -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, color = GroupColors.Muted, style = MaterialTheme.typography.labelMedium)
        if (trailing != null) {
            Text(
                trailing, color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall,
                modifier = if (onTrailingClick != null) Modifier.clickable { onTrailingClick() } else Modifier
            )
        }
    }
}

/** Big centered number block ("You are owed (net)" / "$62.00"). */
@Composable
fun BigHeader(label: String, value: String) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(4.dp))
        Text(value, color = GroupColors.Text, style = MaterialTheme.typography.displaySmall)
    }
}

/** Generic dark card row: avatar + title/subtitle on the left, value/caption on the right. */
@Composable
fun ListRowCard(
    title: String,
    subtitle: String,
    endTitle: String? = null,
    endCaption: String? = null,
    endCaptionUnderlined: Boolean = false,
    onEndCaptionClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(GroupColors.Card)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar()
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = GroupColors.Text, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall)
        }
        if (endTitle != null) {
            Column(horizontalAlignment = Alignment.End) {
                Text(endTitle, color = GroupColors.Text, style = MaterialTheme.typography.titleSmall)
                if (endCaption != null) {
                    Text(
                        endCaption, color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall,
                        textDecoration = if (endCaptionUnderlined) TextDecoration.Underline else null,
                        modifier = if (onEndCaptionClick != null) Modifier.clickable { onEndCaptionClick() } else Modifier
                    )
                }
            }
        }
        trailing?.invoke()
    }
}

// ---------- Models + sample data (replace with real data / ViewModel later) ----------

data class GroupSummary(
    val id: String,
    val name: String,
    val subtitle: String,   // "4 members · 3 expenses"
    val amount: Double,     // + owed to you, - you owe
    val description: String = ""
)

data class Member(
    val name: String,
    val username: String,
    val balance: Double = 0.0,
    val note: String = "",  // "You · @alex", "1 paid expense"
    val isYou: Boolean = false,
    val isCreator: Boolean = false
)

data class ActivityItem(val title: String, val subtitle: String, val amount: Double)

// Sample data matching the lo-fi models – replace with real data / ViewModel later
val sampleGroups = listOf(
    GroupSummary("1", "Weekend Trip", "4 members · 3 expenses", 62.0, "Shared expenses for our weekend trip."),
    GroupSummary("2", "Apartment", "3 members · 7 expenses", -14.5)
)
val sampleMembers = listOf(
    Member("Alex Taylor", "@alex", 62.0, "You · @alex", isYou = true, isCreator = true),
    Member("Jordan", "@jordan", -22.0, "1 paid expense"),
    Member("Sam", "@sam", -40.0, "@sam"),
    Member("Casey", "@casey", 0.0, "@casey")
)
val sampleActivity = listOf(
    ActivityItem("Jordan paid Dinner", "Split 4 ways", 88.0),
    ActivityItem("Sam settled up", "2d ago", 22.0)
)