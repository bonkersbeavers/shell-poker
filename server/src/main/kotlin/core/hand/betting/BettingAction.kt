package core.hand.betting

import core.hand.ApplicableHandAction
import core.hand.ValidatableHandAction
import core.hand.PlayerAction

abstract class BettingAction(override val seat: Int): PlayerAction(seat), ApplicableHandAction, ValidatableHandAction
