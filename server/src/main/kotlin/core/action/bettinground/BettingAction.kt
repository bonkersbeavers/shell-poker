package core.action.bettinground

import core.gameflow.HandState

abstract class BettingAction {

    abstract fun apply(handState: HandState): HandState
    abstract fun isLegal(handState: HandState): Boolean
}
