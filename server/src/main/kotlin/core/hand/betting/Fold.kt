package core.hand.betting

import core.hand.ActionValidation
import core.hand.HandState
import core.hand.ValidAction

data class Fold(override val seat: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) it.copy(currentActionType = BettingActionType.FOLD) else it
        }
        return handState.copy(playersStates = newPlayerStates)
    }

    override fun validate(handState: HandState): ActionValidation = ValidAction
}
