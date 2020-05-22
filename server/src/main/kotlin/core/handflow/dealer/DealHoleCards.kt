package core.handflow.dealer

import core.cards.Card
import core.handflow.hand.ActionValidation
import core.handflow.hand.HandState
import core.handflow.hand.InvalidAction
import core.handflow.hand.ValidAction

data class DealHoleCards(val seatToCardsMapping: Map<Int, Pair<Card, Card>>) : DealerAction() {
    override fun validate(handState: HandState): ActionValidation {
        return if (handState.bettingRound == null) {
            ValidAction
        } else {
            InvalidAction("cannot deal hole cards when current betting round is ${handState.bettingRound}")
        }
    }

    override fun apply(handState: HandState): HandState {
        val newPlayers = handState.players.map {
            it.copy(cards = seatToCardsMapping[it.seat])
        }

        return handState.copy(players = newPlayers)
    }
}
