package core.hand.player.betting

import core.hand.ActionValidation
import core.hand.HandState
import core.hand.InvalidAction
import core.hand.ValidAction
import core.hand.player.getBySeat

data class Raise(override val seat: Int, val chips: Int): BettingAction(seat) {
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
            // The raise is player's all-in, but is not high enough to be considered a legal raise.
            return stateWithUpdatedPlayer.copy(
                    extraBet = raisedAmount
            )
        }
    }

    override fun validate(handState: HandState): ActionValidation {
        val player = handState.playersStates.getBySeat(seat)!!

        val playerIsAllowedToRaise = (player.currentBet < handState.lastLegalBet) or (player.currentActionType == ActionType.POST)
        val playerHasEnoughChips = player.maxBet >= chips
        val raiseIsBigEnough = chips >= handState.minRaise
        val betHasBeenMade = handState.lastLegalBet > 0
        val raiseIsPlayersAllIn = chips == player.maxBet

        return when {
            playerIsAllowedToRaise.not() ->
                InvalidAction("cannot raise if betting action has not been restarted since player's previous play")

            playerHasEnoughChips.not() ->
                InvalidAction("raise of size $chips is higher than player's maximum possible bet ${player.maxBet}")

            betHasBeenMade.not() ->
                InvalidAction("cannot raise if no bet has been made")

            raiseIsBigEnough.not() and raiseIsPlayersAllIn.not() ->
                InvalidAction("raise of size $chips is smaller than minimum legal raise ${handState.minRaise}")

            else -> ValidAction
        }
    }
}
