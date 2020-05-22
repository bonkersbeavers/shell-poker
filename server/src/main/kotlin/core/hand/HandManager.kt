package core.hand

import core.RoomSettings
import core.hand.blinds.getBlindsPostActionsSequence
import core.hand.dealer.BettingRound
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
        val currentState = { -> handRecord.resolveHandState() }

        // shift positions
        handRecord.register(ShiftPositions(seatsNumber = settings.seatsNumber))

        // TODO() collect ante

        // post blinds
        val blindsPostActions = getBlindsPostActionsSequence(currentState(), emptyList())
        handRecord.registerSequence(blindsPostActions)

        // players interaction
        while (currentState().handStage == HandStage.INTERACTIVE_STAGE) {

            // dealer action
            handRecord.register(dealer.getNextAction(currentState()))

            // players actions
            while (currentState().activePlayer != null) {
                playerAdapter.update(currentState())

                while (true) {
                    val action = playerAdapter.requestBettingAction(currentState().activePlayer!!.seat)
                    val validation = action.validate(currentState())

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
        val showdownSequence = getShowdownActionsSequence(currentState())
        playerAdapter.printShowdown(showdownSequence)
        handRecord.registerSequence(showdownSequence)

        // final dealer's actions
        while (currentState().handStage == HandStage.ALLIN_DUEL_STAGE) {
            handRecord.register(dealer.getNextAction(currentState()))
        }

        val potDistributionSequence = getPotActionsSequence(currentState())
        handRecord.registerSequence(potDistributionSequence)
        playerAdapter.printPotDistribution(potDistributionSequence)

        println("\nfinal results: ")
        playerAdapter.update(currentState())
        return handRecord.resolveHandState()
    }
}
