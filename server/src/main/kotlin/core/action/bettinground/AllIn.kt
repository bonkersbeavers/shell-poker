package core.action.bettinground

import core.gameflow.HandState

class AllIn : BettingAction(ActionType.ALL_IN) {

    // TODO
    override fun innerApply(handState: HandState): HandState = handState

    override fun innerIsLegal(handState: HandState): Boolean = true
}
