package core.action.bettinground

import core.gameflow.HandState

class Fold : BettingAction() {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.afterFold()
        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerIsLegal(handState: HandState): Boolean = true
}
