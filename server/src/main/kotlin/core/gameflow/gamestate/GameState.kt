package core.gameflow.gamestate

import core.gameflow.Blinds
import core.gameflow.Positions

data class GameState(
        val playersInfo: List<PlayerInfo>,
        val positions: Positions,
        val blinds: Blinds,
        val handsPlayed: Int = 0
)
