package com.slateblua.chesschronos.feature.chronos

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.launch


internal sealed class ChronosEffect {
    data object ChronosEnded : ChronosEffect()
}

internal class ChronosScreenModel : ScreenModel {
    companion object {
        private const val DEFAULT_TIMER = 10L
    }

    private val _sideEffect = Channel<ChronosEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _playerOneState = MutableStateFlow(DEFAULT_TIMER)
    val playerOneState = _playerOneState.asStateFlow()

    private val _playerTwoState = MutableStateFlow(DEFAULT_TIMER)
    val playerTwoState = _playerTwoState.asStateFlow()

    private var playerOneJob: Job? = null
    private var playerTwoJob: Job? = null

    private val jobMutex = Mutex()

    fun setChronos(time: Long) {
        screenModelScope.launch {
            jobMutex.withLock {
                pauseBoth() // Ensure no active jobs during state reset
                _playerOneState.value = time
                _playerTwoState.value = time
            }
        }
    }

    fun onPlayerOneMoves() {
        screenModelScope.launch {
            jobMutex.withLock {
                if (playerTwoJob != null) return@launch // Prevent double starting
                pausePlayerOne() // Ensure Player One's timer is paused
                playerTwoJob = screenModelScope.launch {
                    startCountdown(_playerTwoState) {
                        playerTwoJob = null // Clear job reference on completion
                    }
                }
            }
        }
    }

    fun onPlayerTwoMoves() {
        screenModelScope.launch {
            jobMutex.withLock {
                if (playerOneJob != null) return@launch // Prevent double starting
                pausePlayerTwo() // Ensure Player Two's timer is paused
                playerOneJob = screenModelScope.launch {
                    startCountdown(_playerOneState) {
                        playerOneJob = null // Clear job reference on completion
                    }
                }
            }
        }
    }

    private suspend fun startCountdown(
        state: MutableStateFlow<Long>,
        onComplete: () -> Unit
    ) {
        try {
            while (state.value > 0) {
                delay(1000)
                state.value -= 1

                if (state.value.toInt() == 0) {
                    sendEndedEffect()
                }
            }
        } finally {
            onComplete()
        }
    }

    private suspend fun sendEndedEffect() {
        _sideEffect.send(ChronosEffect.ChronosEnded)
    }

    fun pauseBoth() {
        screenModelScope.launch {
            jobMutex.withLock {
                pausePlayerOne()
                pausePlayerTwo()
            }
        }
    }

    private fun pausePlayerOne() {
        playerOneJob?.cancel()
        playerOneJob = null
    }

    private fun pausePlayerTwo() {
        playerTwoJob?.cancel()
        playerTwoJob = null
    }
}