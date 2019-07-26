package core.gameflow

import core.Card
import core.action.bettinground.*

/**
 * handStartingStack - player's stack size at the beginning of currently played hand,
 *      it remains unchanged throughout the hand
 *
 * chipsInPot - the amount of player's chips that already went into the pot in previous betting rounds,
 *      note that it doesn't include the chips in player's current bet
 */
data class Player(
    val position: Int,
    private val handStartingStack: Int,
    val holeCards: List<Card> = emptyList(),
    val chipsInPot: Int = 0,
    val lastAction: BettingAction? = null
) {

    /* the amount od chips available for the player at the beginning of current betting round */
    private val roundStartingStack: Int = handStartingStack - chipsInPot

    val currentBet: Int = when (lastAction) {
        is Post -> lastAction.size
        is Call -> lastAction.size
        is Bet -> lastAction.size
        is Raise -> lastAction.size
        is AllIn -> roundStartingStack
        else -> 0   // no action / check / fold
    }

    /* the amount of chips that haven't been neither put into the pot nor the current bet */
    val stack: Int = roundStartingStack - currentBet

    fun withCards(newCards: List<Card>): Player = this.copy(holeCards = newCards)

    val isInGame: Boolean = lastAction !is Fold
    val isAllIn: Boolean = lastAction is AllIn
    val isDecisive: Boolean = isInGame and !isAllIn
}
