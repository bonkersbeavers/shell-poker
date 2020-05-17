package core.hand.utils

import core.betting.ActionType
import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.HandState

object BettingRoundCleanup : HandAction(), ApplicableHandAction {
    override fun apply(handState: HandState): HandState {

        // TODO("not implemented")
        val newPlayers = handState.playersStates.map {
            it.copy(
                    currentActionType = if (it.currentActionType == ActionType.FOLD) ActionType.FOLD else null,
                    currentBet = 0
            )
        }

        return handState.copy(
                playersStates = newPlayers,
                lastLegalBet = 0,
                extraBet = 0,
                minRaise = handState.blinds.bigBlind
        )
    }
}
