package core.adapters

import core.bettinground.ActionValidation
import core.bettinground.BettingAction
import core.bettinground.Fold
import core.gameflow.handstate.HandState

class PlayerRouter {
    fun getAction(playerId: Int): BettingAction = Fold
    fun sendPrivateUpdate(playerId: Int, actionValidation: ActionValidation) {}
    fun sendPrivateUpdate(playerId: Int, handState: HandState) {}
    fun broadcastPlayerAction(playerId: Int, action: BettingAction) {}
    fun broadcast(handState: HandState) {}
}
