package core.bettinground

import core.gameflow.handstate.HandState
import core.gameflow.handstate.updateActivePlayer

object Call : BettingAction() {

    override val type: ActionType = ActionType.CALL

    override fun innerApply(handState: HandState): HandState {
        val activePlayer = handState.activePlayer!!

        val callAmount = when {
            (activePlayer.stack > handState.totalBet) -> handState.totalBet
            else -> activePlayer.stack
        }

        val updatedPlayer = activePlayer
                .withBet(callAmount)
                .copy(lastAction = ActionType.CALL)

        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerValidate(handState: HandState): ActionValidation = ValidAction
}
