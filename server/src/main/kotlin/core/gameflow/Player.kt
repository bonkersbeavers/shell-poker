package core.gameflow

import core.Card
import core.action.bettinground.*
import core.bettinground.ActionType

/**
 * chipsInPot - the amount of player's chips that already went into the pot in previous rbetting rounds,
 *      not including the chips in player's current bet
 */
data class Player(
    val position: Int,
    val stack: Int,
    val holeCards: List<Card> = emptyList(),
    val bet: Int = 0,
    val chipsInPot: Int = 0,
    val lastAction: ActionType? = null
) {
    init {
        assert(stack >= 0)
        assert(bet >= 0)
        assert(chipsInPot >= 0)
    }

    val isInGame: Boolean = lastAction != ActionType.FOLD
    val isAllIn: Boolean = (stack == 0) and isInGame
    val isDecisive: Boolean = isInGame and !isAllIn

    val maxBet: Int = bet + stack

    fun withBet(betSize: Int): Player {
        val difference = betSize - bet
        return this.copy(
                stack = stack - difference,
                bet = betSize
        )
    }

    fun withCards(newCards: List<Card>): Player = this.copy(holeCards = newCards)
}
