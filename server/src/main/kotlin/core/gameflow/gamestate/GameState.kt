package core.gameflow.gamestate

import core.gameflow.Blinds
import core.gameflow.Positions

data class PlayerStatus(val position: Int, val stack: Int) {
    val id: Int = position
}

data class GameState(
        val playersInfo: List<PlayerStatus>,
        val positions: Positions,
        val blinds: Blinds,
        val handsPlayed: Int = 0
)
