package core.pokerhands

import core.Card

class FourOfAKind(cards: List<Card>) : PokerHand(HandRank.FOUR_OF_A_KIND, cards) {

    override fun isValidHand(): Boolean = isFourOfAKind(cards)

    override fun compareWithinRank(other: PokerHand): Int {
        val thisQuadRank = this.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key
        val otherQuadRank = other.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key

        if (thisQuadRank != otherQuadRank)
            return thisQuadRank.strength - otherQuadRank.strength

        val thisKickers = this.cards.filter { it.rank != thisQuadRank }
        val otherKickers = other.cards.filter { it.rank != otherQuadRank }

        return compareKickers(thisKickers, otherKickers)
    }
}
