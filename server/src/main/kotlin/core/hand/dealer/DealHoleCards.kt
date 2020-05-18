package core.hand.dealer

import core.cards.Card
import core.hand.ActionValidation
import core.hand.HandState
import core.hand.InvalidAction
import core.hand.ValidAction

data class DealHoleCards(val seatToCardsMapping: Map<Int, Pair<Card, Card>>) : DealerAction() {
    override fun validate(handState: HandState): ActionValidation {
        return if (handState.bettingRound == null) {
            ValidAction
        } else {
            InvalidAction("cannot deal hole cards when current betting round is ${handState.bettingRound}")
        }
    }

    override fun apply(handState: HandState): HandState {
        val newPlayers = handState.playersStates.map {
            it.copy(cards = seatToCardsMapping[it.seat])
        }

        return handState.copy(playersStates = newPlayers)
    }
}
