package core.handflow.helpers

import core.handflow.betting.BettingActionType
import core.handflow.dealer.BettingRound
import core.handflow.hand.ApplicableHandAction
import core.handflow.hand.HandAction
import core.handflow.hand.HandStage
import core.handflow.hand.HandState
import core.handflow.player.decisive
import core.handflow.player.inGame

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

        val stage = if (handState.bettingRound == BettingRound.RIVER || newPlayers.inGame().count() < 2) {
            HandStage.RESULTS_STAGE
        } else if (newPlayers.decisive().count() < 2) {
            HandStage.ALLIN_DUEL_STAGE
        } else {
            HandStage.INTERACTIVE_STAGE
        }

        return handState.copy(
                players = newPlayers,
                seatToPotContribution = totalContributionMap.toMap(),
                lastLegalBet = 0,
                extraBet = 0,
                minRaise = handState.blinds.bigBlind,
                handStage = stage
        )
    }
}
