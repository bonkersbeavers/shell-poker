package core.adapters

import core.bettinground.BettingAction
import core.gameflow.ShowdownAction

interface ICommunicator {

    fun addAdapter(adapter: IPlayerAdapter)
    fun requestBettingAction(playerId: Int): BettingAction
    fun requestShowdownAction(playerId: Int): ShowdownAction? = null
    fun sendUpdate(playerId: Int, update: GameUpdate)
    fun broadcastUpdate(update: GameUpdate)
}
