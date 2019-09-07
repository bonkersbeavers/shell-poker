package core.gameflow

import core.bettinground.*
import core.gameflow.handstate.HandState
import core.gameflow.player.Player

class GameManager(handState: HandState) {

    val dealer: Dealer = Dealer()
    
    private fun bettingRound(startingHandState: HandState): HandState {
        var handState = startingHandState
        var activePlayer: Player? = handState.activePlayer

        while(activePlayer != null) {
            var action: BettingAction
            var actionValidation: ActionValidation

            do {
                action = getAction(activePlayer)
                actionValidation = action.validate(handState)
                sendValidation(activePlayer, actionValidation)
            }
            while(actionValidation != ValidAction)

            handState = action.apply(handState)
            activePlayer = handState.activePlayer

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
