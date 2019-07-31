package core.action.bettinground

import core.gameflow.HandState

class Raise(val size: Int) : BettingAction(ActionType.RAISE) {

    override fun innerApply(handState: HandState): HandState {

        val updatedPlayer = handState.activePlayer!!
                .withBet(size)
                .copy(lastAction = ActionType.RAISE)

        val betDifference = size - handState.lastLegalBet

        return handState
                .updateActivePlayer(updatedPlayer)
                .copy(lastLegalBet = size,
                        extraBet = 0,
                        minRaise = size + betDifference,
                        lastAggressor = updatedPlayer)
    }

    override fun innerValidate(handState: HandState): ActionValidation {
        val player = handState.activePlayer!!

        return when {
            handState.lastLegalBet == 0 ->
                InvalidAction("Cannot raise when no bet has been made")

            size > player.maxBet ->
                InvalidAction("Raise of size $size higher than player's maximum possible bet ${player.maxBet}")

            size < handState.minRaise ->
                InvalidAction("Raise of size $size smaller than minimum legal raise ${handState.minRaise}")

            else -> ValidAction()
        }
    }
}
