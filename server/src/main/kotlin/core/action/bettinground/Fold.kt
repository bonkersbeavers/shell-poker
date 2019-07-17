package core.action.bettinground

import core.gameflow.HandState
import core.gameflow.findNextPlayer

class Fold : BettingAction() {

    override fun apply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.afterFold()
        val updatedState = handState.updateActivePlayer(updatedPlayer)

        val activePlayers = updatedState.playersInGame()
        val nextPlayer = findNextPlayer(activePlayers, updatedPlayer.position)

        return when {
            (activePlayers.size == 1) or (nextPlayer == handState.lastAggressor) ->
                updatedState.copy(activePlayer = null)

            else -> updatedState.copy(activePlayer = nextPlayer)
        }
    }

    override fun innerIsLegal(handState: HandState): Boolean = true
}
