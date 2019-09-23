package core.gameflow.dealer

import core.gameflow.handstate.HandState

interface IDealer {
    fun deal(handState: HandState): HandState
    fun shuffle(seed: Int? = null)
}