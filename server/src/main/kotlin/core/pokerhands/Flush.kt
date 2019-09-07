package core.pokerhands

import core.cards.Card

class Flush(cards: Set<Card>) : PokerHand(HandRank.FLUSH, cards) {

    override fun isValidHand(): Boolean = cards.isFlush()

    override fun compareWithinRank(other: PokerHand) = compareKickers(cards, other.cards)
}
