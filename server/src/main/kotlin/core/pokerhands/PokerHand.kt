package core.pokerhands

import core.cards.Card

abstract class PokerHand(val rank: HandRank, val cards: Set<Card>) : Comparable<PokerHand> {

    init {
        assert(isValidHand())
    }

    override operator fun compareTo(other: PokerHand): Int {
        return if (rank != other.rank) rank.strength - other.rank.strength else compareWithinRank(other)
    }

    protected abstract fun isValidHand(): Boolean
    protected abstract fun compareWithinRank(other: PokerHand): Int
}
