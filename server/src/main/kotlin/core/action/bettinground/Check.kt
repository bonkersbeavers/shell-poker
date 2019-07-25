package core.action.bettinground

import core.gameflow.HandState

class Check : BettingAction() {

    // TODO: BUGGED FOR NOW!!!!
    override fun innerApply(handState: HandState): HandState {
        if (handState.activePlayer!!.position == handState.buttonPosition) {
            return handState.copy(activePlayer = null)
        }

        val nextPlayer = handState.nextDecisivePlayer(handState.activePlayer.position)

        return handState.copy(activePlayer = nextPlayer)
    }

    // TODO: BUGGED FOR NOW!!!!
    override fun innerIsLegal(handState: HandState): Boolean {
        return when {
            (handState.lastAggressor == null) -> true
            else -> false
        }
    }
}