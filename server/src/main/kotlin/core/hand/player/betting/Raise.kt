package core.hand.player.betting

import core.betting.ActionType
import core.hand.HandState
import core.hand.player.getBySeat

data class Raise(override val seat: Int, val chips: Int): BettingAction(seat) {
    override val actionType: ActionType = ActionType.RAISE

    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.playersStates.map {
            if (it.seat == seat) {
                it.withBet(chips).copy(currentActionType = ActionType.RAISE)
            }
            else it
        }
        val stateWithUpdatedPlayer = handState.copy(playersStates = newPlayerStates)

        val raiseIsBigEnough = chips >= handState.minRaise
        val raisedAmount = chips - handState.lastLegalBet

        if (raiseIsBigEnough) {
            return stateWithUpdatedPlayer.copy(
                    lastLegalBet = chips,
                    minRaise = chips + raisedAmount,
                    extraBet = 0
            )
        } else {
            // The raise is player's all-in, but is not high enough to be considered
            // as legal raise. Only extraBet parameter should be updated in such case.
            return stateWithUpdatedPlayer.copy(
                    extraBet = raisedAmount
            )
        }
    }

    override fun validate(handState: HandState): Boolean {
        val player = handState.playersStates.getBySeat(seat)!!

        val playerIsAllowedToRaise = (player.currentBet < handState.lastLegalBet) or (player.currentActionType == ActionType.POST)
        val playerHasEnoughChips = player.maxBet >= chips
        val raiseIsBigEnough = chips >= handState.minRaise
        val betHasBeenMade = handState.lastLegalBet > 0
        val raiseIsPlayersAllIn = chips == player.maxBet

        return playerIsAllowedToRaise && playerHasEnoughChips && betHasBeenMade &&
                (raiseIsBigEnough || raiseIsPlayersAllIn)
    }
}
