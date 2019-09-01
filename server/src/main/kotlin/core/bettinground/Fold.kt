package core.bettinground

import core.gameflow.HandState
import core.gameflow.updateActivePlayer

class Fold : BettingAction(ActionType.FOLD) {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.copy(lastAction = ActionType.FOLD)
        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerValidate(handState: HandState): ActionValidation = ValidAction()
}
