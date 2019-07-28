package core.action.bettinground

import core.gameflow.HandState
import kotlin.math.min

class Post(val size: Int) : BettingAction(ActionType.POST) {

    override fun innerApply(handState: HandState): HandState {
        val activePlayer = handState.activePlayer!!
        val bet = min(size, activePlayer.maxBet)
        val playerUpdate = activePlayer
                .withBet(bet)
                .copy(lastAction = ActionType.POST)

        val newState = handState.updateActivePlayer(playerUpdate)

        return when {

            /* When posted bet is lower than current bet in the hand. */
            bet <= newState.totalBet -> newState

            /* When posted bet has an effect of raising current bet in the hand. */
            bet >= newState.minRaise -> {
                val difference = bet - newState.totalBet
                val newMinRaise = bet + difference
                newState.copy(lastLegalBet = bet, extraBet = 0, minRaise = newMinRaise)
            }

            /* When posted bet is higher than current bet in the hand but not high enough
             * to be considered a legal raise*/
            else -> {
                val extra = bet - newState.lastLegalBet
                newState.copy(extraBet = extra)
            }
        }
    }

    override fun innerIsLegal(handState: HandState): Boolean = true
}
