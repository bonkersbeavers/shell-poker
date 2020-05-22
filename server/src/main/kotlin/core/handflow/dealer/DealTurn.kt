package core.handflow.dealer

import core.cards.Card
import core.handflow.hand.ActionValidation
import core.handflow.hand.HandState
import core.handflow.hand.InvalidAction
import core.handflow.hand.ValidAction

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
