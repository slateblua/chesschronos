package com.slateblua.chesschronos

import android.app.Application
import com.slateblua.chesschronos.appmodule.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ChessChronosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ChessChronosApp)
            modules(appModule)
        }
    }
}