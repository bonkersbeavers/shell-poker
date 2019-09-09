package core.adapters

import core.bettinground.ActionValidation
import core.bettinground.BettingAction
import core.gameflow.handstate.HandState

interface IPlayerAdapter {

    val playerId: Int

    fun sendHandUpdate(handState: HandState)
    fun sendActionValidation(validation: ActionValidation)
    fun requestAction(): BettingAction
}
