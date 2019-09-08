package core.adapters

import core.bettinground.ActionValidation
import core.bettinground.BettingAction
import core.bettinground.Fold
import core.gameflow.handstate.HandState

class PlayerRouter {
    private fun getAction(playerId: Int): BettingAction = Fold
    private fun update(playerId: Int, actionValidation: ActionValidation) {}
    private fun update(playerId: Int, handState: HandState) {}
    private fun broadcast(playerId: Int, action: BettingAction) {}
    private fun broadcast(handState: HandState) {}
}
