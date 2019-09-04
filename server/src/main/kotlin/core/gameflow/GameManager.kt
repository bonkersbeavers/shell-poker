package core.gameflow

import core.bettinground.ActionValidation
import core.bettinground.BettingAction
import core.bettinground.Fold
import core.bettinground.ValidAction

class GameManager(handState: HandState) {

    val dealer: Dealer = Dealer()


    fun bettingRound(handState: HandState) {

        val activePlayer = handState.activePlayer!!

        while(activePlayer != handState.lastAggressor) {
            var action: BettingAction

            do {
                action = getAction(activePlayer)
                val actionValidation: ActionValidation = action.validate(handState)
            } while (actionValidation == ValidAction())
        }
    }

    private fun getAction(player: Player): BettingAction {
        return Fold();
    }

    private fun sendValidation(player: Player) {}
}
