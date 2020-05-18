package core.betting

import core.hand.player.betting.ActionType
import core.handflow.HandState
import core.handflow.rebuild
import core.handflow.updateActivePlayer

data class Bet(val size: Int) : BettingAction() {

    override val type: ActionType = ActionType.BET

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState
                .activePlayer!!
                .withBet(size)
                .copy(lastAction = ActionType.BET)

        return handState
                .updateActivePlayer(updatedPlayer)
                .rebuild(lastAggressor = updatedPlayer,
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

            else -> ValidAction
        }
    }
}
