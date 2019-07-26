package core.action.bettinground
import core.gameflow.HandState

interface BettingAction {

    /*
     * This method should only update active player, fields related to current bet size
     * and lastAggressor field, activePlayer must not be shifted.
     */
    fun innerApply(handState: HandState): HandState
    fun innerIsLegal(handState: HandState): Boolean

    fun apply(handState: HandState): HandState {
        assert(isLegal(handState))

        val newState = innerApply(handState)
        return shiftActivePlayer(newState)
    }

    fun isLegal(handState: HandState): Boolean = (handState.activePlayer != null) and innerIsLegal(handState)

    private fun shiftActivePlayer(handState: HandState): HandState {

        handState.activePlayer!!
        val nextDecisivePlayer = handState.nextDecisivePlayer(handState.activePlayer)

        if (nextDecisivePlayer != null) {

            /* The case when the next decisive player didn't have a chance to take any action yet. */
            if ((nextDecisivePlayer.lastAction == null) or (nextDecisivePlayer.lastAction is Post))
                return handState.copy(activePlayer = nextDecisivePlayer)

            /*
             * These are the cases when the action should end:
             *  - only one player is left in the hand
             *  - active player was the last decisive player in the hand
             *  - everyone called / folded to the next player's bet
             *  - no bet was made and everyone checked / folded
             */
            if ((handState.playersInGame.size == 1) or
                    (nextDecisivePlayer == handState.activePlayer) or
                    ((handState.totalBet > 0) and (nextDecisivePlayer.currentBet == handState.totalBet)) or
                    ((handState.totalBet == 0) and (nextDecisivePlayer.lastAction is Check)))
                return handState.copy(activePlayer = null)
        }

        /* Regular active player shift (or setting active player to null if no decisive players were found). */
        return handState.copy(activePlayer = nextDecisivePlayer)
    }
}
