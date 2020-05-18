package core.hand.player.betting

import core.hand.ActionValidation
import core.hand.HandState
import core.hand.ValidAction

// Special betting action representing passive play (check if possible, if not -> fold).
data class CheckOrFold(override val seat: Int): BettingAction(seat) {
    override fun apply(handState: HandState): HandState {
        val checkAction = Check(seat)
        val foldAction = Fold(seat)

        return if (checkAction.validate(handState) is ValidAction) {
            checkAction.apply(handState)
        } else {
            foldAction.apply(handState)
        }
    }

    override fun validate(handState: HandState): ActionValidation = ValidAction
}
