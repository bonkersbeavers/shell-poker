package core.action.bettinground

import core.gameflow.HandState

class Bet(val size: Int) : BettingAction() {

    // TODO
    override fun innerApply(handState: HandState): HandState = handState

    // TODO
    override fun innerIsLegal(handState: HandState): Boolean = true
}
