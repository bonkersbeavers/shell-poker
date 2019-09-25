package fake

import core.adapters.GameUpdate
import core.adapters.ICommunicator
import core.adapters.IPlayerAdapter
import core.bettinground.BettingAction
import core.gameflow.ShowdownAction

class FakeCommunicator(actions: List<BettingAction>) : ICommunicator {

    private val actionIterator: Iterator<BettingAction> = actions.iterator()
    override fun requestBettingAction(playerId: Int): BettingAction = actionIterator.next()

    override fun addAdapter(adapter: IPlayerAdapter) {}
    override fun requestShowdownAction(playerId: Int): ShowdownAction? = null
    override fun sendUpdate(playerId: Int, update: GameUpdate) {}
    override fun broadcastUpdate(update: GameUpdate) {}
}