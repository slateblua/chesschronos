package com.slateblua.chesschronos.feature.chronos

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChronosScreenModel : ScreenModel {

    private val _playerOneState = MutableStateFlow(10L)
    val playerOneState = _playerOneState.asStateFlow()

    private val _playerTwoState = MutableStateFlow(10L)
    val playerTwoState = _playerTwoState.asStateFlow()

    private var playerOneJob: Job? = null
    private var playerTwoJob: Job? = null

    fun setChronos(l: Long) {
        _playerOneState.value = l;
        _playerTwoState.value = l;
    }

    fun onPlayerOneMoves() {
        pausePlayerOne()
        playerTwoJob = screenModelScope.launch {
            while (_playerTwoState.value > 0) {
                delay(1000)
                _playerTwoState.value--
            }
        }
    }

    fun onPlayerTwoMoves() {
        pausePlayerTwo()
        playerOneJob = screenModelScope.launch {
            while (_playerOneState.value > 0) {
                delay(1000)
                _playerOneState.value--
            }
        }
    }

    fun pauseBoth() {
        playerOneJob?.cancel()
        playerTwoJob?.cancel()
    }

    private fun pausePlayerOne() {
        playerOneJob?.cancel()
    }

    private fun pausePlayerTwo() {
        playerTwoJob?.cancel()
    }
}