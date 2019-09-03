package core.pokerhands

import core.cards.Card

class StraightFlush(cards: Set<Card>) : PokerHand(HandRank.STRAIGHT_FLUSH, cards) {

    override fun isValidHand(): Boolean = cards.isStraightFlush()

    override fun compareWithinRank(other: PokerHand): Int = compareStraights(this.cards, other.cards)
}
