package core.action.bettinground

import core.gameflow.HandState

class Post(val size: Int) : BettingAction(ActionType.POST) {

    // TODO
    override fun innerApply(handState: HandState): HandState = handState

    // TODO
    override fun innerIsLegal(handState: HandState): Boolean = true
}
