package core.hand.dealer

import core.cards.Card
import core.hand.ActionValidation
import core.hand.HandState
import core.hand.InvalidAction
import core.hand.ValidAction

data class DealTurn(val card: Card) : DealerAction() {
    override fun validate(handState: HandState): ActionValidation {
        return if (handState.bettingRound == BettingRound.FLOP) {
            ValidAction
        } else {
            InvalidAction("cannot deal turn when current betting round is ${handState.bettingRound}")
        }
    }

    override fun apply(handState: HandState): HandState {
        return handState.copy(communityCards = handState.communityCards + listOf(card))
    }
}
