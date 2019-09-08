package core.gameflow

import core.gameflow.handstate.HandState

fun postBlinds(handState: HandState): HandState = handState

data class Blinds(val smallBlind: Int, val bigBlind: Int, val ante: Int = 0)
