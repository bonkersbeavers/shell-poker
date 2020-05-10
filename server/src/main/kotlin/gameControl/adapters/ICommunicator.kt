package gameControl.adapters

import core.betting.BettingAction
import core.flowUtils.ShowdownAction

interface ICommunicator {

    fun addAdapter(adapter: IPlayerAdapter)
    fun requestBettingAction(playerId: Int): BettingAction
    fun requestShowdownAction(playerId: Int): ShowdownAction? = null
    fun sendUpdate(playerId: Int, update: GameUpdate)
    fun broadcastUpdate(update: GameUpdate)
}
