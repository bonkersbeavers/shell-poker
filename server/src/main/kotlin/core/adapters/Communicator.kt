package core.adapters

import core.bettinground.BettingAction
import core.gameflow.ShowdownAction

class Communicator {

    private var idToAdapter: MutableMap<Int, IPlayerAdapter> = HashMap()

    fun addAdapter(playerId: Int, adapter: IPlayerAdapter) {
        this.idToAdapter[playerId] = adapter
    }

    fun requestBettingAction(playerId: Int): BettingAction = idToAdapter.getValue(playerId).requestBettingAction()
    fun requestShowdownAction(playerId: Int): ShowdownAction? = idToAdapter.getValue(playerId).requestShowdownAction()
    fun sendUpdate(playerId: Int, update: Any) = idToAdapter.getValue(playerId).sendUpdate(update)
    fun broadcastUpdate(update: Any) = idToAdapter.values.forEach { it.sendUpdate(update) }
}
