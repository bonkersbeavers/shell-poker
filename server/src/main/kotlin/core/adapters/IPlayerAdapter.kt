package core.adapters

import core.bettinground.BettingAction
import core.gameflow.ShowdownAction

interface IPlayerAdapter {

    val playerId: Int

    fun sendUpdate(update: Any) // TODO
    fun requestBettingAction(): BettingAction
    fun requestShowdownAction(): ShowdownAction?
}
