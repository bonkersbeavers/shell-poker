package core.pokerhands

import core.Card

class FullHouse(cards: List<Card>) : PokerHand(HandRank.FULL_HOUSE, cards) {

    override fun isValidHand(): Boolean = isFullHouse(cards)

    /** First compares the sets ranks, then pairs if necessary. */
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
