package core.gameflow

import core.bettinground.Post
import core.gameflow.handstate.HandState

fun postBlinds(handState: HandState): HandState {
    val smallBlindValue = handState.blinds.smallBlind
    val bigBlindValue = handState.blinds.bigBlind

    val afterSmallBlindState = Post(smallBlindValue).apply(handState)
    val afterBigBlindState = Post(bigBlindValue).apply(afterSmallBlindState)

    return afterBigBlindState
}

data class Blinds(val smallBlind: Int, val bigBlind: Int, val ante: Int = 0)
