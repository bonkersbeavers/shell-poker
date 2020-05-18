package core.betting
import core.hand.player.betting.ActionType
import core.handflow.HandState
import core.handflow.rebuild
import core.player.inGame
import core.player.nextDecisive

abstract class BettingAction {

    abstract val type: ActionType

    /**
     * This method must not shift the activePlayer pointer as this logic
     * is extracted to shiftActivePlayer method that is always called after innerApply.
     */
    protected abstract fun innerApply(handState: HandState): HandState
    protected abstract fun innerValidate(handState: HandState): ActionValidation

    fun apply(handState: HandState): HandState {
        assert(validate(handState) is ValidAction)

        val newState = innerApply(handState)
        return shiftActivePlayer(newState)
    }

    fun validate(handState: HandState): ActionValidation {
        return when (handState.activePlayer) {
            null -> InvalidAction("There is no active player")
            else -> innerValidate(handState)
        }
    }

    private fun shiftActivePlayer(handState: HandState): HandState {

        handState.activePlayer!!
        val nextDecisivePlayer = handState.players.nextDecisive(handState.activePlayer)

        return when {
            /* End action when there are no more decisive players. */
            nextDecisivePlayer == null -> handState.rebuild(activePlayer = null)

            /* End action when only one player is left in the hand. */
            handState.players.inGame().size == 1 -> handState.rebuild(activePlayer = null)

            /* End action when current action taker is the last decisive player in the hand. */
            nextDecisivePlayer == handState.activePlayer -> handState.rebuild(activePlayer = null)

            /* Proceed when next decisive player didn't have a chance to take any action yet. */
            (nextDecisivePlayer.lastAction == null) or (nextDecisivePlayer.lastAction == ActionType.POST) ->
                handState.rebuild(activePlayer = nextDecisivePlayer)

            /* End action when no one raised next player's bet. */
            /* Exception: when action gets to BB pre-flop and no one raised, handled in previous case. */
            (handState.totalBet > 0) and (nextDecisivePlayer.bet == handState.totalBet) ->
                handState.rebuild(activePlayer = null)

            /* End action when no bet was made and everyone checked or folded. */
            (handState.totalBet == 0) and (nextDecisivePlayer.lastAction == ActionType.CHECK) ->
                handState.rebuild(activePlayer = null)

            /* Proceed in other cases. */
            else -> handState.rebuild(activePlayer = nextDecisivePlayer)
        }
    }
}
