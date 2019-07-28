package core.action.bettinground

import core.gameflow.HandState

class Call(val size: Int) : BettingAction(ActionType.CALL) {

    // TODO
    override fun innerApply(handState: HandState): HandState = handState

    // TODO
    override fun innerIsLegal(handState: HandState): Boolean = true
}
