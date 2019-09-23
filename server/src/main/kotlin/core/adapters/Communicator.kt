package core.adapters

import core.bettinground.BettingAction
import core.gameflow.ShowdownAction

class Communicator {

    private var idToAdapter: MutableMap<Int, IPlayerAdapter> = HashMap()

    fun addAdapter(adapter: IPlayerAdapter) {
        this.idToAdapter[adapter.playerId] = adapter
    }

    fun requestBettingAction(playerId: Int): BettingAction = idToAdapter.getValue(playerId).requestBettingAction()
    fun requestShowdownAction(playerId: Int): ShowdownAction? = idToAdapter.getValue(playerId).requestShowdownAction()
    fun sendUpdate(playerId: Int, update: GameUpdate) = idToAdapter.getValue(playerId).sendUpdate(update)
    fun broadcastUpdate(update: GameUpdate) = idToAdapter.values.forEach { it.sendUpdate(update) }
}
