package core.hand.dealer

import core.cards.Card
import core.hand.HandState

data class DealHoleCards(val seatToCardsMapping: Map<Int, Pair<Card, Card>>) : DealerAction() {
    override fun validate(handState: HandState): Boolean = handState.bettingRound == null

    override fun apply(handState: HandState): HandState {
        val newPlayers = handState.playersStates.map {
            it.copy(cards = seatToCardsMapping[it.seat])
        }

        return handState.copy(playersStates = newPlayers)
    }
}
