package core.handflow.showdown

import core.cards.Card
import core.handflow.hand.HandAction

sealed class ShowdownAction(open val seat: Int) : HandAction()

data class ShowCards(override val seat: Int, val cards: Pair<Card, Card>) : ShowdownAction(seat)
data class MuckCards(override val seat: Int) : ShowdownAction(seat)
