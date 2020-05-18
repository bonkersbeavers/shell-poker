package core.hand

import core.cards.Card
import core.hand.betting.BettingActionType
import core.pokerhands.PokerHand
import core.pokerhands.pickBestHand

data class Player(
        val seat: Int,
        val stack: Int,
        val cards: Pair<Card, Card>? = null,
        val currentActionType: BettingActionType? = null,
        val currentBet: Int = 0
        ) {

    val isInGame: Boolean = currentActionType != BettingActionType.FOLD
    val isAllIn: Boolean = (stack == 0) and isInGame
    val isDecisive: Boolean = isInGame and !isAllIn
    val actedInCurrentBettingRound: Boolean = (currentActionType != null) && (currentActionType != BettingActionType.POST)

    val maxBet: Int = currentBet + stack

    fun withBet(betSize: Int): Player {
        val difference = betSize - currentBet
        return this.copy(
                stack = stack - difference,
                currentBet = betSize
        )
    }

    /* Creates the best possible hand out of player's hole cards and community cards. */
    fun makeHand(communityCards: Collection<Card>): PokerHand {
        cards!!
        val allCards = communityCards.toSet() + cards.toList().toSet()
        return allCards.pickBestHand()
    }
}
