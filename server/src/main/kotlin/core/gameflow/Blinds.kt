package core.gameflow

import core.gameflow.handstate.HandState

data class Blinds(val smallBlind: Int, val bigBlind: Int, val ante: Int = 0) {
    fun postBlinds(handState: HandState): HandState {
        return handState
    }
}
