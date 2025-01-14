package com.slateblua.chesschronos.feature.chronos

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.slateblua.chesschronos.data.Type

class ChronosScreen(private val type: Type) : Screen {
    @Composable
    override fun Content() {
        val chronosScreenModel = getScreenModel<ChronosScreenModel>()
        chronosScreenModel.setChronos(type.durationInMinutes * 60L)
        ChronosScreenContent(chronosScreenModel = chronosScreenModel)
    }
}

@Composable
fun ChronosScreenContent(chronosScreenModel: ChronosScreenModel) {

    val playerOneState by chronosScreenModel.playerOneState.collectAsState()
    val playerTwoState by chronosScreenModel.playerTwoState.collectAsState()

    ChronosScreen(
        playerOneState = playerOneState,
        onPlayerOneMove = { chronosScreenModel.onPlayerOneMoves() },
        onPause = { chronosScreenModel.pauseBoth() },
        playerTwoState = playerTwoState,
        onPlayerTwoMove = { chronosScreenModel.onPlayerTwoMoves() },
    )
}

@Composable
fun ChronosScreen(
    playerOneState: Long,
    onPlayerOneMove: () -> Unit,
    playerTwoState: Long,
    onPlayerTwoMove: () -> Unit,
    onPause: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChronosPart(
            modifier = Modifier
                .weight(1f)
                .rotate(180f),
            onMove = onPlayerOneMove,
            chronosValue = playerOneState
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedIconButton(onClick = onPause) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = "Pause the game")
            }
        }
        ChronosPart(
            modifier = Modifier.weight(1f),
            onMove = onPlayerTwoMove,
            chronosValue = playerTwoState
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ChronosPart(modifier: Modifier = Modifier, onMove: () -> Unit, chronosValue: Long) {
    Card(modifier = modifier
        .fillMaxSize()
        .clickable { onMove() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = String.format("%02d:%02d", chronosValue / 60, chronosValue % 60), fontSize = 90.sp)
        }
    }
}
