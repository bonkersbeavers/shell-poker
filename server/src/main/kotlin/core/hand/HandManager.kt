package core.hand

import core.RoomSettings
import core.hand.blinds.getPostActionsSequence
import core.hand.pot.getPotActionsSequence
import core.hand.dealer.Dealer
import core.hand.helpers.CollectBets
import core.hand.helpers.FinalizeHand
import core.hand.helpers.InitializeHand
import core.hand.positions.ShiftPositions

class HandManager(val settings: RoomSettings, val playerAdapter: LocalPlayerAdapter) {

    fun playHand(initialState: HandState): HandState {

        // initialize helper structures
        val dealer = Dealer()
        val handRecord = HandRecord(initialState)

        handRecord.register(InitializeHand)

        // shift positions
        handRecord.register(ShiftPositions(seatsNumber = settings.seatsNumber))

        // post blinds and ante
        val postActions = getPostActionsSequence(handRecord.resolveHandState(), emptyList())
        handRecord.registerSequence(postActions)

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

        // final dealer's actions
        while (handRecord.resolveHandState().handIsOver.not()) {
            handRecord.register(dealer.getNextAction(handRecord.resolveHandState()))
        }

        val potDistributionSequence = getPotActionsSequence(handRecord.resolveHandState())
        handRecord.registerSequence(potDistributionSequence)
        println(potDistributionSequence.joinToString(", "))

        //playerAdapter.update(handRecord.resolveHandState())
        handRecord.register(FinalizeHand)
        return handRecord.resolveHandState()
    }
}
