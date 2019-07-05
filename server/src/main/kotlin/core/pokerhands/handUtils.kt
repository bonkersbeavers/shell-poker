package core.pokerhands

import core.Card
import core.CardRank

fun makeHand(cards: List<Card>): PokerHand {
    return when {
//        isRoyalFlush(cards) -> RoyalFlush(cards)
//        isStraightFlush(cards) -> StraightFlush(cards)
//        isFourOfAKind(cards) -> FourOfAKind(cards)
        isFullHouse(cards) -> FullHouse(cards)
        isFlush(cards) -> Flush(cards)
//        isStraight(cards) -> Straight(cards)
        isThreeOfAKind(cards) -> ThreeOfAKind(cards)
        isTwoPair(cards) -> TwoPair(cards)
        isPair(cards) -> Pair(cards)
        else -> HighCard(cards)
    }
}

/**
 * Following methods check if given list of cards make certain poker hand.
 * It is assumed that cards parameter is always a valid 5-card hand.
 */

fun isPair(cards: List<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 2 }
}

fun isTwoPair(cards: List<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.count { it == 2 } == 2
}

fun isThreeOfAKind(cards: List<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 3 }
}

fun isStraight(cards: List<Card>): Boolean {

    val ranks = cards.map{ it.rank }.toSet()
    val lowest: CardRank = ranks.minBy { it.strength } !!

    return when(ranks) {
        (lowest..(lowest + 4)).toSet() -> true
        (CardRank.TWO..CardRank.FIVE).toSet() + CardRank.ACE -> true
        else -> false
    }
}

fun isFlush(cards: List<Card>): Boolean = cards.all { it.suit == cards[0].suit }

fun isFullHouse(cards: List<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 2 } and rankCounts.any { it == 3 }
}

fun isFourOfAKind(cards: List<Card>): Boolean {
    val rankCounts = cards.groupBy { it.rank }.map { it.value.size }
    return rankCounts.any { it == 4 }
}

fun isStraightFlush(cards: List<Card>): Boolean = isStraight(cards) and isFlush(cards)

fun isRoyalFlush(cards: List<Card>): Boolean {
    val isHighestStraight = cards.map { it.rank }.toSet() == (CardRank.TEN..CardRank.ACE).toSet()
    return isFlush(cards) and isHighestStraight
}


/**
 * Performs kickers comparison. Returns positive value if the first list contains
 * better kickers, 0 if both lists are equally strong and negative value otherwise.
 * It is assumed that both lists are equally long.
 */
fun compareKickers(kickersA: List<Card>, kickersB: List<Card>): Int {
    val ranksA = kickersA.map { it.rank }.sortedDescending()
    val ranksB = kickersB.map { it.rank }.sortedDescending()

    (ranksA zip ranksB).forEach {
        if (it.first != it.second) return it.first.strength - it.second.strength
    }

    return 0
}
