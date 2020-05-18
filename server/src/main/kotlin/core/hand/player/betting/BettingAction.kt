package core.hand.player.betting

import core.hand.ApplicableHandAction
import core.hand.ValidatableHandAction
import core.hand.player.PlayerAction

abstract class BettingAction(override val seat: Int): PlayerAction(seat), ApplicableHandAction, ValidatableHandAction
