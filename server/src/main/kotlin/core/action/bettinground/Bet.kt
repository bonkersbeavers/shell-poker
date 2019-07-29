package core.action.bettinground

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
                        minRaise = (size - handState.totalBet) * 2,
                        lastLegalBet = size)
    }

    override fun innerValidate(handState: HandState): ActionValidation {
        val activePlayer = handState.activePlayer!!

        return when {

            size < handState.minRaise ->
                InvalidAction("Bet of size $size smaller than minimum legal bet ${handState.minRaise}")

            size > activePlayer.stack ->
                InvalidAction("Bet of size $size larger than player's stack ${activePlayer.stack}")

            handState.lastLegalBet > 0 ->
                InvalidAction("A bet has already been made in current betting round")

            else -> ValidAction()
        }
    }
}
