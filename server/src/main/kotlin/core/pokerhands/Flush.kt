package core.pokerhands

import core.Card


class Flush(cards: List<Card>) : PokerHand(HandRank.FLUSH, cards) {

    override fun isValidHand(): Boolean = isFlush(cards)

    override fun compareWithinRank(other: PokerHand) = compareKickers(cards, other.cards)
}