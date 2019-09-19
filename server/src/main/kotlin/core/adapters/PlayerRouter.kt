package core.adapters

import core.bettinground.BettingAction
import core.gameflow.RoomSettings
import core.gameflow.player.Player

data class PlayersUpdate(val players: List<Player>, val newPlayersIds: Set<Int>)

class PlayerRouter(val roomSettings: RoomSettings) {

    fun registerPlayer(position: Int, stack: Int) {
        TODO()
    }

    fun getPlayers(): PlayersUpdate {
        TODO()
    }

    fun requestBettingAction(playerId: Int): BettingAction {
        TODO()
    }

    fun requestShowdownAction(playerId: Int): BettingAction {
        TODO()
    }

    fun sendUpdate(playerId: Int, update: Any) {
        TODO()
    }

    fun broadcastUpdate(update: Any) {
        TODO()
    }
}
