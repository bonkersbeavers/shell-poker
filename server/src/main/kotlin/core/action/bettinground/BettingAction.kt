package core.action.bettinground

import core.gameflow.HandState

abstract class BettingAction {

    abstract fun apply(handState: HandState): HandState
    abstract fun innerIsLegal(handState: HandState): Boolean

    fun isLegal(handState: HandState): Boolean = (handState.activePlayer != null) and innerIsLegal(handState)
}
