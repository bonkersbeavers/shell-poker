package core.bettinground

import core.gameflow.HandState

class Bet(val size: Int) : BettingAction(ActionType.BET) {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState
                .activePlayer!!
                .withBet(size)
                .copy(lastAction = ActionType.BET)

        return handState
                .updateActivePlayer(updatedPlayer)
                .copy(lastAggressor = updatedPlayer,
                        minRaise = size * 2,
                        lastLegalBet = size,
                        extraBet = 0)
    }

    override fun innerValidate(handState: HandState): ActionValidation {
        val player = handState.activePlayer!!

        return when {
            handState.lastLegalBet > 0 ->
                InvalidAction("Cannot bet when a bet has already been made")

            size > player.maxBet ->
                InvalidAction("Bet of size $size larger than player's maximum possible bet ${player.maxBet}")

            size < handState.minRaise ->
                InvalidAction("Bet of size $size smaller than minimum legal bet ${handState.minRaise}")

            else -> ValidAction()
        }
    }
}
