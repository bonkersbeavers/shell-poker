package gameControl.adapters

import core.betting.ActionValidation
import core.betting.BettingAction
import core.flowUtils.PotResult
import core.flowUtils.ShowdownAction
import gameControl.gamestate.GameState
import core.handflow.HandState

/*
 * These updates are only temporary. We will need to design the updates hierarchy
 * and slightly refactor players updating logic.
 */

sealed class GameUpdate

data class ActionValidationUpdate(val validation: ActionValidation) : GameUpdate()
data class PlayerActionUpdate(val playerId: Int, val action: BettingAction) : GameUpdate()
data class HandStateUpdate(val newHandState: HandState) : GameUpdate()
data class GameStateUpdate(val newGameState: GameState) : GameUpdate()
data class ShowdownActionUpdate(val action: ShowdownAction) : GameUpdate()
data class PotResultUpdate(val potResult: PotResult) : GameUpdate()
