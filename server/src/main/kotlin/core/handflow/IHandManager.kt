package core.handflow

import gameControl.gamestate.GameState

interface IHandManager {
    fun playHand(gameState: GameState): GameState
}
