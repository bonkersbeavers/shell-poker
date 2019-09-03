package core.pokerhands

import core.cards.Card

class FourOfAKind(cards: Set<Card>) : PokerHand(HandRank.FOUR_OF_A_KIND, cards) {

    override fun isValidHand(): Boolean = cards.isFourOfAKind()

    override fun compareWithinRank(other: PokerHand): Int {
        val thisQuadRank = this.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key
        val otherQuadRank = other.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key

        if (thisQuadRank != otherQuadRank)
            return thisQuadRank.strength - otherQuadRank.strength

        val thisKickers = this.cards.filter { it.rank != thisQuadRank }.toSet()
        val otherKickers = other.cards.filter { it.rank != otherQuadRank }.toSet()

        return compareKickers(thisKickers, otherKickers)
    }
}
