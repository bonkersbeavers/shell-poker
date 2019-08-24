package core.pokerhands

import core.Card

abstract class PokerHand(val rank: HandRank, val cards: Set<Card>) {

    init {
        assert(cards.size == 5)
        assert(isValidHand())
    }

    operator fun compareTo(other: PokerHand): Int {
        return if (rank != other.rank) rank.strength - other.rank.strength else compareWithinRank(other)
    }

    protected abstract fun isValidHand(): Boolean
    protected abstract fun compareWithinRank(other: PokerHand): Int
}
