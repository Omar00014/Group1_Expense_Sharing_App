package com.example.payment_app.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payment_app.ui.theme.Payment_AppTheme
import java.util.UUID

@Composable
fun AddEditCardScreen(
    cardToEdit: PaymentCard?,
    onSaveCard: (PaymentCard) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditing = cardToEdit != null

    var cardholderName by rememberSaveable { mutableStateOf(cardToEdit?.cardholderName ?: "Alex Taylor") }
    var cardNumber by rememberSaveable { mutableStateOf(cardToEdit?.cardNumberMasked ?: "•••• •••• •••• 4242") }
    var expiryDate by rememberSaveable { mutableStateOf(cardToEdit?.expiryDate ?: "12/28") }
    var securityCode by rememberSaveable { mutableStateOf("") }
    var isDefault by rememberSaveable { mutableStateOf(cardToEdit?.isDefault ?: true) }

    val brand = if (cardNumber.startsWith("5") || cardNumber.contains("1881")) CardBrand.MASTERCARD else CardBrand.VISA

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Top Bar
            PaymentTopBar(
                title = "Add / edit card",
                onBack = onCancel
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Card details",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaTextPrimary
                        )
                    )
                    Text(
                        text = "Manage the card you use to pay your share.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            color = FigmaTextMuted
                        )
                    )
                }

                // Fields container
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PaymentInputField(
                        value = cardholderName,
                        onValueChange = { cardholderName = it },
                        labelRight = "Cardholder",
                        placeholder = "Name on card",
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                    )

                    PaymentInputField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        labelRight = brand.displayName,
                        placeholder = "Card number",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    PaymentInputField(
                        value = expiryDate,
                        onValueChange = { expiryDate = it },
                        labelRight = "Expires",
                        placeholder = "MM/YY",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    PaymentInputField(
                        value = securityCode,
                        onValueChange = { if (it.length <= 4) securityCode = it },
                        labelRight = "Security code",
                        placeholder = "•••",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }

                // Default card toggle row
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = FigmaSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Set as default card",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = FigmaTextPrimary
                                )
                            )
                            Text(
                                text = "Used for future group payments",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = FigmaTextMuted
                                )
                            )
                        }

                        Switch(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = FigmaTextDark,
                                checkedTrackColor = FigmaPrimaryPill,
                                uncheckedThumbColor = FigmaTextMuted,
                                uncheckedTrackColor = FigmaSurfaceHigh
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bottom actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryPill(
                text = "Cancel",
                onClick = onCancel,
                height = 44
            )

            PrimaryPill(
                text = "Save card",
                onClick = {
                    val last4Digits = if (cardNumber.length >= 4) {
                        cardNumber.takeLast(4)
                    } else {
                        "4242"
                    }
                    val finalCard = PaymentCard(
                        id = cardToEdit?.id ?: UUID.randomUUID().toString(),
                        cardholderName = cardholderName.ifBlank { "Alex Taylor" },
                        cardNumberMasked = if (cardNumber.contains("••••")) cardNumber else "•••• •••• •••• $last4Digits",
                        last4 = last4Digits,
                        expiryDate = expiryDate.ifBlank { "12/28" },
                        brand = brand,
                        isDefault = isDefault,
                        isBackup = !isDefault && (cardToEdit?.isBackup ?: false)
                    )
                    onSaveCard(finalCard)
                },
                height = 44
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun AddEditCardScreenPreview() {
    Payment_AppTheme {
        Surface(color = FigmaBackground) {
            AddEditCardScreen(
                cardToEdit = defaultSavedCards.first(),
                onSaveCard = {},
                onCancel = {}
            )
        }
    }
}
