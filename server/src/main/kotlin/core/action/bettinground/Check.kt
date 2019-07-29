package core.action.bettinground

import core.gameflow.HandState

class Check : BettingAction(ActionType.CHECK) {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.copy(lastAction = ActionType.CHECK)
        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerIsLegal(handState: HandState): Boolean {
        val activePlayer = handState.activePlayer!!

        return when {
            (handState.totalBet == 0) -> true

            /* Pre flop scenario in which BB / straddler should have an option to check */
            ((activePlayer.bet == handState.totalBet) and (activePlayer.lastAction == ActionType.POST)) -> true

            else -> false
        }
    }
}
