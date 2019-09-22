package core.gameflow.gamestate

import core.adapters.Communicator
import core.adapters.IPlayerAdapter
import core.bettinground.*
import core.gameflow.*
import core.gameflow.handstate.HandManager
import core.gameflow.player.*

class GameManager(initialState: GameState, val communicator: Communicator, val roomSettings: RoomSettings) {

    private var currentGameState: GameState = initialState
    private val handManager: HandManager = HandManager(communicator, roomSettings)

    private var pendingPlayersBuffer: MutableSet<PlayerInfo> = mutableSetOf()

    fun start() {

        while (true) {

            // TODO: send hand start update

            // TODO: protect this CRITICAL SECTION
            // START OF CRITICAL SECTION
            currentGameState = updatePlayers(currentGameState)
            val newPlayersIds = pendingPlayersBuffer.map { it.id }.toSet()
            // END OF CRITICAL SECTION

            currentGameState = handManager.playHand(currentGameState, newPlayersIds)
            currentGameState = currentGameState.copy(handsPlayed = currentGameState.handsPlayed + 1)

            // TODO: send state after completed hand update
        }
    }

    fun registerNewPlayer(playerInfo: PlayerInfo, adapter: IPlayerAdapter) {
        /*
         * TODO:
         *  - update communicator's state
         *  - broadcast update about new player
         */
        this.pendingPlayersBuffer.add(playerInfo)
    }

    private fun updatePlayers(gameState: GameState): GameState {
        /*
         * TODO:
         *  - add pending players to game state
         *  - delete players that lost the game / lost the connection
         */
        return gameState
    }

    private fun clearPendingPlayersBuffer() {
        /*
         * TODO:
         *  - add synchronization to buffer clearing
         */
        this.pendingPlayersBuffer.clear()
    }
}