package core.hand.dealer

import core.cards.Card
import core.flowUtils.BettingRound
import core.hand.HandState

data class DealRiver(val card: Card) : DealerAction() {
    override fun validate(handState: HandState): Boolean = handState.bettingRound == BettingRound.TURN

    override fun apply(handState: HandState): HandState {
        return handState.copy(communityCards = handState.communityCards + listOf(card))
    }
}
