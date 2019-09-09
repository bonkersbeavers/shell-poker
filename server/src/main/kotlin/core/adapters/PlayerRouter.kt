package core.adapters

import core.bettinground.ActionValidation
import core.bettinground.BettingAction
import core.bettinground.Fold
import core.gameflow.handstate.HandState

class PlayerRouter(val adapters: List<IPlayerAdapter>) {

    private val idToAdapter: Map<Int, IPlayerAdapter> = adapters.associateBy { it.playerId }

    fun requestAction(playerId: Int): BettingAction = idToAdapter.getValue(playerId).requestAction()

    fun sendActionValidation(playerId: Int, actionValidation: ActionValidation) =
            idToAdapter.getValue(playerId).sendActionValidation(actionValidation)

//    fun broadcastPlayerAction(playerId: Int, action: BettingAction) {}

    fun broadcastHandState(handState: HandState) {
        idToAdapter.values.forEach { it.sendHandUpdate(handState) }
    }
}
