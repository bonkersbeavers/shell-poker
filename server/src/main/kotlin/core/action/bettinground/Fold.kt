package core.action.bettinground

import core.gameflow.HandState

class Fold : BettingAction(ActionType.FOLD) {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.copy(lastAction = ActionType.FOLD)
        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerIsLegal(handState: HandState): Boolean = true
}
