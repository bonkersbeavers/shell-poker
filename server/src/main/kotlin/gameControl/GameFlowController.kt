package gameControl

//import core.RoomSettings
//import core.betting.AllIn
//import core.flowUtils.Blinds
//import core.handflow.HandFlowActionType
//import core.handflow.HandManager
//import core.player.PlayerStatus
//
//class GameFlowController(val settings: RoomSettings) {
//    fun run() {
//        val handManager = HandManager(settings)
//
//        val players = emptyList<PlayerStatus>() // mock players
//        val blinds = Blinds(50, 100) // mock blinds
//
//        while (true) {
//
//            val newPlayersIds = emptyList<Int>() // get new players ids
//
//            handManager.startNewHand(
//                    playersStatuses = players,
//                    newPlayersIds = newPlayersIds,
//                    blinds = blinds
//            )
//
//            // broadcast initial state
//
//            // interactive phase (betting and dealing next streets)
//            while (handManager.playersInteractionIsOver.not()) {
//
//                if (handManager.nextActionType == HandFlowActionType.PLAYER_ACTION) {
//                    val action = AllIn // get proper player's action
//                    handManager.setNextPlayerAction(action)
//                }
//
//                handManager.takeAction()
//                // broadcast update
//            }
//
//            val showdownResults = handManager.getShowdownResults()
//            // broadcast showdown
//
//            // non-interactive phase (dealing final cards after action ended, e.g. due to all-ins)
//            while (handManager.handIsOver.not()) {
//                handManager.takeAction()
//                // broadcast update
//            }
//
//            val potResults = handManager.getPotResults()
//            // broadcast pot results
//
//            handManager.finalizeHand()
//            // broadcast final state
//        }
//    }
//}
