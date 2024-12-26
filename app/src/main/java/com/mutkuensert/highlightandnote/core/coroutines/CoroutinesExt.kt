package com.mutkuensert.highlightandnote.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withIo(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO, block)
}