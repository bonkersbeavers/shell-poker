package core.handflow.betting

import core.handflow.hand.ActionValidation
import core.handflow.hand.HandState
import core.handflow.hand.InvalidAction
import core.handflow.hand.ValidAction
import core.handflow.player.getBySeat

data class Bet(override val seat: Int, val chips: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val newPlayerStates = handState.players.map {
            if (it.seat == seat) {
                it.withBet(chips).copy(currentActionType = BettingActionType.BET)
            }
            else it
        }

        return if (chips >= handState.minRaise) {
            handState.copy(
                    players = newPlayerStates,
                    lastLegalBet = chips,
                    extraBet = 0,
                    minRaise = chips * 2
            )
        } else {
            handState.copy(
                    players = newPlayerStates,
                    extraBet = chips
            )
        }
    }

    override fun validate(handState: HandState): ActionValidation {
        val player = handState.players.getBySeat(seat)!!
        val betAlreadyMade = handState.lastLegalBet > 0
        val playerHasEnoughChips = player.maxBet >= chips
        val betIsHighEnough = chips >= handState.minRaise
        val betIsPlayersAllIn = chips == player.maxBet

        return when {
            betAlreadyMade ->
                InvalidAction("Cannot bet when a bet has already been made")

            playerHasEnoughChips.not() ->
                InvalidAction("Bet of size $chips is larger than player's maximum possible bet ${player.maxBet}")

            betIsHighEnough.not() and betIsPlayersAllIn.not() ->
                InvalidAction("Bet of size $chips smaller than minimum legal bet ${handState.minRaise}")

            else -> ValidAction
        }
    }
}
