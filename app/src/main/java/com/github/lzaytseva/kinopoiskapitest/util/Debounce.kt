package com.github.lzaytseva.kinopoiskapitest.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Функция debounce используется для ограничения скорости вызова функции, то есть функция action будет вызвана не чаще, чем указано в параметре delayMillis.
 *
 * ### Параметры функции debounce:
 *
 * - delayMillis: Long - время задержки в миллисекундах между вызовами функции action.
 * - coroutineScope: CoroutineScope - область видимости корутины, в которой будет выполняться задержка и вызов функции action.
 * - useLastParam: Boolean - если true, то после задержки будет использоваться последний переданный аргумент, если false, то будет использоваться аргумент, который был передан при первом вызове.
 * - actionWithDelay: Boolean (по умолчанию true) - если true, функция action будет вызвана после задержки, если false, функция action будет вызвана сразу без задержки.
 * - action: (T) -> Unit - функция, вызов которой нужно задебаунсить. T - тип входного параметра функции.
 *
 */
fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean = false,
    actionWithDelay: Boolean = true,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            if (!actionWithDelay) action(param)
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                if (actionWithDelay) action(param)
            }
        }
    }
}
