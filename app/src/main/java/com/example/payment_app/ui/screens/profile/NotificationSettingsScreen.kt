package com.example.payment_app.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payment_app.ui.theme.Payment_AppTheme


@Composable
fun NotificationSettingsScreen(
    settings: NotificationSettings,
    onSave: (NotificationSettings) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var draft by rememberSaveable(stateSaver = NotificationSettingsSaver) {
        mutableStateOf(settings)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        // Top bar: circular back button + centered title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Surface(
                onClick = onBack,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterStart)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                "Settings",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(Modifier.height(12.dp))
        ScreenTitle("Notification settings")
        Spacer(Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ToggleRow(
                title = "New expense posted",
                subtitle = "New group expenses",
                checked = draft.newExpensePosted,
                onCheckedChange = { draft = draft.copy(newExpensePosted = it) }
            )
            ToggleRow(
                title = "Expense updates",
                subtitle = "Expense and member changes",
                checked = draft.expenseUpdates,
                onCheckedChange = { draft = draft.copy(expenseUpdates = it) }
            )
            ToggleRow(
                title = "Payment reminders",
                subtitle = "When your share is due",
                checked = draft.paymentReminders,
                onCheckedChange = { draft = draft.copy(paymentReminders = it) }
            )

            Text(
                "DELIVERY",
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 0.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            ToggleRow(
                title = "Push notifications",
                subtitle = "Alerts on this device",
                checked = draft.pushNotifications,
                onCheckedChange = { draft = draft.copy(pushNotifications = it) }
            )
            ToggleRow(
                title = "Email summaries",
                subtitle = "A weekly activity digest",
                checked = draft.emailSummaries,
                onCheckedChange = { draft = draft.copy(emailSummaries = it) }
            )
            ToggleRow(
                title = "Background sync",
                subtitle = "Keep balances up to date",
                checked = draft.backgroundSync,
                onCheckedChange = { draft = draft.copy(backgroundSync = it) }
            )
        }

        Spacer(Modifier.height(14.dp))
        PrimaryPillButton(
            text = "Save notification settings",
            onClick = { onSave(draft) }
        )
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    SettingsCard(onClick = { onCheckedChange(!checked) }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                RowTitle(title)
                RowSubtitle(subtitle)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.background,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedBorderColor = Color.Transparent,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun NotificationSettingsPreview() {
    Payment_AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NotificationSettingsScreen(
                settings = NotificationSettings(),
                onSave = {},
                onBack = {}
            )
        }
    }
}