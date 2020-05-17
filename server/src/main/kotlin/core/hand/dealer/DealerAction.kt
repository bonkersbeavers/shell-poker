package core.hand.dealer

import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.ValidableHandAction

abstract class DealerAction : HandAction(), ApplicableHandAction, ValidableHandAction
