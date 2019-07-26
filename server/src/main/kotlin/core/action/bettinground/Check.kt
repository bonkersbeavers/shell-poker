package core.action.bettinground

import core.gameflow.BettingRound
import core.gameflow.HandState

class Check : BettingAction() {

    override fun innerApply(handState: HandState): HandState {
        return handState
    }

    override fun innerIsLegal(handState: HandState): Boolean {
        return when {
            (handState.lastAggressor == null) -> true

            /* Pre flop scenario in which BB should have an option to check */
            (handState.activePlayer == handState.bigBlindPlayer) and
                    (handState.totalBet == handState.blinds.bigBlind) and
                    (handState.bettingRound == BettingRound.PRE_FLOP) -> true

            else -> false
        }
    }
}
