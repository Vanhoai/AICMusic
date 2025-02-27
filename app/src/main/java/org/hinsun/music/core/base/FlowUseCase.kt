package org.hinsun.core.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
abstract class FlowUseCase<T> {
    private val _trigger = MutableStateFlow(true)
    
    val resultFlow: Flow<T> = _trigger.flatMapLatest {
        performAction()
    }

    suspend fun launch() {
        _trigger.emit(!(_trigger.value))
    }

    protected abstract fun performAction(): Flow<T>
}