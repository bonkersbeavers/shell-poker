package core.action.bettinground

import core.gameflow.HandState

class Raise(val size: Int) : BettingAction(ActionType.RAISE) {

    // TODO
    override fun innerApply(handState: HandState): HandState = handState

    // TODO
    override fun innerValidate(handState: HandState): ActionValidation = ValidAction()
}
