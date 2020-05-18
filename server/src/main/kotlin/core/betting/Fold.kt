package core.betting

import core.hand.player.betting.ActionType
import core.handflow.HandState
import core.handflow.updateActivePlayer

object Fold : BettingAction() {

    override val type: ActionType = ActionType.FOLD

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.copy(lastAction = ActionType.FOLD)
        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerValidate(handState: HandState): ActionValidation = ValidAction
}
