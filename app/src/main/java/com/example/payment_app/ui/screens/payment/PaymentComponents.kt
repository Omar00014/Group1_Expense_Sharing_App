package com.example.payment_app.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Figma design tokens
val FigmaBackground = Color(0xFF181B22)
val FigmaSurface = Color(0xFF23262F)
val FigmaSurfaceHigh = Color(0xFF2E3140)
val FigmaPrimaryPill = Color(0xFFEEEEEE)
val FigmaTextPrimary = Color(0xFFEDEDED)
val FigmaTextMuted = Color(0xFFB0B5BF)
val FigmaTextDark = Color(0xFF181B22)
val FigmaBorder = Color(0xFF454957)

@Composable
fun PaymentTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    endIcon: ImageVector? = null,
    onEndClick: (() -> Unit)? = null,
    backLabel: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (backLabel != null) {
            // Screen 25 style: icon + "Back" label
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onBack)
                    .padding(vertical = 4.dp, horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = FigmaTextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = backLabel,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FigmaTextMuted
                    )
                )
            }
        } else {
            // Circular 36dp button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(FigmaSurface)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = FigmaTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = FigmaTextMuted
            )
        )

        if (endIcon != null && onEndClick != null) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(FigmaSurface)
                    .clickable(onClick = onEndClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = endIcon,
                    contentDescription = null,
                    tint = FigmaTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            // Balancer box to keep title centered if using circular back button
            Spacer(modifier = Modifier.size(if (backLabel != null) 0.dp else 36.dp))
        }
    }
}

@Composable
fun CardBadge(
    text: String,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    textColor: Color? = null
) {
    val bg = if (isPrimary) FigmaPrimaryPill else FigmaSurfaceHigh
    val contentColor = textColor ?: if (isPrimary) FigmaTextDark else FigmaTextPrimary

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
        )
    }
}

@Composable
fun PaymentCardItem(
    card: PaymentCard,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = FigmaSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card Top: Brand/last4 + Expiry & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = card.displayTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaTextPrimary
                        )
                    )
                    Text(
                        text = "Expires ${card.expiryDate}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                }

                if (card.isDefault) {
                    CardBadge(text = "Default", isPrimary = false, textColor = FigmaTextPrimary)
                } else if (card.isBackup) {
                    CardBadge(text = "Backup", isPrimary = false, textColor = FigmaTextMuted)
                }
            }

            // Card Bottom: Subtitle & Action button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (card.isDefault) "Your default card" else "Additional saved card",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = FigmaTextMuted
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(FigmaSurfaceHigh)
                        .clickable(onClick = onActionClick)
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (card.isDefault) "Manage" else "Edit",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaTextPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentSecurityNote(
    title: String = "Saved card details",
    description: String = "Only masked card details are shown here. Choose Manage or Edit to update a card.",
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = FigmaSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FigmaTextPrimary
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = FigmaTextMuted
                )
            )
        }
    }
}

@Composable
fun PaymentInputField(
    value: String,
    onValueChange: (String) -> Unit,
    labelRight: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        color = FigmaSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty() && placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = FigmaTextMuted
                        )
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    readOnly = readOnly,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = FigmaTextPrimary
                    ),
                    cursorBrush = SolidColor(FigmaTextPrimary),
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = labelRight,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = FigmaTextMuted
                )
            )
        }
    }
}

@Composable
fun PrimaryPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    height: Int = 44
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = FigmaPrimaryPill,
            contentColor = FigmaTextDark,
            disabledContainerColor = FigmaSurfaceHigh,
            disabledContentColor = FigmaTextMuted
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = FigmaTextDark
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun SecondaryPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Int = 44
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = FigmaSurface,
            contentColor = FigmaTextMuted
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
