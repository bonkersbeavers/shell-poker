package core.pokerhands

import core.cards.Card

class StraightFlush(cards: List<Card>) : PokerHand(HandRank.STRAIGHT_FLUSH, cards) {

    override fun isValidHand(): Boolean = isStraightFlush(cards)

    override fun compareWithinRank(other: PokerHand): Int = compareStraights(this.cards, other.cards)
}
