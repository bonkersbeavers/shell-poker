package core.hand.player.betting

import core.betting.ActionType
import core.hand.HandState
import core.hand.player.getBySeat

data class Bet(override val seat: Int, val chips: Int): BettingAction(seat) {
    override val actionType: ActionType = ActionType.BET

    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) {
                it.withBet(chips).copy(currentActionType = ActionType.BET)
            }
            else it
        }

        return handState.copy(
                playersStates = newPlayerStates,
                lastLegalBet = chips,
                extraBet = 0,
                minRaise = chips * 2
        )
    }

    override fun validate(handState: HandState): Boolean {
        val player = handState.playersStates.getBySeat(seat)!!
        return (handState.lastLegalBet == 0) && (chips >= handState.minRaise) && (player.maxBet >= chips)
    }
}
