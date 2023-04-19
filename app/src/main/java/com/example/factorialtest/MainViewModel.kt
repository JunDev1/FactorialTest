package com.example.factorialtest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("My coroutine scope"))

    fun calculate(value: String?) {
        _state.value = Progress
        if (value.isNullOrBlank()) {
            _state.value = Error
            return
        }
        coroutineScope.launch {
            val number = value.toLong()
            val result = withContext(Dispatchers.Default) {
                factorial(number)
            }
            _state.value = Factorial(result)
            Log.d("MainViewModel", coroutineScope.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}

private suspend fun factorial(number: Long): String {
    var result = BigInteger.ONE
    for (i in 1..number) {
        result = result.multiply(BigInteger.valueOf(i))
    }
    return result.toString()
}