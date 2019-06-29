package core.pokerhands

import core.Card


fun isPair(cards: List<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 2 }
}



class Pair(cards: List<Card>) : PokerHand(HandRank.PAIR, cards) {

    init { assert(isPair(cards)) }

    /** First compares pair ranks. If those are the same, then compares kickers. */
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
