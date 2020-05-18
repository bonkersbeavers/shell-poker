package core.hand.betting

import core.hand.ActionValidation
import core.hand.HandState
import core.hand.ValidAction

data class Fold(override val seat: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.players.map {
            if (it.seat == seat) it.copy(currentActionType = BettingActionType.FOLD) else it
        }
        return handState.copy(players = newPlayerStates)
    }

    override fun validate(handState: HandState): ActionValidation = ValidAction
}
