package core.gameflow.table

import core.gameflow.gamestate.GameState
import core.gameflow.player.PlayerStatus

class TableManager {
    private var playersInGame: List<PlayerStatus> = emptyList()
    private var pendingPlayers: List<PlayerStatus> = emptyList()

    fun registerNewPlayer(position: Int) {
        TODO()
    }

    fun makePlayerBuyIn(position: Int, stack: Int) {
        TODO()
    }

    fun getUpdatedGameState(gameState: GameState): GameState {
        val przejebani = gameState.playersStatuli.filter { it.stack == 0 }
        TODO()
    }
}
