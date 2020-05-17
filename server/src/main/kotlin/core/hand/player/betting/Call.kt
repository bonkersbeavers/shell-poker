package core.hand.player.betting

import core.betting.ActionType
import core.hand.HandState

data class Call(override val seat: Int): BettingAction(seat) {
    override val actionType: ActionType = ActionType.CALL

    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) {
                val possibleBet = minOf(it.maxBet, handState.totalBet)
                it.withBet(possibleBet).copy(currentActionType = ActionType.CALL)
            }
            else it
        }

        return handState.copy(playersStates = newPlayerStates)
    }

    override fun validate(handState: HandState): Boolean {
        return true
    }
}
