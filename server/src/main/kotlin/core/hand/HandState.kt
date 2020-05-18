package core.hand

import core.hand.betting.BettingActionType
import core.cards.Card
import core.hand.dealer.BettingRound
import core.hand.blinds.Blinds
import core.hand.positions.Positions
import core.hand.pot.Pot
import core.hand.pot.resolvePots

data class HandState(
        val players: List<Player>,
        val positions: Positions,
        val blinds: Blinds,
        val communityCards: List<Card> = emptyList(),
        val seatToPotContribution: Map<Int, Int> = emptyMap(),
        val lastLegalBet: Int = 0,
        val extraBet: Int = 0,
        val minRaise: Int = 0
) {
    val bettingRound: BettingRound? = run {
        val holeCardsDealt = players.all { it.cards != null }
        when (communityCards.count()) {
            0 -> if (holeCardsDealt) BettingRound.PRE_FLOP else null
            3 -> BettingRound.FLOP
            4 -> BettingRound.TURN
            5 -> BettingRound.RIVER
            else -> throw HandFlowException("invalid cards status")
        }
    }

    val totalBet = lastLegalBet + extraBet

    val lastAggressor: Player? = players.find { it.currentBet == totalBet &&
            (it.currentActionType == BettingActionType.BET || it.currentActionType == BettingActionType.RAISE) }

    val activePlayer: Player? = run {
        if (players.decisive().count() < 2) {
            null
        } else {
            val startingSeat = when {
                lastAggressor != null -> lastAggressor.seat + 1
                bettingRound == BettingRound.PRE_FLOP -> positions.bigBlind + 1
                else -> positions.button + 1
            }
            val orderedPlayers = players.orderedBySeats(startingSeat)
            orderedPlayers.find { it.isDecisive && (it.currentBet < totalBet || it.actedInCurrentBettingRound.not()) }
        }
    }

    val pots: List<Pot> = resolvePots(players, seatToPotContribution)

    val handIsOver: Boolean = (bettingRound == BettingRound.RIVER && activePlayer == null) or
            (players.inGame().count() < 2)

    val playersInteractionIsOver: Boolean = handIsOver or (players.decisive().count() < 2)
}
