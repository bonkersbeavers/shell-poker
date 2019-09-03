package core.pokerhands

import core.cards.Card

class TwoPair(cards: Set<Card>) : PokerHand(HandRank.TWO_PAIR, cards) {

    override fun isValidHand(): Boolean = cards.isTwoPair()

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
