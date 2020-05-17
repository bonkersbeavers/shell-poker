package core.hand.player.betting

import core.betting.ActionType
import core.hand.HandState
import core.hand.player.getBySeat

data class Check(override val seat: Int): BettingAction(seat) {
    override val actionType: ActionType = ActionType.CHECK

    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) it.copy(currentActionType = ActionType.CHECK) else it
        }
        return handState.copy(playersStates = newPlayerStates)
    }

    override fun validate(handState: HandState): Boolean {
        val player = handState.playersStates.getBySeat(seat)!!
        return player.currentBet == handState.totalBet
    }
}
