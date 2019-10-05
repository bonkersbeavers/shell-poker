package core.gameflow.gamestate

import core.adapters.Communicator
import core.adapters.IPlayerAdapter
import core.gameflow.*
import core.gameflow.handstate.IHandManager
import core.gameflow.table.TableManager

class GameManager(
    val handManager: IHandManager,
    val communicator: Communicator,
    val roomSettings: RoomSettings,
    initialState: GameState
) {

    private val tableManager: TableManager = TableManager()
    private var currentGameState: GameState = initialState

    fun start() {

        while (true) {

            // TODO: send hand start update

            currentGameState = tableManager.getUpdatedGameState(currentGameState)
            currentGameState = handManager.playHand(currentGameState)
            currentGameState = currentGameState.copy(handsPlayed = currentGameState.handsPlayed + 1)

            // TODO: send state after completed hand update
        }
    }

    fun registerNewPlayer(position: Int, adapter: IPlayerAdapter) {
        this.tableManager.registerNewPlayer(position)
        this.communicator.addAdapter(adapter)
    }

    fun unregisterPlayer(position: Int) {
        TODO()
    }
}
