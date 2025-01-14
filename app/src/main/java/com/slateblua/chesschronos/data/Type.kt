package com.slateblua.chesschronos.data

enum class Type(
    val durationInMinutes: Int,
) {
    BULLET(1),
    BLITZ(3),
    STANDARD(10),
}