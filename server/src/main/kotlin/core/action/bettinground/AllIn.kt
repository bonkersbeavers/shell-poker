package core.action.bettinground

import core.gameflow.HandState

class AllIn : BettingAction() {

    // TODO
    override fun innerApply(handState: HandState): HandState = handState

    override fun innerIsLegal(handState: HandState): Boolean = true
}
