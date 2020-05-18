package core.hand.helpers

import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.HandState

data class ShiftPositions(val seatsNumber: Int): HandAction(), ApplicableHandAction {
    override fun apply(handState: HandState): HandState {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return handState
    }
}
