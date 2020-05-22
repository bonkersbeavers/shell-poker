package core.handflow.dealer

import core.cards.Card
import core.handflow.hand.*

data class DealRiver(val card: Card) : DealerAction() {
    override fun validate(handState: HandState): ActionValidation {
        return if (handState.bettingRound == BettingRound.TURN) {
            ValidAction
        } else {
            InvalidAction("cannot deal river when current betting round is ${handState.bettingRound}")
        }
    }

    override fun apply(handState: HandState): HandState {
        val newStage = when (handState.handStage) {
            HandStage.ALLIN_DUEL_STAGE -> HandStage.RESULTS_STAGE
            else -> HandStage.INTERACTIVE_STAGE
        }
        return handState.copy(communityCards = handState.communityCards + listOf(card), handStage = newStage)
    }
}
