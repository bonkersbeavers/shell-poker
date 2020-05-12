package gameControl

import core.RoomSettings
import core.betting.InvalidAction
import core.flowUtils.Blinds
import core.handflow.HandFlowActionType
import core.handflow.HandManager
import core.player.PlayerStatus
import gameControl.adapters.LocalPlayerAdapter

class LocalGameFlowController(val playerAdapter: LocalPlayerAdapter) {
    fun run(playersCount: Int, initStack: Int, blinds: Blinds, settings: RoomSettings) {
        val handManager = HandManager(settings)

        var playersStatuses = (0 until playersCount).map { PlayerStatus(seat = it, stack = initStack) } // mock players

        var firstHand = true

        while (true) {

            val newPlayersIds = emptyList<Int>() // get new players ids

            if (firstHand) {
                handManager.startNewHand(
                        playersStatuses = playersStatuses,
                        newPlayersIds = newPlayersIds,
                        blinds = blinds,
                        randomPositions = true
                )
                firstHand = false
            }

            else {
                handManager.startNewHand(
                        playersStatuses = playersStatuses,
                        newPlayersIds = newPlayersIds,
                        blinds = blinds
                )
            }

            playerAdapter.update(handManager.currentState!!)

            // interactive phase (betting and dealing next streets)
            while (handManager.playersInteractionIsOver.not()) {

                if (handManager.getNextActionType() == HandFlowActionType.PLAYER_ACTION) {

                    while(true) {
                        val action = playerAdapter.requestBettingAction()
                        val validation = action.validate(handManager.currentState!!)

                        if (validation is InvalidAction)
                            println(validation.reason)

                        else {
                            handManager.setNextPlayerAction(action)
                            break
                        }
                    }
                }

                handManager.takeAction()
                playerAdapter.update(handManager.currentState!!)
            }

            val showdownResults = handManager.getShowdownResults()
            println(showdownResults)

            // non-interactive phase (dealing final cards after action ended, e.g. due to all-ins)
            while (handManager.handIsOver.not()) {
                handManager.takeAction()
                playerAdapter.update(handManager.currentState!!)
            }

            val potResults = handManager.getPotResults()
            println(potResults)

            handManager.finalizeHand()
            playerAdapter.update(handManager.currentState!!)

            playersStatuses = handManager.currentState!!.players.map { PlayerStatus(it.seat, it.stack) }
        }
    }
}
