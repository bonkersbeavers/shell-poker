package core.gameflow

import core.gameflow.handstate.HandState

data class Positions(val button: Int, val smallBlind: Int, val bigBlind: Int)

private fun previousPosition(from: Int, tableSeatsNumber: Int): Int {
    return from
}

fun shiftPositions(builder: HandState.ImmutableBuilder, setting: RoomSettings): HandState.ImmutableBuilder {
    builder.players!!
    builder.positions!!

    assert(builder.players.size >= 2)

    return builder
}
