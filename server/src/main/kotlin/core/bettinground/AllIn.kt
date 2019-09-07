package core.bettinground

import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild
import core.gameflow.handstate.updateActivePlayer

object AllIn : BettingAction() {

    override val type: ActionType = ActionType.ALL_IN

    override fun innerApply(handState: HandState): HandState {
        val player = handState.activePlayer!!
        val betSize = player.maxBet

        val updatedPlayer = player
                .withBet(betSize)
                .copy(lastAction = ActionType.ALL_IN)

        val newState = handState.updateActivePlayer(updatedPlayer)

        return when {
            /* All in lower or equal to previous highest bet. */
            betSize <= handState.totalBet -> newState

            /* All in higher than previous highest bet, but lower than min raise. */
            betSize < handState.minRaise -> {
                val betDifference = betSize - handState.lastLegalBet
                newState.rebuild(extraBet = betDifference)
            }

            /* All in with effect of a regular raise. */
            else -> {
                val betDifference = betSize - handState.lastLegalBet
                newState.rebuild(
                        lastLegalBet = betSize,
                        extraBet = 0,
                        minRaise = betSize + betDifference,
                        lastAggressor = updatedPlayer
                )
            }
        }
    }

    override fun innerValidate(handState: HandState): ActionValidation = ValidAction
}
