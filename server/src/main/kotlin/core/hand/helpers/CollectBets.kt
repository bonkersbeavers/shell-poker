package core.hand.helpers

import core.hand.betting.BettingActionType
import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.HandState

object CollectBets : HandAction(), ApplicableHandAction {
    override fun apply(handState: HandState): HandState {

        val totalContributionMap = handState.seatToPotContribution.toMutableMap()
        for (player in handState.players) {
            totalContributionMap[player.seat] = totalContributionMap.getOrDefault(player.seat, 0) + player.currentBet
        }

        val newPlayers = handState.players.map {
            it.copy(
                    currentActionType = if (it.currentActionType == BettingActionType.FOLD) BettingActionType.FOLD else null,
                    currentBet = 0
            )
        }

        return handState.copy(
                players = newPlayers,
                seatToPotContribution = totalContributionMap.toMap(),
                lastLegalBet = 0,
                extraBet = 0,
                minRaise = handState.blinds.bigBlind
        )
    }
}
