package core.pokerhands

import core.Card

class HighCard(cards: List<Card>) : PokerHand(HandRank.HIGH_CARD, cards) {

    override fun compareWithinRank(other: PokerHand) = compareKickers(cards, other.cards)
}