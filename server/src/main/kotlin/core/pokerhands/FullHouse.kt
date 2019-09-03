package core.pokerhands

import core.cards.Card

class FullHouse(cards: Set<Card>) : PokerHand(HandRank.FULL_HOUSE, cards) {

    override fun isValidHand(): Boolean = cards.isFullHouse()

    override fun compareWithinRank(other: PokerHand): Int {
        val thisSetRank = this.cards.groupBy { it.rank }.filterValues { it.size == 3 }.keys.first()
        val otherSetRank = other.cards.groupBy { it.rank }.filterValues { it.size == 3 }.keys.first()

        if (thisSetRank != otherSetRank)
            return thisSetRank.strength - otherSetRank.strength

        val thisPairRank = this.cards.groupBy { it.rank }.filterValues { it.size == 2 }.keys.first()
        val otherPairRank = other.cards.groupBy { it.rank }.filterValues { it.size == 2 }.keys.first()

        return thisPairRank.strength - otherPairRank.strength
    }
}
