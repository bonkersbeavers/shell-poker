package gameControl.adapters

import core.betting.BettingAction
import core.flowUtils.ShowdownAction

class Communicator : ICommunicator {

    private var idToAdapter: MutableMap<Int, IPlayerAdapter> = HashMap()

    override fun addAdapter(adapter: IPlayerAdapter) {
        this.idToAdapter[adapter.playerId] = adapter
    }

    override fun requestBettingAction(playerId: Int): BettingAction = idToAdapter.getValue(playerId).requestBettingAction()
    override fun requestShowdownAction(playerId: Int): ShowdownAction? = idToAdapter.getValue(playerId).requestShowdownAction()
    override fun sendUpdate(playerId: Int, update: GameUpdate) = idToAdapter.getValue(playerId).sendUpdate(update)
    override fun broadcastUpdate(update: GameUpdate) = idToAdapter.values.forEach { it.sendUpdate(update) }
}
