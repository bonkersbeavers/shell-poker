package core.bettinground

import core.gameflow.handstate.HandState
import core.gameflow.handstate.updateActivePlayer

object Fold : BettingAction() {

    override val type: ActionType = ActionType.FOLD

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.copy(lastAction = ActionType.FOLD)
        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerValidate(handState: HandState): ActionValidation = ValidAction
}
