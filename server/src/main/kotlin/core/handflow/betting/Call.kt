package core.handflow.betting

import core.handflow.hand.ActionValidation
import core.handflow.hand.HandState
import core.handflow.hand.InvalidAction
import core.handflow.hand.ValidAction
import core.handflow.player.getBySeat

data class Call(override val seat: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.players.map {
            if (it.seat == seat) {
                val possibleBet = minOf(it.maxBet, handState.totalBet)
                it.withBet(possibleBet).copy(currentActionType = BettingActionType.CALL)
            }
            else it
        }

        return handState.copy(players = newPlayerStates)
    }

    override fun validate(handState: HandState): ActionValidation {
        val player = handState.players.getBySeat(seat)!!
        return if (player.currentBet < handState.totalBet) {
            ValidAction
        } else {
            InvalidAction("no bet higher than player's current bet was made")
        }
    }
}
