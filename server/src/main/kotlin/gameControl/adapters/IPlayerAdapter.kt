package gameControl.adapters

import core.betting.BettingAction
import core.flowUtils.ShowdownAction

interface IPlayerAdapter {

    val playerId: Int

    fun sendUpdate(update: GameUpdate)
    fun requestBettingAction(): BettingAction
    fun requestShowdownAction(): ShowdownAction? = null
}
