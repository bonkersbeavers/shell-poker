package core.pokerhands

import core.cards.Card

class Straight(cards: List<Card>) : PokerHand(HandRank.STRAIGHT, cards) {

    override fun isValidHand(): Boolean = isStraight(cards)

    override fun compareWithinRank(other: PokerHand): Int = compareStraights(this.cards, other.cards)
}
