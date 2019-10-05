package core.adapters

import core.bettinground.BettingAction
import core.gameflow.ShowdownAction

interface IPlayerAdapter {

    val playerId: Int

    fun sendUpdate(update: GameUpdate)
    fun requestBettingAction(): BettingAction
    fun requestShowdownAction(): ShowdownAction? = null
}
