package core.hand.player.betting

import core.hand.ActionValidation
import core.hand.HandState
import core.hand.ValidAction

data class Post(override val seat: Int, val chips: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) {
                val possibleBet = minOf(it.maxBet, chips)
                it.withBet(possibleBet).copy(currentActionType = ActionType.POST)
            }
            else it
        }

        return handState.copy(playersStates = newPlayerStates)
    }

    override fun validate(handState: HandState): ActionValidation = ValidAction
}
