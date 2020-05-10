package testutil.fake

import gameControl.adapters.GameUpdate
import gameControl.adapters.ICommunicator
import gameControl.adapters.IPlayerAdapter
import core.betting.BettingAction
import core.flowUtils.ShowdownAction

class FakeCommunicator(actions: List<BettingAction>) : ICommunicator {

    private val actionIterator: Iterator<BettingAction> = actions.iterator()
    override fun requestBettingAction(playerId: Int): BettingAction = actionIterator.next()

    override fun addAdapter(adapter: IPlayerAdapter) {}
    override fun requestShowdownAction(playerId: Int): ShowdownAction? = null
    override fun sendUpdate(playerId: Int, update: GameUpdate) {}
    override fun broadcastUpdate(update: GameUpdate) {}
}
