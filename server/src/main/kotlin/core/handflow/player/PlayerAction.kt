package core.handflow.player

import core.handflow.hand.HandAction

abstract class PlayerAction(open val seat: Int) : HandAction()
