package core.pokerhands

import core.Card


class RoyalFlush(cards: List<Card>) : PokerHand(HandRank.ROYAL_FLUSH, cards) {

    override fun isValidHand(): Boolean = isRoyalFlush(cards)

    override fun compareWithinRank(other: PokerHand): Int = 1
}