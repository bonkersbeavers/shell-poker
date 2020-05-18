package core.hand.helpers

import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.HandState
import core.hand.PlayerState

object HandCleanup: HandAction(), ApplicableHandAction {
    override fun apply(handState: HandState): HandState {
        val newPlayers = handState.playersStates.map {
            PlayerState(seat = it.seat, stack = it.stack, cards = null, currentActionType = null, currentBet = 0)
        }

        return handState.copy(
                playersStates = newPlayers,
                blinds = handState.blinds,
                positions = handState.positions,
                communityCards = emptyList(),
                lastLegalBet = 0,
                extraBet = 0,
                minRaise = 0
        )
    }
}
