package core.handflow.betting

import core.handflow.hand.ActionValidation
import core.handflow.hand.HandState
import core.handflow.hand.InvalidAction
import core.handflow.hand.ValidAction
import core.handflow.player.getBySeat

data class Check(override val seat: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.players.map {
            if (it.seat == seat) it.copy(currentActionType = BettingActionType.CHECK) else it
        }
        return handState.copy(players = newPlayerStates)
    }

    override fun validate(handState: HandState): ActionValidation {
        val player = handState.players.getBySeat(seat)!!
        return if (player.currentBet == handState.totalBet) {
            ValidAction
        } else {
            InvalidAction("cannot check when there is a bet to call")
        }
    }
}
