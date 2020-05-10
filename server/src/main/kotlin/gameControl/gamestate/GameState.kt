package gameControl.gamestate

import core.flowUtils.Blinds
import core.flowUtils.Positions
import core.player.PlayerStatus

data class GameState(
    val playersStatuli: List<PlayerStatus>,
    val positions: Positions,
    val blinds: Blinds,
    val newPlayersIds: List<Int> = emptyList(),
    val handsPlayed: Int = 0
)
