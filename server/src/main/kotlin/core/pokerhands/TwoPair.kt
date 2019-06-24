package core.pokerhands

import core.Card


fun isTwoPair(cards: List<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.count { it == 2 } == 2
}



class TwoPair(cards: List<Card>) : PokerHand(HandRank.TWO_PAIR, cards) {

    init { assert(isTwoPair(cards)) }

    /**
     * First tries to find the highest pair rank that is different for each hand.
     * If no such exists, compares the remaining kicker.
     */
    override fun compareWithinRank(other: PokerHand): Int {
        val thisPairRanks = this.cards.groupBy { it.rank }.filterValues { it.size == 2 }.keys.sortedDescending()
        val otherPairRanks = other.cards.groupBy { it.rank }.filterValues { it.size == 2 }.keys.sortedDescending()

        val differentPair = (thisPairRanks zip otherPairRanks).find { it.first != it.second }

        if (differentPair != null)
            return differentPair.first.strength - differentPair.second.strength

        val thisKicker = this.cards.find { it.rank !in thisPairRanks }!!
        val otherKicker = other.cards.find { it.rank !in otherPairRanks }!!

        return thisKicker.rank.strength - otherKicker.rank.strength
    }
}