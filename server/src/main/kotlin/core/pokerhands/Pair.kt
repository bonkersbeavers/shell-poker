package core.pokerhands

import core.Card

class Pair(cards: List<Card>) : PokerHand(HandRank.PAIR, cards) {

    override fun isValidHand(): Boolean = isPair(cards)

    override fun compareWithinRank(other: PokerHand): Int {
        val thisPairRank = this.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key
        val otherPairRank = other.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key

        if (thisPairRank != otherPairRank)
            return thisPairRank.strength - otherPairRank.strength

        val thisKickers = this.cards.filter { it.rank != thisPairRank }
        val otherKickers = other.cards.filter { it.rank != otherPairRank }

        return compareKickers(thisKickers, otherKickers)
    }
}
