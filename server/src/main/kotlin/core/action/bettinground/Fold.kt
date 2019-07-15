package core.action.bettinground

import core.gameflow.HandState
import core.gameflow.findNextPlayer
import core.gameflow.getActivePlayers

class Fold : BettingAction() {

    override fun apply(handState: HandState): HandState {
        val updatedPlayer = handState.activePlayer!!.afterFold()
        val updatedState = handState.updateActivePlayer(updatedPlayer)

        val activePlayers = getActivePlayers(updatedState.players)
        val nextPlayer = findNextPlayer(activePlayers, updatedPlayer.position)

        return when {
            (activePlayers.size == 1) or (nextPlayer == handState.lastAggressor) ->
                updatedState.copy(activePlayer = null)

            else -> updatedState.copy(activePlayer = nextPlayer)
        }
    }

    override fun isLegal(handState: HandState): Boolean = handState.activePlayer != null
}
