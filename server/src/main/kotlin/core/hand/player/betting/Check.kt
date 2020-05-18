package core.hand.player.betting

import core.hand.ActionValidation
import core.hand.HandState
import core.hand.InvalidAction
import core.hand.ValidAction
import core.hand.player.getBySeat

data class Check(override val seat: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) it.copy(currentActionType = ActionType.CHECK) else it
        }
        return handState.copy(playersStates = newPlayerStates)
    }

    override fun validate(handState: HandState): ActionValidation {
        val player = handState.playersStates.getBySeat(seat)!!
        return if (player.currentBet == handState.totalBet) {
            ValidAction
        } else {
            InvalidAction("cannot check when there is a bet to call")
        }
    }
}
