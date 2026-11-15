package com.example.payment_app.ui.screens.receipt

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.payment_app.data.model.Receipt
import com.example.payment_app.data.model.ReceiptExpense
import com.example.payment_app.data.model.ReceiptFileType
import com.example.payment_app.data.repository.FakeReceiptRepository
import com.example.payment_app.ui.components.SectionHeader
import com.example.payment_app.ui.components.formatMoney
import com.example.payment_app.ui.theme.Payment_AppTheme
import com.example.payment_app.ui.viewmodel.ReceiptUiState
import com.example.payment_app.ui.viewmodel.ReceiptViewModel
import kotlinx.coroutines.launch

private val RECEIPT_MIME_TYPES = arrayOf("application/pdf", "image/*")

@Composable
fun ReceiptScreen(
    expenseId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReceiptViewModel = viewModel(key = "receipt-$expenseId") { ReceiptViewModel(expenseId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val pickFile = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            keepReadAccess(context, uri)
            viewModel.onReceiptPicked(
                fileUri = uri.toString(),
                fileName = displayNameOf(context, uri),
                mimeType = context.contentResolver.getType(uri),
            )
        }
    }

    fun showMessage(text: String) {
        scope.launch { snackbarHostState.showSnackbar(text) }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        ReceiptContent(
            uiState = uiState,
            onBack = onBack,
            onAttach = { pickFile.launch(RECEIPT_MIME_TYPES) },
            onView = {
                val fileUri = uiState.receipt?.fileUri
                if (fileUri == null) {
                    showMessage("This is sample data, there is no file to open yet.")
                } else if (!openFile(context, fileUri)) {
                    showMessage("No app on this phone can open this file.")
                }
            },
            onReplace = { pickFile.launch(RECEIPT_MIME_TYPES) },
            onRemove = viewModel::onRemoveClick,
            onRemoveConfirmed = viewModel::onRemoveConfirmed,
            onRemoveDismissed = viewModel::onRemoveDismissed,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun ReceiptContent(
    uiState: ReceiptUiState,
    onBack: () -> Unit,
    onAttach: () -> Unit,
    onView: () -> Unit,
    onReplace: () -> Unit,
    onRemove: () -> Unit,
    onRemoveConfirmed: () -> Unit,
    onRemoveDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(text = "Receipt", style = MaterialTheme.typography.titleLarge)
        }

        val expense = uiState.expense
        if (expense == null) {
            Text(
                text = "This expense could not be found.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            return@Column
        }

        ExpenseHeader(expense)

        val receipt = uiState.receipt
        if (receipt != null) ReceiptCard(receipt) else NoReceiptCard()

        SectionHeader(title = "Attachment")
        if (receipt == null) {
            Button(onClick = onAttach, modifier = Modifier.fillMaxWidth()) { Text("Attach receipt") }
            Text(
                text = "Optional receipt attachment for ${expense.groupName}.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onView, modifier = Modifier.weight(1f)) { Text("View") }
                OutlinedButton(onClick = onReplace, modifier = Modifier.weight(1f)) { Text("Replace") }
            }
            OutlinedButton(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) { Text("Remove receipt") }
        }
    }

    if (uiState.showRemoveConfirmation) {
        AlertDialog(
            onDismissRequest = onRemoveDismissed,
            title = { Text("Remove receipt?") },
            text = { Text("The receipt will be removed from this expense. The expense itself is kept.") },
            confirmButton = { TextButton(onClick = onRemoveConfirmed) { Text("Remove") } },
            dismissButton = { TextButton(onClick = onRemoveDismissed) { Text("Cancel") } },
        )
    }
}

@Composable
private fun ExpenseHeader(expense: ReceiptExpense) {
    Column {
        Text(
            text = "${expense.groupName} · ${expense.title}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = formatMoney(expense.amountCents),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Paid by ${expense.paidByName} · split ${expense.splitCount} ways",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ReceiptCard(receipt: Receipt) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = receipt.fileName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Badge(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Text(
                        text = if (receipt.fileType == ReceiptFileType.PDF) "PDF" else "IMAGE",
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                }
            }

            if (receipt.merchant != null || receipt.dateLabel != null) {
                Row {
                    LabelValue("Merchant", receipt.merchant ?: "-", Modifier.weight(1f))
                    LabelValue("Date", receipt.dateLabel ?: "-", horizontalAlignment = Alignment.End)
                }
            }

            if (receipt.lineItems.isNotEmpty()) {
                HorizontalDivider()
                receipt.lineItems.forEach { item ->
                    AmountRow(item.label, formatMoney(item.amountCents))
                }
                HorizontalDivider()
                AmountRow("Total", formatMoney(receipt.lineItemsTotalCents), bold = true)
            } else if (receipt.fileUri != null) {
                Text(
                    text = "Attached file. Tap View to open it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun NoReceiptCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "No receipt attached", style = MaterialTheme.typography.titleSmall)
            Text(
                text = "You can attach a photo or a PDF of the receipt.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun LabelValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun AmountRow(label: String, amount: String, bold: Boolean = false) {
    val weight = if (bold) FontWeight.Bold else FontWeight.Normal
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = weight, modifier = Modifier.weight(1f))
        Text(text = amount, fontWeight = weight)
    }
}

private fun displayNameOf(context: Context, uri: Uri): String =
    context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
        ?: uri.lastPathSegment
        ?: "Receipt"

private fun keepReadAccess(context: Context, uri: Uri) {
    try {
        context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    } catch (_: SecurityException) {
    }
}

private fun openFile(context: Context, fileUri: String): Boolean {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(fileUri.toUri(), context.contentResolver.getType(fileUri.toUri()))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    return try {
        context.startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}

private val previewExpense = FakeReceiptRepository().expense("a1")

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun ReceiptContentPreview() {
    Payment_AppTheme {
        Surface {
            ReceiptContent(
                uiState = ReceiptUiState(expense = previewExpense, receipt = FakeReceiptRepository().receipt("a1")),
                onBack = {}, onAttach = {}, onView = {}, onReplace = {}, onRemove = {},
                onRemoveConfirmed = {}, onRemoveDismissed = {},
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun ReceiptContentEmptyPreview() {
    Payment_AppTheme {
        Surface {
            ReceiptContent(
                uiState = ReceiptUiState(expense = previewExpense, receipt = null),
                onBack = {}, onAttach = {}, onView = {}, onReplace = {}, onRemove = {},
                onRemoveConfirmed = {}, onRemoveDismissed = {},
            )
        }
    }
}
