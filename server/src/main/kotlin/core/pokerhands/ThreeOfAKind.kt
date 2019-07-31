package core.pokerhands

import core.cards.Card

class ThreeOfAKind(cards: List<Card>) : PokerHand(HandRank.THREE_OF_A_KIND, cards) {

    override fun isValidHand(): Boolean = isThreeOfAKind(cards)

    override fun compareWithinRank(other: PokerHand): Int {
        val thisThreeRank = this.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key
        val otherThreeRank = other.cards.groupBy { it.rank }.maxBy { it.value.size }!!.key

        if (thisThreeRank != otherThreeRank)
            return thisThreeRank.strength - otherThreeRank.strength

        val thisKickers = this.cards.filter { it.rank != thisThreeRank }
        val otherKickers = other.cards.filter { it.rank != otherThreeRank }

        return compareKickers(thisKickers, otherKickers)
    }
}
