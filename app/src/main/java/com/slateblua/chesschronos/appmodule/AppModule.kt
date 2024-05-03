package com.slateblua.chesschronos.appmodule

import com.slateblua.chesschronos.feature.chronos.ChronosScreenModel
import com.slateblua.chesschronos.feature.newgame.NewGameScreenModel
import org.koin.dsl.module

val appModule = module {
    factory { NewGameScreenModel() }
    factory { ChronosScreenModel() }
}