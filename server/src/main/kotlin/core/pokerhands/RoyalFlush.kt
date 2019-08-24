package core.pokerhands

import core.cards.Card

class RoyalFlush(cards: Set<Card>) : PokerHand(HandRank.ROYAL_FLUSH, cards) {

    override fun isValidHand(): Boolean = isRoyalFlush(cards)

    override fun compareWithinRank(other: PokerHand): Int = 0
}
