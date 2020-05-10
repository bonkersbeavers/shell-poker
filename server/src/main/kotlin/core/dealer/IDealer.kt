package core.dealer

import core.handflow.HandState

interface IDealer {
    fun deal(handState: HandState): HandState
    fun shuffle(seed: Int? = null)
}
