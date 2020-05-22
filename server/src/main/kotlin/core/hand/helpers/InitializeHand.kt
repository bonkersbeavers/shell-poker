package core.hand.helpers

import core.hand.*

object InitializeHand: HandAction(), ApplicableHandAction {
    override fun apply(handState: HandState): HandState {
        val clearPlayers = handState.players.map {
            Player(seat = it.seat, stack = it.stack, cards = null, currentActionType = null, currentBet = 0)
        }

        return handState.copy(
                players = clearPlayers,
                blinds = handState.blinds,
                positions = handState.positions,
                communityCards = emptyList(),
                seatToPotContribution = emptyMap<Int, Int>().withDefault { 0 },
                lastLegalBet = handState.blinds.bigBlind,
                extraBet = 0,
                minRaise = handState.blinds.bigBlind * 2,
                handStage = HandStage.INTERACTIVE_STAGE
        )
    }
}
