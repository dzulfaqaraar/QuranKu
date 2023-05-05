package com.dzulfaqar.quranku.core.coroutine

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider @Inject constructor() {
    open val main: CoroutineContext by lazy { Dispatchers.Main }
    open val io: CoroutineContext by lazy { Dispatchers.IO }
    open val default: CoroutineContext by lazy { Dispatchers.Default }
    open val unconfined: CoroutineContext by lazy { Dispatchers.Unconfined }
}

class TestCoroutineContextProvider : CoroutineContextProvider() {
    override val main: CoroutineContext
        get() = Dispatchers.Unconfined

    override val io: CoroutineContext
        get() = Dispatchers.Unconfined

    override val default: CoroutineContext
        get() = Dispatchers.Unconfined

    override val unconfined: CoroutineContext
        get() = Dispatchers.Unconfined
}
