package core.handflow

import gameControl.gamestate.GameState

interface IDeprecatedHandManager {
    fun playHand(gameState: GameState): GameState
}
