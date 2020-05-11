package gameControl.gamestate

import core.RoomSettings
import gameControl.adapters.Communicator
import gameControl.adapters.IPlayerAdapter
import core.handflow.IDeprecatedHandManager
import gameControl.table.TableManager

class GameManager(
        val handManager: IDeprecatedHandManager,
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
