package core.hand.player.betting

import core.betting.ActionType
import core.hand.ApplicableHandAction
import core.hand.ValidableHandAction
import core.hand.player.PlayerAction

abstract class BettingAction(override val seat: Int): PlayerAction(seat), ApplicableHandAction, ValidableHandAction {
    abstract val actionType: ActionType
}
