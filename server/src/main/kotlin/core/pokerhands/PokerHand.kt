package core.pokerhands

import core.Card

abstract class PokerHand(val rank: HandRank, val cards: List<Card>) {

    init {
        // checks if cards parameter is a list of five distinct cards
        assert((cards.size == 5) and (cards.distinct().size == 5))
        assert(isValidHand())
    }

    operator fun compareTo(other: PokerHand): Int {
        return if (rank != other.rank) rank.strength - other.rank.strength else compareWithinRank(other)
    }

    protected abstract fun isValidHand(): Boolean
    protected abstract fun compareWithinRank(other: PokerHand): Int
}
