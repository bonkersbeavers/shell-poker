package core.pokerhands

import core.cards.Card

class Pair(cards: Set<Card>) : PokerHand(HandRank.PAIR, cards) {

    override fun isValidHand(): Boolean = cards.isPair()

    override fun compareWithinRank(other: PokerHand): Int {
        val thisPairRank = this.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key
        val otherPairRank = other.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key

        if (thisPairRank != otherPairRank)
            return thisPairRank.strength - otherPairRank.strength

        val thisKickers = this.cards.filter { it.rank != thisPairRank }.toSet()
        val otherKickers = other.cards.filter { it.rank != otherPairRank }.toSet()

        return compareKickers(thisKickers, otherKickers)
    }
}
