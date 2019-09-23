package core.gameflow.handstate

import core.gameflow.gamestate.GameState

interface IHandManager {
    fun playHand(gameState: GameState, newPlayersIds: Set<Int>): GameState
}
