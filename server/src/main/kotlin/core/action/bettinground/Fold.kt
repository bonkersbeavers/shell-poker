package core.action.bettinground

import core.gameflow.HandState

class Fold : BettingAction {

    override fun innerApply(handState: HandState): HandState {
        val player = handState.activePlayer!!
        val updatedPlayer = player.copy(
                lastAction = this,
                chipsInPot = player.chipsInPot + player.currentBet
        )

        return handState.updateActivePlayer(updatedPlayer)
    }

    override fun innerIsLegal(handState: HandState): Boolean = true
}
