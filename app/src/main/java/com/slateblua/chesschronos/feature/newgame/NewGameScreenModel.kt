package com.slateblua.chesschronos.feature.newgame

import cafe.adriel.voyager.core.model.ScreenModel
import com.slateblua.chesschronos.data.Type
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameScreenModel : ScreenModel {
    private val _gameType = MutableStateFlow(Type.BULLET)
    val gameType = _gameType

    fun toggleType(type: Type) {
        _gameType.value = type;
    }
}