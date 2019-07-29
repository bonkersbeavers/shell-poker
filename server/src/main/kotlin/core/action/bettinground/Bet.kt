package core.action.bettinground

import core.gameflow.HandState

class Bet(val size: Int) : BettingAction(ActionType.BET) {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState
                .activePlayer!!
                .withBet(size)
                .copy(lastAction = ActionType.BET)

        return handState
                .updateActivePlayer(updatedPlayer)
                .copy(lastAggressor = updatedPlayer,
                        minRaise = (size - handState.totalBet) * 2,
                        lastLegalBet = size)
    }

    override fun innerIsLegal(handState: HandState): Boolean {
        val activePlayer = handState.activePlayer!!

        return when {
            (size >= handState.minRaise)
                    and (size <= activePlayer.stack)
                    and (handState.lastLegalBet == 0) -> true
            else -> false
        }
    }
}
