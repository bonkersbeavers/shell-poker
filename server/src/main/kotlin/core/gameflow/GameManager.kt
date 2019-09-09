package core.gameflow

import core.bettinground.*
import core.gameflow.handstate.HandState
import core.gameflow.player.Player

class GameManager(handState: HandState) {

    val dealer: Dealer = Dealer()

    /**
     * Main loop:
     *
     * 1) post blinds (metoda w Blinds)
     *
     * init action
     * pre flop betting
     * collect bets
     * if (action ended)
     *   showdown / end hand
     * deal flop
     *
     * init action
     * flop betting
     * collect bets
     * deal turn
     *
     * init action
     * flop betting
     * collect bets
     * deal river
     *
     * init action
     * flop betting
     * collect bets
     *
     *
     */

    private fun bettingRound(startingHandState: HandState): HandState {
        var handState = startingHandState

        while (handState.activePlayer != null) {
            var action: BettingAction
            var actionValidation: ActionValidation

            do {
                action = getAction(handState.activePlayer!!)
                actionValidation = action.validate(handState)
                sendValidation(handState.activePlayer!!, actionValidation)
            }
            while (actionValidation != ValidAction)

            handState = action.apply(handState)
            sendUpdate(handState)
        }

        return handState
    }

    private fun getAction(player: Player): BettingAction {
        return Fold
    }

    private fun sendUpdate(state: HandState) {}
    private fun sendValidation(player: Player, action: ActionValidation) {}
}
