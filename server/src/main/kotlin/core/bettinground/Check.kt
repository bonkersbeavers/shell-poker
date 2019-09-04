package core.bettinground

import core.gameflow.handstate.HandState
import core.gameflow.handstate.updateActivePlayer

class Check : BettingAction(ActionType.CHECK) {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.copy(lastAction = ActionType.CHECK)
        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerValidate(handState: HandState): ActionValidation {
        val activePlayer = handState.activePlayer!!

        return when {
            (handState.totalBet == 0) -> ValidAction()

            /* Pre flop scenario in which BB / straddler should have an option to check */
            ((activePlayer.bet == handState.totalBet) and (activePlayer.lastAction == ActionType.POST)) -> ValidAction()

            else -> InvalidAction("Cannot check when there is a bet to call")
        }
    }
}
