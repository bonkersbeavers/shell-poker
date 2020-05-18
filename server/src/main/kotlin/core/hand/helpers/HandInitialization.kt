package core.hand.helpers

import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.HandState

object HandInitialization: HandAction(), ApplicableHandAction {
    override fun apply(handState: HandState): HandState {
        return handState.copy(
                lastLegalBet = handState.blinds.bigBlind,
                extraBet = 0,
                minRaise = handState.blinds.bigBlind * 2
        )
    }
}
