package com.slateblua.chesschronos.feature.newgame

import android.graphics.drawable.ShapeDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.slateblua.chesschronos.data.Type
import com.slateblua.chesschronos.feature.chronos.ChronosScreen

class NewGameScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<NewGameScreenModel>()
        NewGameScreenContent(screenModel = screenModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGameScreenContent(
    screenModel: NewGameScreenModel
) {
    val gameType = screenModel.gameType.collectAsState().value
    val nav = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Chess Chronos") }) }
    ) { paddValues ->
        Column(
            modifier = Modifier
                .padding(paddValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                Type.entries.toList().forEach { type ->
                    OutlinedButton(
                        onClick = { screenModel.toggleType(type) },
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (gameType == type) Color(
                                0xFFFFD8B7
                            ) else Color.Transparent)
                    ) {
                        Text(text = type.name)
                    }
                }
            }
            OutlinedButton(onClick = { nav.push(
                ChronosScreen(gameType)
            ) }) {
                Text(text = "START")
            }
        }
    }
}
