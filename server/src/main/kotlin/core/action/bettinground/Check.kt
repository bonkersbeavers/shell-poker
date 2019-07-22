package core.action.bettinground

import core.gameflow.HandState
import core.gameflow.findNextPlayer

class Check : BettingAction() {

    override fun apply(handState: HandState): HandState {
        if (handState.activePlayer!!.position == handState.buttonPosition) {
            return handState.copy(activePlayer = null)
        }

        val activePlayers = handState.playersInGame()
        val nextPlayer = findNextPlayer(activePlayers, handState.activePlayer.position)

        return handState.copy(activePlayer = nextPlayer)
    }

    override fun innerIsLegal(handState: HandState): Boolean {
        return when {
            (handState.lastAggressor == null) -> true
            else -> false
        }
    }
}
