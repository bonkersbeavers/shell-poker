package core.hand

import core.RoomSettings
import core.hand.dealer.Dealer
import core.hand.utils.*

class HandManager(val settings: RoomSettings, val playerAdapter: LocalPlayerAdapter) {

    fun playHand(initialState: HandState): HandState {

        // initialize helper structures
        val dealer = Dealer()
        val handRecord = HandRecord(initialState)

        handRecord.register(HandInitialization)

        // shift positions
        handRecord.register(ShiftPositions(seatsNumber = settings.seatsNumber))

        // post blinds and ante
        val postActions = resolveBlindPosting(handRecord.resolveHandState(), emptyList())
        handRecord.registerSeries(postActions)

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

                    if (validation == false)
                        println("gowno")

                    else {
                        handRecord.register(action)
                        break
                    }
                }
            }

            handRecord.register(BettingRoundCleanup)
        }

        // final dealer's actions
        while (handRecord.resolveHandState().handIsOver.not()) {
            handRecord.register(dealer.getNextAction(handRecord.resolveHandState()))
        }

        playerAdapter.update(handRecord.resolveHandState())
        handRecord.register(HandCleanup)
        return handRecord.resolveHandState()
    }
}
