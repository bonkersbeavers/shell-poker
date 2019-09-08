package core.gameflow

import core.adapters.PlayerRouter
import core.bettinground.*
import core.gameflow.handstate.HandState

class GameManager(handState: HandState) {

    private val dealer: Dealer = Dealer()
    private val playerRouter = PlayerRouter()

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

    private fun playHand(startingHandState: HandState): HandState {
        var handState = startingHandState

        return handState
    }

    private fun bettingRound(startingHandState: HandState): HandState {
        var handState = startingHandState

        while(handState.activePlayer != null) {
            var action: BettingAction
            var actionValidation: ActionValidation

            do {
                action = playerRouter.getAction(handState.activePlayer!!.id)
                actionValidation = action.validate(handState)
                playerRouter.sendPrivateUpdate(handState.activePlayer!!.id, actionValidation)
            }
            while(actionValidation != ValidAction)

            playerRouter.broadcastPlayerAction(handState.activePlayer!!.id, action)
            handState = action.apply(handState)
            playerRouter.broadcast(handState)
        }

        return handState
    }
}
