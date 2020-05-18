package core.hand

import core.RoomSettings
import core.hand.blinds.getBlindsPostActionsSequence
import core.hand.pot.getPotActionsSequence
import core.hand.dealer.Dealer
import core.hand.helpers.CollectBets
import core.hand.helpers.InitializeHand
import core.hand.positions.ShiftPositions
import core.hand.showdown.getShowdownActionsSequence

class HandManager(val settings: RoomSettings, val playerAdapter: LocalPlayerAdapter) {

    private var handNumber: Int = 0

    fun playHand(initialState: HandState): HandState {
        handNumber++
        println("===================================== HAND NUMBER $handNumber ".padEnd(87, padChar = '='))

        // initialize helper structures
        val dealer = Dealer()
        val handRecord = HandRecord(initialState)

        handRecord.register(InitializeHand)

        // shift positions
        handRecord.register(ShiftPositions(seatsNumber = settings.seatsNumber))

        // TODO() collect ante

        // post blinds
        val blindsPostActions = getBlindsPostActionsSequence(handRecord.resolveHandState(), emptyList())
        handRecord.registerSequence(blindsPostActions)

        // players interaction phase
        while (handRecord.resolveHandState().playersInteractionIsOver.not()) {

            // dealer action
            handRecord.register(dealer.getNextAction(handRecord.resolveHandState()))

            // players actions
            while (handRecord.resolveHandState().activePlayer != null) {
                val state = handRecord.resolveHandState()
                playerAdapter.update(state)

                while (true) {
                    val action = playerAdapter.requestBettingAction(state.activePlayer!!.seat)
                    val validation = action.validate(state)

                    if (validation is InvalidAction)
                        println(validation.reason)

                    else {
                        handRecord.register(action)
                        break
                    }
                }
            }

            handRecord.register(CollectBets)
        }

        // showdown phase
        val showdownSequence = getShowdownActionsSequence(handRecord.resolveHandState())
        playerAdapter.printShowdown(showdownSequence)
        handRecord.registerSequence(showdownSequence)

        // final dealer's actions
        while (handRecord.resolveHandState().handIsOver.not()) {
            handRecord.register(dealer.getNextAction(handRecord.resolveHandState()))
        }

        val potDistributionSequence = getPotActionsSequence(handRecord.resolveHandState())
        handRecord.registerSequence(potDistributionSequence)
        playerAdapter.printPotDistribution(potDistributionSequence)

        println("\nfinal results: ")
        playerAdapter.update(handRecord.resolveHandState())
        return handRecord.resolveHandState()
    }
}
