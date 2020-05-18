package core.hand.dealer

import core.cards.Card
import core.flowUtils.BettingRound
import core.hand.ActionValidation
import core.hand.HandState
import core.hand.InvalidAction
import core.hand.ValidAction

data class DealFlop(val cards: Triple<Card, Card, Card>) : DealerAction() {
    override fun validate(handState: HandState): ActionValidation {
        return if (handState.bettingRound == BettingRound.PRE_FLOP) {
            ValidAction
        } else {
            InvalidAction("cannot deal flop when current betting round is ${handState.bettingRound}")
        }
    }

    override fun apply(handState: HandState): HandState {
        return handState.copy(communityCards = cards.toList())
    }
}
