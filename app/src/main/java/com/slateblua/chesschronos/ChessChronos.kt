package com.slateblua.chesschronos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.slateblua.chesschronos.feature.newgame.NewGameScreen

class ChessChronos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigator(screen = NewGameScreen()) { nav ->
                SlideTransition(navigator = nav)
            }
        }
    }
}

