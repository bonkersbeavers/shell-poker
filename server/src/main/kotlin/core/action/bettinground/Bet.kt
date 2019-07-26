package core.action.bettinground

import core.gameflow.HandState

class Bet(val size: Int) : BettingAction(){

    override fun innerApply(handState: HandState): HandState {
        return handState
    }

    override fun innerIsLegal(handState: HandState): Boolean = true
}
