package core.hand.showdown

import core.cards.Card

sealed class ShowdownAction(open val seat: Int)

data class ShowCards(override val seat: Int, val cards: Pair<Card, Card>) : ShowdownAction(seat)
data class MuckCards(override val seat: Int) : ShowdownAction(seat)
