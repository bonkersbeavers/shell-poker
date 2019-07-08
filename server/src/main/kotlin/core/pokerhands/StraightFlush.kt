package core.pokerhands

import core.Card
import core.CardRank

class StraightFlush(cards: List<Card>) : PokerHand(HandRank.STRAIGHT_FLUSH, cards) {

    override fun isValidHand(): Boolean = isStraightFlush(cards)

    override fun compareWithinRank(other: PokerHand): Int {
        val thisStraightRank = getStraightRank(this.cards)
        val otherStraightRank = getStraightRank(other.cards)

        return thisStraightRank.strength - otherStraightRank.strength
    }
}
