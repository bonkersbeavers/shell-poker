package core.hand.player.betting

import core.betting.ActionType
import core.hand.HandState

data class Fold(override val seat: Int): BettingAction(seat) {
    override val actionType: ActionType = ActionType.FOLD

    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) it.copy(currentActionType = ActionType.FOLD) else it
        }
        return handState.copy(playersStates = newPlayerStates)
    }

    override fun validate(handState: HandState): Boolean {
        return true
    }
}
