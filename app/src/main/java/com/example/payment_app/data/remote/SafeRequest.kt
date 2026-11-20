package com.example.payment_app.data.remote

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

const val DEFAULT_REQUEST_TIMEOUT_MS = 10_000L

suspend fun <T> safeRequest(
    dispatcher: CoroutineDispatcher,
    timeoutMs: Long = DEFAULT_REQUEST_TIMEOUT_MS,
    block: suspend () -> T,
): Result<T> = withContext(dispatcher) {
    try {
        Result.success(withTimeout(timeoutMs) { block() })
    } catch (e: TimeoutCancellationException) {
        Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
