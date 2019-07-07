package core.pokerhands

import core.Card
import core.CardRank


class Straight(cards: List<Card>) : PokerHand(HandRank.STRAIGHT, cards) {

    override fun isValidHand(): Boolean = isStraight(cards)

    override fun compareWithinRank(other: PokerHand): Int {
        val thisRanks = this.cards.map{ it.rank }.toSet()
        val otherRanks = other.cards.map{ it.rank }.toSet()

        val thisHighestRank = getHighestRank(thisRanks)
        val otherHighestRank = getHighestRank(otherRanks)

        return thisHighestRank.strength - otherHighestRank.strength
    }

    private fun getHighestRank(ranks: Set<CardRank>): CardRank {
        val lowestStraight = (CardRank.TWO..CardRank.FIVE).toSet() + CardRank.ACE

        if (ranks == lowestStraight)
            return CardRank.FIVE
        else
        return ranks.maxBy { it.strength } !!
    }
}