package core.pokerhands

import core.Card
import core.CardRank

class Straight(cards: List<Card>) : PokerHand(HandRank.STRAIGHT, cards) {

    override fun isValidHand(): Boolean = isStraight(cards)

    override fun compareWithinRank(other: PokerHand): Int {
        val thisStraightRank = getStraightRank(this.cards)
        val otherStraightRank = getStraightRank(other.cards)

        return thisStraightRank.strength - otherStraightRank.strength
    }
}
