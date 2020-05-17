package core.hand.dealer

import core.cards.Card
import core.flowUtils.BettingRound
import core.hand.HandState

data class DealFlop(val cards: Triple<Card, Card, Card>) : DealerAction() {
    override fun validate(handState: HandState): Boolean = handState.bettingRound == BettingRound.PRE_FLOP

    override fun apply(handState: HandState): HandState {
        return handState.copy(communityCards = cards.toList())
    }
}
