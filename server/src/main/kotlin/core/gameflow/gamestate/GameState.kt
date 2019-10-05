package core.gameflow.gamestate

import core.gameflow.Blinds
import core.gameflow.Positions
import core.gameflow.player.PlayerStatus

data class GameState(
    val playersStatuli: List<PlayerStatus>,
    val positions: Positions,
    val blinds: Blinds,
    val newPlayersIds: List<Int> = emptyList(),
    val handsPlayed: Int = 0
)
