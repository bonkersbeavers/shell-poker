package core.action.bettinground

import core.gameflow.HandState

class Fold : BettingAction() {

    override fun innerApply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.afterFold()
        val updatedState = handState.updateActivePlayer(updatedPlayer)

        val nextPlayer = updatedState.nextDecisivePlayer(updatedPlayer)

        return when {
            (updatedState.decisivePlayers.size == 1) or (nextPlayer == handState.lastAggressor) ->
                updatedState.copy(activePlayer = null)

            else -> updatedState.copy(activePlayer = nextPlayer)
        }
    }

    override fun innerIsLegal(handState: HandState): Boolean = true
}
