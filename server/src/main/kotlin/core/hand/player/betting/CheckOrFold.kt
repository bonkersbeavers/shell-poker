package core.hand.player.betting

import core.betting.ActionType
import core.hand.HandState

// Special betting action representing passive play (check if possible, if not -> fold).
data class CheckOrFold(override val seat: Int): BettingAction(seat) {
    override val actionType: ActionType = TODO(reason = "not sure how to handle")

    override fun apply(handState: HandState): HandState {
        val checkAction = Check(seat)
        val foldAction = Fold(seat)

        return if (checkAction.validate(handState)) checkAction.apply(handState) else foldAction.apply(handState)
    }

    override fun validate(handState: HandState): Boolean = true
}
