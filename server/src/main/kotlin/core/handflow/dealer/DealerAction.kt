package core.handflow.dealer

import core.handflow.hand.ApplicableHandAction
import core.handflow.hand.HandAction
import core.handflow.hand.ValidatableHandAction

abstract class DealerAction : HandAction(), ApplicableHandAction, ValidatableHandAction
