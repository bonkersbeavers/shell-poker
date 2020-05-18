package core.hand.dealer

import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.ValidatableHandAction

abstract class DealerAction : HandAction(), ApplicableHandAction, ValidatableHandAction
