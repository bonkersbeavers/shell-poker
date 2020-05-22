package core.handflow.betting

import core.handflow.hand.ApplicableHandAction
import core.handflow.hand.ValidatableHandAction
import core.handflow.player.PlayerAction

abstract class BettingAction(override val seat: Int): PlayerAction(seat), ApplicableHandAction, ValidatableHandAction
