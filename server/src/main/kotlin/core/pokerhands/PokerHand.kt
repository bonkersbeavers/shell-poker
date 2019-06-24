package core.pokerhands

import core.Card


/** Checks if cards parameter is a list of five distinct cards. */
fun isValidHand(cards: List<Card>): Boolean = (cards.size == 5) and (cards.distinct().size == 5)



abstract class PokerHand(val rank: HandRank, val cards: List<Card>) {

    init { assert(isValidHand(cards)) }

    operator fun compareTo(other: PokerHand): Int {
        return if (rank != other.rank) rank.strength - other.rank.strength else compareWithinRank(other)
    }

    protected abstract fun compareWithinRank(other: PokerHand): Int
}