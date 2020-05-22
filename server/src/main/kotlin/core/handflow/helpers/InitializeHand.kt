package core.handflow.helpers

import core.handflow.hand.ApplicableHandAction
import core.handflow.hand.HandAction
import core.handflow.hand.HandStage
import core.handflow.hand.HandState
import core.handflow.player.Player

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
