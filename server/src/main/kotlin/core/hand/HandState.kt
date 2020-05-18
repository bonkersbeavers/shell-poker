package core.hand

import core.hand.player.betting.ActionType
import core.cards.Card
import core.flowUtils.BettingRound
import core.flowUtils.Blinds
import core.flowUtils.Positions
import core.hand.player.decisive
import core.hand.player.inGame
import core.hand.player.orderedBySeats
import core.hand.pot.Pot
import core.handflow.HandFlowException

data class HandState(
        val playersStates: List<PlayerState>,
        val positions: Positions,
        val blinds: Blinds,
        val communityCards: List<Card> = emptyList(),
        val pots: List<Pot> = emptyList(),

        val lastLegalBet: Int = 0,
        val extraBet: Int = 0,
        val minRaise: Int = 0
) {
    val bettingRound: BettingRound? = run {
        val holeCardsDealt = playersStates.all { it.cards != null }
        when (communityCards.count()) {
            0 -> if (holeCardsDealt) BettingRound.PRE_FLOP else null
            3 -> BettingRound.FLOP
            4 -> BettingRound.TURN
            5 -> BettingRound.RIVER
            else -> throw HandFlowException("invalid cards status")
        }
    }

    val totalBet = lastLegalBet + extraBet

    val lastAggressor: PlayerState? = playersStates.find { it.currentBet == totalBet &&
            (it.currentActionType == ActionType.BET || it.currentActionType == ActionType.RAISE) }

    val activePlayer: PlayerState? = run {
        if (playersStates.decisive().count() < 2) {
            null
        } else {
            val startingSeat = when {
                lastAggressor != null -> lastAggressor.seat + 1
                bettingRound == BettingRound.PRE_FLOP -> positions.bigBlind + 1
                else -> positions.button + 1
            }
            val orderedPlayers = playersStates.orderedBySeats(startingSeat)
            orderedPlayers.find { it.isDecisive && (it.currentBet < totalBet || it.actedInCurrentBettingRound.not()) }
        }
    }

    val handIsOver: Boolean = (bettingRound == BettingRound.RIVER && activePlayer == null) or
            (playersStates.inGame().count() < 2)

    val playersInteractionIsOver: Boolean = handIsOver or (playersStates.decisive().count() < 2)
}
