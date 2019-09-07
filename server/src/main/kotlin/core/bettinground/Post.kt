package core.bettinground

import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild
import core.gameflow.handstate.updateActivePlayer

data class Post(val size: Int) : BettingAction() {

    override val type: ActionType = ActionType.POST

    override fun innerApply(handState: HandState): HandState {
        val activePlayer = handState.activePlayer!!
        val bet = when {
            size > activePlayer.maxBet -> activePlayer.maxBet
            else -> size
        }

        val playerUpdate = activePlayer
                .withBet(bet)
                .copy(lastAction = ActionType.POST)

        val newState = handState.updateActivePlayer(playerUpdate)

        return when {
            /* When posted bet is lower than current bet in the hand. */
            bet <= handState.totalBet -> newState

            /* When posted bet has an effect of raising current bet in the hand. */
            bet >= handState.minRaise -> {
                val difference = bet - handState.lastLegalBet
                val newMinRaise = bet + difference
                newState.rebuild(lastLegalBet = bet, extraBet = 0, minRaise = newMinRaise)
            }

            /* When posted bet is higher than current bet in the hand but not high enough
             * to be considered a legal raise*/
            else -> {
                val extra = bet - newState.lastLegalBet
                newState.rebuild(extraBet = extra)
            }
        }
    }

    override fun innerValidate(handState: HandState): ActionValidation = ValidAction
}
